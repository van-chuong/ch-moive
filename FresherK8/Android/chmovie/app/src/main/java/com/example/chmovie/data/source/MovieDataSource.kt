package com.example.chmovie.data.source

import com.example.chmovie.data.models.Media
import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.data.models.MoviesResponse
import okhttp3.ResponseBody
import retrofit2.Response

interface MovieDataSource {

    interface Local

    interface Remote {

        suspend fun getMovieDetails(movieId: Int): MovieDetail

        suspend fun getPopularMovies(page: Int): MoviesResponse

        suspend fun getTopRated(page: Int): MoviesResponse

        suspend fun getNowPlaying(page: Int): MoviesResponse

        suspend fun getUpcoming(page: Int): MoviesResponse

        suspend fun getWatchList(accountId: String, sessionId: String): MoviesResponse

        suspend fun watchList(accountId: String, sessionId: String, media: Media): Response<ResponseBody>

        suspend fun getMovieSearch(query: String, page: Int): MoviesResponse
    }
}