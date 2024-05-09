package com.example.chmovie.di


import com.example.chmovie.presentation.login.LoginViewModel
import com.example.chmovie.presentation.movie.MovieViewModel
import com.example.chmovie.presentation.movie_detail.MovieDetailViewModel
import com.example.chmovie.presentation.series.SeriesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MovieViewModel(get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { MovieDetailViewModel(get(),get(),get()) }
//    viewModel { SeriesViewModel(get()) }
}
