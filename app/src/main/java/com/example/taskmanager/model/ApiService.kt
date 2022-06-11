package com.example.taskmanager.model

import retrofit2.Call
import retrofit2.http.GET

const val BASE_URL = "http://192.168.1.4:8080"

interface ApiService {

    @GET("/student")
    fun getAllStudents() : Call<List<Student>>

}