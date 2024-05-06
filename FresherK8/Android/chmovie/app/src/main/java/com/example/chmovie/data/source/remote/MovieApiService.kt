package com.example.chmovie.data.source.remote

import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.data.models.MoviesResponse
import com.example.chmovie.data.models.RequestToken
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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

    @GET("authentication/token/new")
    suspend fun getRequestToken(): Response<RequestToken>

    @POST("authentication/token/validate_with_login")
    suspend fun validateWithLogin(@Body body: Map<String, String>): Response<RequestToken>

    @POST("authentication/session/new")
    suspend fun createSession(@Body body: Map<String, String>): Response<ResponseBody>

}