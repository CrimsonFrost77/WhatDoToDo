// ui/screen/ToDoListScreen.kt

package com.example.whatdotodo.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.whatdotodo.data.model.ToDoItem

@Composable
fun ToDoListScreen(navController: NavController) {
    val context = LocalContext.current

    val todoList by remember { mutableStateOf(mutableListOf<ToDoItem>()) }
    var newTaskTitle by remember { mutableStateOf(TextFieldValue("")) }
    var newTaskCategory by remember { mutableStateOf(TextFieldValue("")) }

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
                } else {
                    Toast.makeText(context, "Task Title cannot be empty", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Task")
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(todoList) { task ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(task.title, style = MaterialTheme.typography.bodyLarge)
                        Text("Category: ${task.category}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

    }
}
