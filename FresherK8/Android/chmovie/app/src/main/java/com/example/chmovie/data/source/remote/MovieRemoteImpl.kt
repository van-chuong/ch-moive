package com.example.chmovie.data.source.remote

import com.example.chmovie.data.models.Media
import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.data.models.MoviesResponse
import com.example.chmovie.data.models.toMap
import com.example.chmovie.data.source.MovieDataSource
import com.example.chmovie.data.source.remote.api.MovieApiClient
import okhttp3.ResponseBody
import retrofit2.Response

class MovieRemoteImpl : MovieDataSource.Remote {

    override suspend fun getMovieDetails(movieId: Int): MovieDetail = MovieApiClient.instance.getMovieById(movieId)

    override suspend fun getPopularMovies(page: Int): MoviesResponse = MovieApiClient.instance.getPopularMovies(page)

    override suspend fun getTopRated(page: Int): MoviesResponse = MovieApiClient.instance.getTopRated(page)

    override suspend fun getNowPlaying(page: Int): MoviesResponse = MovieApiClient.instance.getNowPlaying(page)

    override suspend fun getUpcoming(page: Int): MoviesResponse = MovieApiClient.instance.getUpcoming(page)

    override suspend fun getWatchList(accountId: String, sessionId: String): MoviesResponse =
        MovieApiClient.instance.getWatchList(accountId, sessionId)

    override suspend fun watchList(accountId: String, sessionId: String, media: Media): Response<ResponseBody> =
        MovieApiClient.instance.watchList(accountId, sessionId, media.toMap())

    override suspend fun getMovieSearch(query: String, page: Int): MoviesResponse = MovieApiClient.instance.getMovieSearch(query, page)

}