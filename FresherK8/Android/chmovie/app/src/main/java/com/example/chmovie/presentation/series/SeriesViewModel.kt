package com.example.chmovie.presentation.series

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chmovie.data.models.Series
import com.example.chmovie.data.repositories.SeriesRepository
import com.example.chmovie.shared.base.BaseViewModel

class SeriesViewModel(private val seriesRepository: SeriesRepository) : BaseViewModel() {
    private val _trendingSeries = MutableLiveData<MutableList<Series>>()
    val trendingSeries: LiveData<MutableList<Series>> = _trendingSeries

    private val _airTodaySeries = MutableLiveData<MutableList<Series>>()
    val airTodaySeries: LiveData<MutableList<Series>> = _airTodaySeries

    private val _topRatedSeries = MutableLiveData<MutableList<Series>>()
    val topRatedSeries: LiveData<MutableList<Series>> = _topRatedSeries

    private val _onTheAirSeries = MutableLiveData<MutableList<Series>>()
    val onTheAirSeries: LiveData<MutableList<Series>> = _onTheAirSeries

    fun loadTrendingSeries(page: Int) {
        launchTaskSync(
            onRequest = { seriesRepository.getPopularSeries(page) },
            onSuccess = { _trendingSeries.value = it.results.toMutableList() },
            onError = { exception.value = it }
        )
    }

    fun loadAirTodaySeries(page: Int) {
        launchTaskSync(
            onRequest = { seriesRepository.getAirTodaySeries(page) },
            onSuccess = { _airTodaySeries.value = it.results.toMutableList() },
            onError = { exception.value = it }
        )
    }

    fun loadTopRatedSeries(page: Int) {
        launchTaskSync(
            onRequest = { seriesRepository.getTopRatedSeries(page) },
            onSuccess = { _topRatedSeries.value = it.results.toMutableList() },
            onError = { exception.value = it }
        )
    }

    fun loadOnTheAirSeries(page: Int) {
        launchTaskSync(
            onRequest = { seriesRepository.getOnTheAirSeries(page) },
            onSuccess = { _onTheAirSeries.value = it.results.toMutableList() },
            onError = { exception.value = it }
        )
    }
}