package com.example.taskmanager.di

import android.app.Application
import android.widget.ImageView
import androidx.room.Room
import com.example.taskmanager.features.MainViewModel
import com.example.taskmanager.model.MainRepository
import com.example.taskmanager.model.MainRepositoryInterface
import com.example.taskmanager.model.local.MyDatabase
import com.example.taskmanager.model.api.ApiService
import com.example.taskmanager.model.local.TaskDao
import com.squareup.picasso.Picasso
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val myModules = module {

            single<ImageLoaderService> { PicassoLoader() }
            single { provideApi()}
            single { provideDataBase(androidApplication()) }
            single { provideDao(get()) }
            single<MainRepositoryInterface> { provideRepository(get() , get()) }

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

    private fun provideApi(): ApiService{
        return Retrofit.Builder()
            .baseUrl("http://private-app-key.ravanfix.com/app/")
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

    private fun provideDataBase(application: Application): MyDatabase {
        return Room.databaseBuilder(
            this.applicationContext,
            MyDatabase::class.java,
            "myDatabase.db"
        )
            .allowMainThreadQueries()
            .build()
    }

    private fun provideDao(dataBase: MyDatabase): TaskDao {
        return dataBase.taskDao
    }

}