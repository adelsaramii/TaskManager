package com.example.taskmanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "taskTable")
data class TaskModel(
    @PrimaryKey(autoGenerate = true)
    var userID: Int = 0,
    val title: String,
    val description: String,
    val url: String
)
