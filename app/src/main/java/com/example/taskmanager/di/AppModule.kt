package com.example.taskmanager.di

import android.app.Application
import android.widget.ImageView
import androidx.room.Room
import com.example.taskmanager.model.MainRepository
import com.example.taskmanager.model.api.ApiService
import com.example.taskmanager.model.local.MyDatabase
import com.example.taskmanager.model.local.TaskDao
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class AppModule :Application() {

    fun provideRepository(apiService: ApiService, taskDao: TaskDao) : MainRepository {
        return MainRepository(apiService , taskDao)
    }

    fun provideApi(): ApiService {
        return Retrofit.Builder()
            .baseUrl("http://192.168.222.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    interface ImageLoaderService {
        fun loadImage(url: String, imageView: ImageView)
    }

    class PicassoLoader : ImageLoaderService {

        override fun loadImage(url: String, imageView: ImageView) {

            Picasso
                .get()
                .load(url)
                .into(imageView)

        }
    }

    fun provideDataBase(): MyDatabase {
        return Room.databaseBuilder(
            this.applicationContext,
            MyDatabase::class.java,
            "myDatabase.db"
        )
            .allowMainThreadQueries()
            .build()
    }

    fun provideDao(dataBase: MyDatabase): TaskDao {
        return dataBase.taskDao
    }

}