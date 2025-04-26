// data/model/ToDoItem.kt

package com.example.whatdotodo.data.model

data class ToDoItem(
    val title: String,
    val category: String,
    var done: Boolean
)
