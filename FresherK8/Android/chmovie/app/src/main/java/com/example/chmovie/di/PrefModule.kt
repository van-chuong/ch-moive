package com.example.chmovie.di

import com.example.chmovie.data.source.local.PrefManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

val prefManagerModule: Module = module {
    single { PrefManager.with(androidContext()) }
}
