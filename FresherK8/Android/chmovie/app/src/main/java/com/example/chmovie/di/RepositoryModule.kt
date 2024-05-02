package com.example.chmovie.di

import com.example.chmovie.data.repositories.MovieRepository
import org.koin.dsl.module


val repositoryModule  = module {
    single {
        MovieRepository()
    }
}
