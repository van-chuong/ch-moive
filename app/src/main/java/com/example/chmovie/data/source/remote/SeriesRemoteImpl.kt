package com.example.chmovie.data.source.remote

import com.example.chmovie.data.models.Media
import com.example.chmovie.data.models.Series
import com.example.chmovie.data.models.SeriesResponse
import com.example.chmovie.data.models.toMap
import com.example.chmovie.data.source.SeriesDataSource
import com.example.chmovie.data.source.remote.api.MovieApiClient
import okhttp3.ResponseBody
import retrofit2.Response

class SeriesRemoteImpl : SeriesDataSource.Remote {

    override suspend fun getSeriesDetail(seriesId: Int): Series = MovieApiClient.instance.getSeriesDetail(seriesId)

    override suspend fun getAirTodaySeries(page: Int): SeriesResponse = MovieApiClient.instance.getAirTodaySeries(page)

    override suspend fun getTopRatedSeries(page: Int): SeriesResponse = MovieApiClient.instance.getTopRatedSeries(page)

    override suspend fun getOnTheAirSeries(page: Int): SeriesResponse = MovieApiClient.instance.getOnTheAirSeries(page)

    override suspend fun getPopularSeries(page: Int): SeriesResponse = MovieApiClient.instance.getPopularSeries(page)

    override suspend fun getWatchList(accountId: String, sessionId: String): SeriesResponse =
        MovieApiClient.instance.getSeriesWatchList(accountId, sessionId)

    override suspend fun watchList(accountId: String, sessionId: String, media: Media): Response<ResponseBody> =
        MovieApiClient.instance.watchList(accountId, sessionId, media.toMap())

    override suspend fun getSeriesSearch(query: String, page: Int): SeriesResponse = MovieApiClient.instance.getSeriesSearch(query, page)

}