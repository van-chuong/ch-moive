package com.example.chmovie.data.repositories

import com.example.chmovie.data.models.Media
import com.example.chmovie.data.models.Series
import com.example.chmovie.data.models.SeriesResponse
import com.example.chmovie.shared.scheduler.DataResult

interface SeriesRepository {
    /**
     * Remote
     */
    suspend fun getSeriesDetail(seriesId: Int): DataResult<Series>

    suspend fun getAirTodaySeries(page: Int): DataResult<SeriesResponse>

    suspend fun getTopRatedSeries(page: Int): DataResult<SeriesResponse>

    suspend fun getOnTheAirSeries(page: Int): DataResult<SeriesResponse>

    suspend fun getPopularSeries(page: Int): DataResult<SeriesResponse>

    suspend fun getWatchList(accountId: String, sessionId: String): DataResult<SeriesResponse>

    suspend fun watchList(accountId: String, sessionId: String, media: Media): DataResult<String>

    suspend fun getSeriesSearch(query: String, page: Int): DataResult<SeriesResponse>

}