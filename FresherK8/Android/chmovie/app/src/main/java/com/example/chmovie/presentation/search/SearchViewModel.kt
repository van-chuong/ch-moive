package com.example.chmovie.presentation.search

import androidx.lifecycle.MutableLiveData
import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.data.models.Series
import com.example.chmovie.data.models.filterMoviesWithPosterPath
import com.example.chmovie.data.models.filterSeriesWithPosterPath
import com.example.chmovie.data.repositories.MovieRepository
import com.example.chmovie.data.repositories.SeriesRepository
import com.example.chmovie.shared.base.BaseViewModel

class SearchViewModel(
    private val movieRepository: MovieRepository,
    private val seriesRepository: SeriesRepository,
) : BaseViewModel() {

    private val _query = MutableLiveData<String>()
    val query: MutableLiveData<String> = _query

    private val _movies = MutableLiveData<MutableList<MovieDetail>>()
    val movies: MutableLiveData<MutableList<MovieDetail>> = _movies

    private val _series = MutableLiveData<MutableList<Series>>()
    val series: MutableLiveData<MutableList<Series>> = _series

    fun searchMovies(query: String, page: Int) {
        launchTaskSync(
            onRequest = { movieRepository.getMovieSearch(query, page) },
            onSuccess = { _movies.value = it.results.filterMoviesWithPosterPath() },
            onError = { exception.value = it }
        )
    }

    fun searchSeries(query: String, page: Int) {
        launchTaskSync(
            onRequest = { seriesRepository.getSeriesSearch(query, page) },
            onSuccess = { _series.value = it.results.filterSeriesWithPosterPath() },
            onError = { exception.value = it }
        )
    }
}
