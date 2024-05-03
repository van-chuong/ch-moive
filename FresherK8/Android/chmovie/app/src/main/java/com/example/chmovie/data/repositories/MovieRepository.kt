package com.example.chmovie.data.repositories

import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.data.models.PopularMoviesResponse
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

    suspend fun getPopularMovies(page: Int): DataResult<PopularMoviesResponse> {
        return try {
            val response = MovieApiClient.instance.getPopularMovies(page)
            DataResult.Success(response)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

}