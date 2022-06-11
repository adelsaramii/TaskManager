package com.example.taskmanager.model.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.taskmanager.model.TaskModel

@Dao
interface TaskDao {
    @Query("select * from taskTable")
    fun getAllTask(): LiveData<List<TaskModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTask(addTask: TaskModel)

    @Query("UPDATE taskTable SET title =:updateTitle, description=:updateDescription , url=:updateUrl where userID=:id")
    fun updateTask(id: Int, updateTitle: String, updateDescription: String , updateUrl:String)

    @Delete
    fun deleteTask(deleteTask: TaskModel)

    @Query("Delete From taskTable")
    fun deleteAllTask()
}