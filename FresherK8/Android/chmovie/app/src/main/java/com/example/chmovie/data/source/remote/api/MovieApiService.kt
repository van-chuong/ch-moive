package com.example.chmovie.data.source.remote.api

import com.example.chmovie.data.models.Cast
import com.example.chmovie.data.models.CastsResponse
import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.data.models.MoviesProviderResponse
import com.example.chmovie.data.models.MoviesResponse
import com.example.chmovie.data.models.RequestToken
import com.example.chmovie.data.models.Series
import com.example.chmovie.data.models.SeriesResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieApiService {
    /*Movie*/
    @GET("movie/{movie_id}")
    suspend fun getMovieById(
        @Path("movie_id") id: Int,
        @Query("append_to_response") appendToResponse: String = "videos,credits,similar"
    ): MovieDetail

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") id: Int): MoviesResponse

    @GET("movie/top_rated")
    suspend fun getTopRated(@Query("page") id: Int): MoviesResponse

    @GET("movie/now_playing")
    suspend fun getNowPlaying(@Query("page") id: Int): MoviesResponse

    @GET("movie/upcoming")
    suspend fun getUpcoming(@Query("page") id: Int): MoviesResponse

    /*Authen*/
    @GET("authentication/token/new")
    suspend fun getRequestToken(): Response<RequestToken>

    @POST("authentication/token/validate_with_login")
    suspend fun validateWithLogin(@Body body: Map<String, String>): Response<RequestToken>

    @POST("authentication/session/new")
    suspend fun createSession(@Body body: Map<String, String>): Response<ResponseBody>

    /*Account*/
    @GET("account/{account_id}/watchlist/movies")
    suspend fun getWatchList(@Path("account_id") accountId: String, @Query("session_id") sessionId: String): MoviesResponse

    @POST("account/{account_id}/watchlist")
    suspend fun watchList(
        @Path("account_id") accountId: String,
        @Query("session_id") sessionId: String,
        @Body body: Map<String, String>
    ): Response<ResponseBody>

    /*Series*/
    @GET("tv/{series_id}")
    suspend fun getSeriesDetail(
        @Path("series_id") id: Int,
        @Query("append_to_response") appendToResponse: String = "videos,credits,similar"
    ): Series

    @GET("tv/airing_today")
    suspend fun getAirTodaySeries(@Query("page") id: Int): SeriesResponse

    @GET("tv/top_rated")
    suspend fun getTopRatedSeries(@Query("page") id: Int): SeriesResponse

    @GET("tv/on_the_air")
    suspend fun getOnTheAirSeries(@Query("page") id: Int): SeriesResponse

    @GET("tv/popular")
    suspend fun getPopularSeries(@Query("page") id: Int): SeriesResponse

    @GET("account/{account_id}/watchlist/tv")
    suspend fun getSeriesWatchList(@Path("account_id") accountId: String, @Query("session_id") sessionId: String): SeriesResponse

    /*Provider*/
    @GET("watch/providers/movie")
    suspend fun getPopularProvider(): MoviesProviderResponse

    @GET("person/popular")
    suspend fun getPopularPerson(): CastsResponse

    @GET("person/{person_id}")
    suspend fun getPersonById(
        @Path("person_id") personId: Int,
        @Query("append_to_response") appendToResponse: String = "movie_credits,tv_credits"
    ): Cast
}