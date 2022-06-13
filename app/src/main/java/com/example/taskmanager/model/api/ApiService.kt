package com.example.taskmanager.model.api

import com.example.taskmanager.model.local.TaskModel
import com.google.gson.JsonObject
import retrofit2.http.*

interface ApiService {

    @GET("/task")
    suspend fun getAllTask(): List<TaskModel>

    @POST("/task/insert")
    suspend fun insertTask(@Body body: JsonObject)

    @PUT("/task/updating")
    suspend fun updateTask( @Body body :JsonObject )

    @DELETE("/task/deleting{id}")
    suspend fun deleteTask( @Path("id") id:Int )

    @DELETE("/task/deleteAll")
    suspend fun deleteAllTask()

}