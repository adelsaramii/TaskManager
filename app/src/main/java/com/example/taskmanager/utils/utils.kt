package com.example.taskmanager.utils

import com.example.taskmanager.model.local.TaskModel
import com.google.gson.JsonObject

fun taskToJsonObject(task: TaskModel) :JsonObject {

    val jsonObject = JsonObject()
    jsonObject.addProperty("title", task.title)
    jsonObject.addProperty("description", task.description)
    jsonObject.addProperty("url", task.url)
    jsonObject.addProperty("date", task.date)
    jsonObject.addProperty("time", task.time)
    jsonObject.addProperty("state", task.state)

    return jsonObject

}