package com.example.chmovie.data.models


import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Series(
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @SerializedName("episode_run_time")
    val episodeRunTime: List<Int>,
    @SerializedName("genres")
    val genres: List<Genre>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_name")
    val originalName: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("popularity")
    val popularity: Double,
    @SerializedName("poster_path", alternate = ["posterPath"])
    val posterPath: String,
    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompany>,
    @SerializedName("production_countries")
    val productionCountries: List<ProductionCountry>,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int,
    @SerializedName("videos")
    @Expose
    val videos: VideosResponse,
    @SerializedName("credits")
    @Expose
    val casts: CastsResponse,
    @SerializedName("similar")
    @Expose
    val similar: SeriesResponse,
) : Parcelable

@Parcelize
data class SeriesResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<Series>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("cast")
    val cast: List<Series>,
) : Parcelable

fun List<Series>.filterSeriesWithPosterPath(): MutableList<Series> {
    return this.filter { it.posterPath != null && it.posterPath.isNotEmpty() }.toMutableList()
}

fun List<Series>.randomSubList(size: Int): List<Series> {
    val subsetSize = size.coerceAtMost(this.size)
    val shuffledList = this.shuffled()
    return shuffledList.take(subsetSize)
}