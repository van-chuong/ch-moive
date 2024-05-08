package com.example.chmovie.di

import com.example.chmovie.data.repositories.AuthRepository
import com.example.chmovie.data.repositories.AuthRepositoryImpl
import com.example.chmovie.data.repositories.FavoriteRepository
import com.example.chmovie.data.repositories.FavoriteRepositoryImpl
import com.example.chmovie.data.repositories.MovieRepository
import com.example.chmovie.data.repositories.MovieRepositoryImpl
import org.koin.dsl.module


val repositoryModule  = module {
    single<MovieRepository> {
        MovieRepositoryImpl(get())
    }
    single<AuthRepository> {
        AuthRepositoryImpl(get())
    }
    single <FavoriteRepository> {
        FavoriteRepositoryImpl(get())
    }
}
