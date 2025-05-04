package com.example.whatdotodo.ui.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.whatdotodo.data.model.ToDoItem
import com.example.whatdotodo.service.NotificationHelper
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun ToDoListScreen(navController: NavController) {
    val context = LocalContext.current
    val notificationHelper = remember { NotificationHelper(context) }


    val todoList = remember { mutableStateListOf<ToDoItem>() }
    var newTaskTitle by remember { mutableStateOf(TextFieldValue("")) }
    var newTaskCategory by remember { mutableStateOf(TextFieldValue("")) }
    var showCompleted by remember { mutableStateOf(false) } // Collapsible completed tasks

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Your Tasks", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = newTaskTitle,
            onValueChange = { newTaskTitle = it },
            label = { Text("Task Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = newTaskCategory,
            onValueChange = { newTaskCategory = it },
            label = { Text("Category (optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (newTaskTitle.text.isNotEmpty()) {
                    todoList.add(
                        ToDoItem(
                            title = newTaskTitle.text,
                            category = newTaskCategory.text.ifEmpty { "General" },
                            done = false
                        )
                    )
                    newTaskTitle = TextFieldValue("")
                    newTaskCategory = TextFieldValue("")
                    Toast.makeText(context, "Task Added", Toast.LENGTH_SHORT).show()
                    notificationHelper.showNotification(todoList.firstOrNull { !it.done }?.title ?: "No tasks pending")

                } else {
                    Toast.makeText(context, "Task Title cannot be empty", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Task")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Pending Tasks", style = MaterialTheme.typography.titleLarge)

        val coroutineScope = rememberCoroutineScope()
        val listState = rememberLazyListState()

        var draggedItemIndex by remember { mutableStateOf<Int?>(null) }

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(todoList.filter { !it.done }) { index, task ->
                val realIndex = todoList.indexOf(task)

                TaskItem(
                    task = task,
                    onCheckedChange = { checked ->
                        val globalIndex = todoList.indexOf(task)
                        if (globalIndex != -1) {
                            todoList[globalIndex] = task.copy(done = checked)
                            notificationHelper.showNotification(
                                todoList.firstOrNull { !it.done }?.title ?: "No tasks pending"
                            )
                        }
                    },
                    modifier = Modifier
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = {
                                    draggedItemIndex = realIndex
                                },
                                onDragEnd = {
                                    draggedItemIndex = null
                                    // update notification again just to be safe
                                    notificationHelper.showNotification(
                                        todoList.firstOrNull { !it.done }?.title ?: "No tasks pending"
                                    )
                                },
                                onDrag = { change, dragAmount ->
                                    val targetIndex = realIndex + if (dragAmount.y > 0) 1 else -1
                                    if (targetIndex in todoList.indices && targetIndex != realIndex) {
                                        todoList.swap(realIndex, targetIndex)
                                        draggedItemIndex = targetIndex
                                    }
                                }
                            )
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showCompleted = !showCompleted }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (showCompleted) Icons.Filled.ArrowDropDown else Icons.Filled.ArrowForward,
                contentDescription = null
            )
            Text("Completed Tasks", style = MaterialTheme.typography.titleLarge)
        }

        if (showCompleted) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(todoList.filter { it.done }) { task ->
                    TaskItem(task = task, onCheckedChange = { checked ->
                        val index = todoList.indexOf(task)
                        if (index != -1) {
                            todoList[index] = task.copy(done = checked)
                        }
                    })
                }
            }
        }
    }
}

@Composable
fun TaskItem(
    task: ToDoItem,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(task.title, style = MaterialTheme.typography.bodyLarge)
                Text("Category: ${task.category}", style = MaterialTheme.typography.bodySmall)
            }
            Checkbox(
                checked = task.done,
                onCheckedChange = onCheckedChange
            )
        }
    }
}


fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    val tmp = this[index1]
    this[index1] = this[index2]
    this[index2] = tmp
}
