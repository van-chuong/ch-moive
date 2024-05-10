package com.example.chmovie.di


import com.example.chmovie.presentation.explore.ExploreViewModel
import com.example.chmovie.presentation.login.LoginViewModel
import com.example.chmovie.presentation.movie.MovieViewModel
import com.example.chmovie.presentation.movie_detail.MovieDetailViewModel
import com.example.chmovie.presentation.my_favorite_list.MyFavoriteListViewModel
import com.example.chmovie.presentation.person_detail.PersonDetailViewModel
import com.example.chmovie.presentation.series.SeriesViewModel
import com.example.chmovie.presentation.series_detail.SeriesDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MovieViewModel(get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { MovieDetailViewModel(get(),get(),get()) }
    viewModel { SeriesViewModel(get()) }
    viewModel { SeriesDetailViewModel(get(),get()) }
    viewModel { ExploreViewModel(get()) }
    viewModel { PersonDetailViewModel(get()) }
    viewModel { MyFavoriteListViewModel(get()) }
}
