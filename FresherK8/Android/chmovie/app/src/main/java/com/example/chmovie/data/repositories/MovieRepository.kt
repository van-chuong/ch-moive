package com.example.chmovie.data.repositories

import com.example.chmovie.data.models.ErrorResponse
import com.example.chmovie.data.models.Media
import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.data.models.MoviesResponse
import com.example.chmovie.data.models.toErrorResponse
import com.example.chmovie.data.models.toMap
import com.example.chmovie.data.source.remote.api.MovieApiClient
import com.example.chmovie.shared.scheduler.DataResult

interface MovieRepository {
    /**
     * Remote
     */
    suspend fun getMovieDetails(movieId: Int): DataResult<MovieDetail>

    suspend fun getPopularMovies(page: Int): DataResult<MoviesResponse>

    suspend fun getTopRated(page: Int): DataResult<MoviesResponse>

    suspend fun getNowPlaying(page: Int): DataResult<MoviesResponse>

    suspend fun getUpcoming(page: Int): DataResult<MoviesResponse>

    suspend fun getWatchList(accountId: String?, sessionId: String?): DataResult<MoviesResponse>

    suspend fun watchList(accountId: String, sessionId: String, media: Media): DataResult<String>

    suspend fun getMovieSearch(query: String, page: Int): DataResult<MoviesResponse>

}