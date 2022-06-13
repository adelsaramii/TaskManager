package com.example.taskmanager.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "taskTable")
data class TaskModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String = "",
    var description: String = "",
    var url: String = "",
    var date: String = "",
    var time: String = "",
    var state: String = "todo"
)
