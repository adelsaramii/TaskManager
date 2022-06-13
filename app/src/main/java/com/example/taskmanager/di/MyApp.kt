package com.example.taskmanager.di

import com.example.taskmanager.features.MainViewModel
import com.example.taskmanager.model.MainRepositoryInterface
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class MyApp : AppModule() {

    override fun onCreate() {
        super.onCreate()

        val myModules = module {

            single<ImageLoaderService> { PicassoLoader() }
            single { provideApi()}
            single { provideDataBase() }
            single { provideDao(get()) }
            single<MainRepositoryInterface> { provideRepository(get() , get()) }

            viewModel { MainViewModel(get()) }

        }

        startKoin {
            androidContext(this@MyApp)
            modules(myModules)
        }

    }

}