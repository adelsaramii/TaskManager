package com.example.taskmanager.model

import com.example.taskmanager.model.api.ApiService
import com.example.taskmanager.model.local.TaskDao
import com.example.taskmanager.model.local.TaskModel
import com.example.taskmanager.utils.taskToJsonObject

class MainRepository(
    private val apiService: ApiService,
    private val taskDao: TaskDao
) {
    fun getAllTasks(): List<TaskModel> {
        return taskDao.getAllTask()
    }

    suspend fun refreshTasks() {
        apiService.getAllTask()
    }

    suspend fun insertTask(task: TaskModel) {
        apiService.insertTask(taskToJsonObject(task))
        taskDao.addTask(task)
    }

    suspend fun updateTask(task: TaskModel) {
        apiService.updateTask(task.id, taskToJsonObject(task))
        taskDao.updateTask(task.id, task.title, task.description, task.url)
    }

    suspend fun deleteTask(task: TaskModel) {
        apiService.deleteTask(task.id)
        taskDao.deleteTask(task)
    }

    suspend fun deleteAllTasks() {
        apiService.deleteAllTask()
        taskDao.deleteAllTask()
    }

}