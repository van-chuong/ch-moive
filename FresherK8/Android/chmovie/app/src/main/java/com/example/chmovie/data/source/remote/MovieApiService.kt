package com.example.chmovie.data.source.remote

import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.data.models.PopularMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieApiService {
    @GET("movie/{movie_id}")
    suspend fun getMovieById(@Path("movie_id") id: Int): MovieDetail

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") id: Int): PopularMoviesResponse

}