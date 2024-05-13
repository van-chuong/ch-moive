package com.example.chmovie.data.source.remote.api

import com.example.chmovie.shared.constant.Constant
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object MovieApiClient {

    val instance: MovieApiService by lazy {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val originalHttpUrl = originalRequest.url

                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("api_key", Constant.API_KEY)
                    .build()
                val request = originalRequest.newBuilder()
                    .url(url)
                    .build()

                chain.proceed(request)
            }
            .build()
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(MovieApiService::class.java)
    }
}
