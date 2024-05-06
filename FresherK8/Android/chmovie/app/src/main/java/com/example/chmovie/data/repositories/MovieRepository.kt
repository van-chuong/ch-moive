package com.example.chmovie.data.repositories

import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.data.models.MoviesResponse
import com.example.chmovie.data.source.remote.MovieApiClient
import com.example.chmovie.shared.scheduler.DataResult

class MovieRepository {
    suspend fun getMovieDetails(movieId: Int): DataResult<MovieDetail> {
        return try {
            val movie = MovieApiClient.instance.getMovieById(movieId)
            DataResult.Success(movie)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun getPopularMovies(page: Int): DataResult<MoviesResponse> {
        return try {
            val response = MovieApiClient.instance.getPopularMovies(page)
            DataResult.Success(response)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun getTopRated(page: Int): DataResult<MoviesResponse> {
        return try {
            val response = MovieApiClient.instance.getTopRated(page)
            DataResult.Success(response)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun getNowPlaying(page: Int): DataResult<MoviesResponse> {
        return try {
            val response = MovieApiClient.instance.getNowPlaying(page)
            DataResult.Success(response)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun getUpcoming(page: Int): DataResult<MoviesResponse> {
        return try {
            val response = MovieApiClient.instance.getUpcoming(page)
            DataResult.Success(response)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }
}