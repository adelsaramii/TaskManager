package com.example.taskmanager.features

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.model.MainRepositoryInterface
import com.example.taskmanager.model.local.TaskModel
import kotlinx.coroutines.launch

class MainViewModel(private val mainRepository: MainRepositoryInterface) : ViewModel() {

    init {
        viewModelScope.launch {
            mainRepository.refreshTasks()
        }
    }

    fun getAllTasks(): LiveData<List<TaskModel>> {
        return mainRepository.getAllTasks()
    }

    suspend fun insertTask(task: TaskModel) {
        mainRepository.insertTask(task)
    }

    suspend fun updateTask(task: TaskModel) {
        mainRepository.updateTask(task)
    }

    suspend fun deleteTask(task: TaskModel) {
        mainRepository.deleteTask(task)
    }

    suspend fun deleteAllTasks() {
        mainRepository.deleteAllTasks()
    }

}