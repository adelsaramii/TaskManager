package com.example.taskmanager.di

import android.app.Application
import android.widget.ImageView
import androidx.room.Room
import com.example.taskmanager.features.MainViewModel
import com.example.taskmanager.model.MainRepository
import com.example.taskmanager.model.local.MyDatabase
import com.example.taskmanager.model.api.ApiService
import com.example.taskmanager.model.api.BASE_URL
import com.example.taskmanager.model.local.TaskDao
import com.squareup.picasso.Picasso
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val myModules = module {

            single { provideRetrofit() }
            single { provideNetworkApi(get()) }
            single<ImageLoaderService> { PicassoLoader() }
            single { provideDataBase(androidApplication()) }
            single { provideDao(get()) }
            single { provideRepository(get() , get()) }

            viewModel { MainViewModel(get()) }

        }

        startKoin {
            androidContext(this@MyApp)
            modules(myModules)
        }

    }

    private fun provideRepository(apiService: ApiService, taskDao: TaskDao) : MainRepository {
        return MainRepository(apiService , taskDao)
    }

    private fun provideRetrofit(): Retrofit {

        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    private fun provideNetworkApi(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
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

    private fun provideDataBase(application: Application): MyDatabase {
        return Room.databaseBuilder(
            this.applicationContext,
            MyDatabase::class.java,
            "myDatabase.db"
        )
            .build()
    }

    private fun provideDao(dataBase: MyDatabase): TaskDao {
        return dataBase.taskDao
    }

}