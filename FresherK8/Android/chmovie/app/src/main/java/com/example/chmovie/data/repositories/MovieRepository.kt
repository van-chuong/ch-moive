package com.example.chmovie.data.repositories

import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.data.models.PopularMoviesResponse
import com.example.chmovie.data.source.remote.MovieApiClient

class MovieRepository {
    suspend fun getMovieDetails(movieId: Int): MovieDetail? {
        return try {
            MovieApiClient.instance.getMovieById(movieId)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getPopularMovies(page: Int): PopularMoviesResponse? {
        return try {
            val response = MovieApiClient.instance.getPopularMovies(page)
            response
        } catch (e: Exception) {
            null
        }
    }
}