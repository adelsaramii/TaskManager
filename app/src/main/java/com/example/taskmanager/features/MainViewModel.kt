package com.example.taskmanager.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.model.MainRepository
import com.example.taskmanager.model.api.ApiService
import com.example.taskmanager.model.local.TaskModel
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    init {
        viewModelScope.launch{
            mainRepository.refreshTasks()
        }
    }

    private fun getAllTasks(){
        mainRepository.getAllTasks()
    }

    suspend fun insertTask(task: TaskModel){
        mainRepository.insertTask(task)
    }

    suspend fun updateTask(task: TaskModel){
        mainRepository.updateTask(task)
    }

    suspend fun deleteTask(task: TaskModel){
        mainRepository.deleteTask(task)
    }

    suspend fun deleteAllTasks(){
        mainRepository.deleteAllTasks()
    }

}