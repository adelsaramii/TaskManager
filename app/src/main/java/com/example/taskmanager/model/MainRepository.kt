package com.example.taskmanager.model

import androidx.lifecycle.LiveData
import com.example.taskmanager.model.api.ApiService
import com.example.taskmanager.model.local.TaskDao
import com.example.taskmanager.model.local.TaskModel
import com.example.taskmanager.utils.taskToJsonObject

class MainRepository(
    private val apiService: ApiService,
    private val taskDao: TaskDao
) : MainRepositoryInterface {

    override fun getAllTasks(): LiveData<List<TaskModel>> {
        return taskDao.getAllTask()
    }

    override suspend fun refreshTasks() {
        taskDao.deleteAllTask()
        taskDao.addAll(apiService.getAllTask())
    }

    override suspend fun insertTask(task: TaskModel) {
        taskDao.addTask(task)
        apiService.insertTask(taskToJsonObject(taskDao.getTask(task.title , task.description , task.date , task.time , task.state , task.url)))
    }

    override suspend fun updateTask(task: TaskModel) {
        apiService.updateTask(taskToJsonObject(task))
        taskDao.updateTask(task)
    }

    override suspend fun deleteTask(task: TaskModel) {
        apiService.deleteTask(task.id)
        taskDao.deleteTask(task)
    }

    override suspend fun deleteAllTasks() {
        apiService.deleteAllTask()
        taskDao.deleteAllTask()
    }

}