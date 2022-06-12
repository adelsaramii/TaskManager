package com.example.taskmanager.model

import com.example.taskmanager.model.local.TaskModel

interface MainRepositoryInterface {
    fun getAllTasks() : List<TaskModel>
    suspend fun refreshTasks()
    suspend fun insertTask(task: TaskModel)
    suspend fun updateTask(task: TaskModel)
    suspend fun deleteTask(task: TaskModel)
    suspend fun deleteAllTasks()
}