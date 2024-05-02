package com.example.chmovie.data.source.remote

import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.data.models.PopularMoviesResponse
import com.example.chmovie.data.models.RequestToken
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieApiService {
    @GET("movie/{movie_id}")
    suspend fun getMovieById(@Path("movie_id") id: Int): MovieDetail

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") id: Int): PopularMoviesResponse

    @GET("authentication/token/new")
    suspend fun getRequestToken(): RequestToken

    @POST("authentication/token/validate_with_login")
    suspend fun validateWithLogin(@Body body: Map<String, String>): RequestToken

    @POST("authentication/session/new")
    suspend fun createSession(@Body body: Map<String, String>): ResponseBody

}