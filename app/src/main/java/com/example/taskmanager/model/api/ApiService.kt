package com.example.taskmanager.model.api

import com.example.taskmanager.model.local.TaskModel
import com.google.gson.JsonObject
import retrofit2.http.*

interface ApiService {

    @GET("/get_questions.php")
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