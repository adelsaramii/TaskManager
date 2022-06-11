package com.example.taskmanager.utils

import com.example.taskmanager.model.TaskModel
import com.google.gson.JsonObject

fun taskToJsonObject(task: TaskModel) :JsonObject {

    val jsonObject = JsonObject()
    jsonObject.addProperty("title", task.title)
    jsonObject.addProperty("description", task.description)
    jsonObject.addProperty("url", task.url)
    return jsonObject

}