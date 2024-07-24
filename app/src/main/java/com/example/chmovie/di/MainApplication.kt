package com.example.chmovie.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val modules = listOf(
            repositoryModule,
            viewModelModule,
            prefManagerModule,
            dataBaseModule,
            dataSourceModule
        )
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MainApplication)
            modules(modules)
        }
    }
}
