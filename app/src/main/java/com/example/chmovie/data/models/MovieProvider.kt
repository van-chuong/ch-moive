package com.example.chmovie.data.models


import com.google.gson.annotations.SerializedName

data class MovieProvider(
    @SerializedName("logo_path")
    val logoPath: String,
    @SerializedName("provider_id")
    val providerId: Int,
    @SerializedName("provider_name")
    val providerName: String
)

data class MoviesProviderResponse(
    @SerializedName("results")
    val results: List<MovieProvider>,
)