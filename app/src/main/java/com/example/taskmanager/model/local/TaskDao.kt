package com.example.taskmanager.model.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {

    @Query("select * from taskTable")
    fun getAllTask(): LiveData<List<TaskModel>>

    @Query("select * from taskTable where title = :title and description = :description and date = :date and time = :time and state =:state and url = :url")
    fun getTask(title :String , description :String , date : String , time : String , state :String , url :String): TaskModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTask(addTask: TaskModel)

    @Update
    fun updateTask(newTask : TaskModel)

    @Delete
    fun deleteTask(deleteTask: TaskModel)

    @Query("Delete From taskTable")
    fun deleteAllTask()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(tasks : List<TaskModel>)
}