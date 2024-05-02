package com.example.chmovie.di


import com.example.chmovie.presentation.login.LoginViewModel
import com.example.chmovie.presentation.movie.MovieViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MovieViewModel(get()) }
    viewModel { LoginViewModel(get(), get()) }
}
