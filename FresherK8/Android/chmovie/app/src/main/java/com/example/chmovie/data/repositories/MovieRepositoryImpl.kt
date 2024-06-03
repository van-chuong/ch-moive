package com.example.chmovie.data.repositories

import com.example.chmovie.data.models.ErrorResponse
import com.example.chmovie.data.models.Media
import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.data.models.MoviesResponse
import com.example.chmovie.data.models.toErrorResponse
import com.example.chmovie.data.source.MovieDataSource
import com.example.chmovie.shared.scheduler.DataResult

class MovieRepositoryImpl(private val moviesDataSource: MovieDataSource.Remote) : MovieRepository {

    override suspend fun getMovieDetails(movieId: Int): DataResult<MovieDetail> {
        return try {
            val movie = moviesDataSource.getMovieDetails(movieId)
            DataResult.Success(movie)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    override suspend fun getPopularMovies(page: Int): DataResult<MoviesResponse> {
        return try {
            val response = moviesDataSource.getPopularMovies(page)
            DataResult.Success(response)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    override suspend fun getTopRated(page: Int): DataResult<MoviesResponse> {
        return try {
            val response = moviesDataSource.getTopRated(page)
            DataResult.Success(response)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    override suspend fun getNowPlaying(page: Int): DataResult<MoviesResponse> {
        return try {
            val response = moviesDataSource.getNowPlaying(page)
            DataResult.Success(response)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    override suspend fun getUpcoming(page: Int): DataResult<MoviesResponse> {
        return try {
            val response = moviesDataSource.getUpcoming(page)
            DataResult.Success(response)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    override suspend fun getWatchList(accountId: String?, sessionId: String?): DataResult<MoviesResponse> {
        try {
            if (accountId != null && sessionId != null) {
                val response = moviesDataSource.getWatchList(accountId, sessionId)
                return DataResult.Success(response)
            }
            return DataResult.Error(Exception("Invalid parameter"))
        } catch (e: Exception) {
            return DataResult.Error(e)
        }
    }

    override suspend fun watchList(accountId: String, sessionId: String, media: Media): DataResult<String> {
        return try {
            val response = moviesDataSource.watchList(accountId, sessionId, media)
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

    override suspend fun getMovieSearch(query: String, page: Int): DataResult<MoviesResponse> {
        return try {
            val response = moviesDataSource.getMovieSearch(query, page)
            DataResult.Success(response)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    override suspend fun reviewNotification(id: String, username: String, type: String): DataResult<Boolean> {
        return try {
            moviesDataSource.reviewNotification(id, username, type)
            DataResult.Success(true)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

}