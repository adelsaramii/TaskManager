package com.example.taskmanager.model.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.taskmanager.model.TaskModel

@Database(entities = [TaskModel::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {

    abstract val taskDao: TaskDao

}