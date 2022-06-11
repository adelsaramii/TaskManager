package com.example.taskmanager.di

import android.app.Application
import android.widget.ImageView
import com.example.taskmanager.features.MainViewModel
import com.example.taskmanager.model.ApiService
import com.example.taskmanager.model.BASE_URL
import com.squareup.picasso.Picasso
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

            viewModel { MainViewModel(get()) }

        }

        startKoin {
            androidContext(this@MyApp)
            modules(myModules)
        }

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
}