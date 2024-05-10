package com.example.chmovie.presentation.series_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chmovie.data.models.Genre
import com.example.chmovie.data.models.Media
import com.example.chmovie.data.models.ProductionCountry
import com.example.chmovie.data.models.Series
import com.example.chmovie.data.models.Video
import com.example.chmovie.data.repositories.SeriesRepository
import com.example.chmovie.data.source.local.PrefManager
import com.example.chmovie.shared.base.BaseViewModel
import com.example.chmovie.shared.constant.Constant
import com.example.chmovie.shared.helper.formatRuntime
import com.example.chmovie.shared.helper.formatVoteAverage
import com.example.chmovie.shared.scheduler.DataResult

class SeriesDetailViewModel(
    private val seriesRepository: SeriesRepository,
    prefManager: PrefManager
) : BaseViewModel() {

    private val _seriesId = MutableLiveData<Int>()
    val seriesId: LiveData<Int> = _seriesId

    private val _seriesDetail = MutableLiveData<Series>()
    val seriesDetail: LiveData<Series> = _seriesDetail

    private val accountId = prefManager.getString(Constant.USERNAME_KEY, "")
    private val sessionId = prefManager.getString(Constant.SESSION_KEY, "")

    private val _editWatchListResult = MutableLiveData<DataResult<String>>()
    val editWatchListResult: LiveData<DataResult<String>> = _editWatchListResult

    fun setSeriesId(data: Int) {
        _seriesId.value = data
    }

    fun loadSeriesDetail(seriesId: Int) {
        launchTaskSync(
            onRequest = { seriesRepository.getSeriesDetail(seriesId) },
            onSuccess = { _seriesDetail.value = it },
            onError = { exception.value = it }
        )
    }

    fun watchList(media: Media) {
        launchTaskSync(
            onRequest = { seriesRepository.watchList(accountId.toString(), sessionId.toString(), media) },
            onSuccess = { _editWatchListResult.value = DataResult.Success(it) },
            onError = { _editWatchListResult.value = DataResult.Error(it) }
        )
    }

    fun formatSeriesRuntime(runtimes: List<Int?>?): String {
        if (runtimes.isNullOrEmpty()) {
            return "Unknown"
        }
        val sum = runtimes.filterNotNull().sum()
        return sum.formatRuntime()
    }

    fun formatVoteAverage(value: Double?): String {
        if (value == null || value == 0.0) {
            return "Unknown"
        }
        return value.formatVoteAverage()
    }

    fun formatGenres(genres: List<Genre>?): String? {
        return genres?.joinToString(separator = ", ") { it.name }
    }

    fun formatCountries(genres: List<ProductionCountry>?): String? {
        return genres?.joinToString(separator = ", ") { it.name }
    }

    fun getVideoKey(videos: List<Video>?): String? {
        return videos?.shuffled()?.firstOrNull()?.key
    }
}