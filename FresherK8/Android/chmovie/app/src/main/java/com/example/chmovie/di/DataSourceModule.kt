package com.example.chmovie.di

import com.example.chmovie.data.source.AuthDataSource
import com.example.chmovie.data.source.FavoriteDataSource
import com.example.chmovie.data.source.MovieDataSource
import com.example.chmovie.data.source.SeriesDataSource
import com.example.chmovie.data.source.local.FavoriteLocalImpl
import com.example.chmovie.data.source.remote.AuthRemoteImpl
import com.example.chmovie.data.source.remote.MovieRemoteImpl
import com.example.chmovie.data.source.remote.SeriesRemoteImpl
import org.koin.dsl.module

val dataSourceModule  = module {
    single<MovieDataSource.Remote> {
        MovieRemoteImpl()
    }
    single<AuthDataSource.Remote> {
        AuthRemoteImpl()
    }
    single<FavoriteDataSource.Local> {
        FavoriteLocalImpl(get())
    }
    single<SeriesDataSource.Remote> {
        SeriesRemoteImpl()
    }
}