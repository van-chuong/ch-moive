package com.example.chmovie.data.source.remote

import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.data.models.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieApiService {
    @GET("movie/{movie_id}")
    suspend fun getMovieById(@Path("movie_id") id: Int): MovieDetail

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") id: Int): MoviesResponse

    @GET("movie/top_rated")
    suspend fun getTopRated(@Query("page") id: Int): MoviesResponse

    @GET("movie/now_playing")
    suspend fun getNowPlaying(@Query("page") id: Int): MoviesResponse

    @GET("movie/upcoming")
    suspend fun getUpcoming(@Query("page") id: Int): MoviesResponse

}