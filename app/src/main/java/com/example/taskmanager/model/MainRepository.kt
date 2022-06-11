package com.example.taskmanager.model

import androidx.lifecycle.LiveData
import com.example.taskmanager.model.api.ApiService
import com.example.taskmanager.model.local.TaskDao
import com.example.taskmanager.utils.taskToJsonObject

class MainRepository(
    private val apiService: ApiService,
    private val taskDao: TaskDao
) {
    fun getAllTasks(): LiveData<List<TaskModel>> {
        return taskDao.getAllTask()
    }

    suspend fun refreshTasks() {
        apiService.getAllTask()
    }

    suspend fun insertTask(task: TaskModel){
        apiService.insertTask(taskToJsonObject(task))
    }

    suspend fun updateTask(id: Int, task: TaskModel){
        apiService.updateTask(id, taskToJsonObject(task))
        taskDao.updateTask(id, task.title, task.description, task.url)
    }

    suspend fun deleteTask(id :Int){
        apiService.deleteTask(id)
    }

    suspend fun deleteAllTasks(){
        apiService.deleteAllTask()
        taskDao.deleteAllTask()
    }

}