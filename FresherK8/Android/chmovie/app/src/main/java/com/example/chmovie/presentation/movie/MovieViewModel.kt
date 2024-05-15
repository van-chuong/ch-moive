package com.example.chmovie.presentation.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.data.models.filterMoviesWithPosterPath
import com.example.chmovie.data.repositories.MovieRepository
import com.example.chmovie.shared.base.BaseViewModel

class MovieViewModel(private val movieRepository: MovieRepository) : BaseViewModel() {

    private val _trendingMovies = MutableLiveData<MutableList<MovieDetail>>()
    val trendingMovies: LiveData<MutableList<MovieDetail>> = _trendingMovies

    private val _topRatedMovies = MutableLiveData<MutableList<MovieDetail>>()
    val topRatedMovies: LiveData<MutableList<MovieDetail>> = _topRatedMovies

    private val _inTheaterMovies = MutableLiveData<MutableList<MovieDetail>>()
    val inTheaterMovies: LiveData<MutableList<MovieDetail>> = _inTheaterMovies

    private val _comingSoonMovies = MutableLiveData<MutableList<MovieDetail>>()
    val comingSoonMovies: LiveData<MutableList<MovieDetail>> = _comingSoonMovies

    fun loadTrendingMovies(page: Int) {
        launchTaskSync(
            onRequest = { movieRepository.getPopularMovies(page) },
            onSuccess = { _trendingMovies.value = it.results.filterMoviesWithPosterPath() },
            onError = { exception.value = it }
        )
    }

    fun loadPopularMovies(page: Int) {
        launchTaskSync(
            onRequest = { movieRepository.getTopRated(page) },
            onSuccess = { _topRatedMovies.value = it.results.filterMoviesWithPosterPath()  },
            onError = { exception.value = it }
        )
    }

    fun loadInTheaterMovies(page: Int) {
        launchTaskSync(
            onRequest = { movieRepository.getNowPlaying(page) },
            onSuccess = { _inTheaterMovies.value = it.results.filterMoviesWithPosterPath()  },
            onError = { exception.value = it }
        )
    }

    fun loadComingSoonMovies(page: Int) {
        launchTaskSync(
            onRequest = { movieRepository.getUpcoming(page) },
            onSuccess = { _comingSoonMovies.value = it.results.filterMoviesWithPosterPath()  },
            onError = { exception.value = it }
        )
    }
}

