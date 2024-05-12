package com.example.chmovie.data.repositories

import com.example.chmovie.data.models.ErrorResponse
import com.example.chmovie.data.models.Media
import com.example.chmovie.data.models.Series
import com.example.chmovie.data.models.SeriesResponse
import com.example.chmovie.data.models.toErrorResponse
import com.example.chmovie.data.source.SeriesDataSource
import com.example.chmovie.shared.scheduler.DataResult

class SeriesRepositoryImpl(private val seriesDataSource: SeriesDataSource.Remote) : SeriesRepository {
    /**
     * Remote
     */
    override suspend fun getSeriesDetail(seriesId: Int): DataResult<Series> {
        return try {
            val movie = seriesDataSource.getSeriesDetail(seriesId)
            DataResult.Success(movie)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    override suspend fun getAirTodaySeries(page: Int): DataResult<SeriesResponse> {
        return try {
            val response = seriesDataSource.getAirTodaySeries(page)
            DataResult.Success(response)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    override suspend fun getTopRatedSeries(page: Int): DataResult<SeriesResponse> {
        return try {
            val response = seriesDataSource.getTopRatedSeries(page)
            DataResult.Success(response)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    override suspend fun getOnTheAirSeries(page: Int): DataResult<SeriesResponse> {
        return try {
            val response = seriesDataSource.getOnTheAirSeries(page)
            DataResult.Success(response)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    override suspend fun getPopularSeries(page: Int): DataResult<SeriesResponse> {
        return try {
            val response = seriesDataSource.getPopularSeries(page)
            DataResult.Success(response)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    override suspend fun getWatchList(accountId: String, sessionId: String): DataResult<SeriesResponse> {
        return try {
            val response = seriesDataSource.getWatchList(accountId, sessionId)
            DataResult.Success(response)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    override suspend fun watchList(accountId: String, sessionId: String, media: Media): DataResult<String> {
        return try {
            val response = seriesDataSource.watchList(accountId, sessionId, media)
            if (response.isSuccessful) {
                if (response.body() != null) {
                    DataResult.Success("Update watchlist successfully")
                } else {
                    DataResult.Error(Exception("Response body is null"))
                }
            } else {
                val errorResponse: ErrorResponse? = response.errorBody()?.toErrorResponse()
                DataResult.Error(Exception(errorResponse?.message))
            }
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    override suspend fun getSeriesSearch(query: String, page: Int): DataResult<SeriesResponse> {
        return try {
            val response = seriesDataSource.getSeriesSearch(query, page)
            DataResult.Success(response)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

}