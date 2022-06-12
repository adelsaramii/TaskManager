package com.example.taskmanager.model.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {
    @Query("select * from taskTable")
    fun getAllTask(): LiveData<List<TaskModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTask(addTask: TaskModel)

    @Query("UPDATE taskTable SET title =:updateTitle, description=:updateDescription , url=:updateUrl , date=:updateDate , time=:updateTime , state=:updateState where id=:id")
    fun updateTask(id: Int, updateTitle: String, updateDescription: String , updateUrl:String ,  updateDate:String, updateTime:String , updateState:String)

    @Delete
    fun deleteTask(deleteTask: TaskModel)

    @Query("Delete From taskTable")
    fun deleteAllTask()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(tasks : List<TaskModel>)
}