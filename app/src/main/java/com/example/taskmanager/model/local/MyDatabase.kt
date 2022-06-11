package com.example.taskmanager.model.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TaskModel::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {

    abstract val taskDao: TaskDao

}