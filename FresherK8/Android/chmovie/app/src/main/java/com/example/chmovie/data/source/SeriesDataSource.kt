package com.example.chmovie.data.source

import com.example.chmovie.data.models.Media
import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.data.models.MoviesResponse
import com.example.chmovie.data.models.Series
import com.example.chmovie.data.models.SeriesResponse
import okhttp3.ResponseBody
import retrofit2.Response

interface SeriesDataSource {
    interface Local {
    }

    interface Remote {

        suspend fun getSeriesDetail(seriesId: Int): Series

        suspend fun getAirTodaySeries(page: Int): SeriesResponse

        suspend fun getTopRatedSeries(page: Int): SeriesResponse

        suspend fun getOnTheAirSeries(page: Int): SeriesResponse

        suspend fun getPopularSeries(page: Int): SeriesResponse

        suspend fun getWatchList(accountId: String, sessionId: String): SeriesResponse

        suspend fun watchList(accountId: String, sessionId: String, media: Media): Response<ResponseBody>
    }
}