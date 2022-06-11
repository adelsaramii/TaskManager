package com.example.taskmanager.model.api

import com.example.taskmanager.model.local.TaskModel
import com.google.gson.JsonObject
import retrofit2.http.*

const val BASE_URL = "http://192.168.1.4:8080"

interface ApiService {

    @GET("/task")
    suspend fun getAllTask(): List<TaskModel>

    @POST("/task")
    suspend fun insertTask(@Body body: JsonObject)

    @PUT("/task/updating{name}")
    suspend fun updateTask( @Path("name") id:Int , @Body body :JsonObject )

    @DELETE("/task/deleting{name}")
    suspend fun deleteTask( @Path("name") id:Int )

    @DELETE("/task/deleteAll")
    suspend fun  deleteAllTask()

}