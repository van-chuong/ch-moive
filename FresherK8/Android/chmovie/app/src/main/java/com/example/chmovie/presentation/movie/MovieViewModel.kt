package com.example.chmovie.presentation.movie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.data.repositories.MovieRepository
import com.example.chmovie.shared.base.BaseViewModel

class MovieViewModel(private val movieRepository: MovieRepository) : BaseViewModel() {

    private val _popularMovies = MutableLiveData<MutableList<MovieDetail>>()
    val popularMovies: LiveData<MutableList<MovieDetail>> = _popularMovies

    fun loadPopularMovies(page: Int) {
        launchTaskSync(
            onRequest = { movieRepository.getPopularMovies(page) },
            onSuccess = { _popularMovies.value = it.results.toMutableList() },
            onError = { exception.value = it }
        )
    }
}

