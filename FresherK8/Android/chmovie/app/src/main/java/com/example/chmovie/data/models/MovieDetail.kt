package com.example.chmovie.data.models


import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieDetail(
    @SerializedName("backdrop_path")
    @Expose
    val backdropPath: String = "",
    @SerializedName("budget")
    @Expose
    val budget: Int = 0,
    @SerializedName("genres")
    @Expose
    val genres: List<Genre>,
    @SerializedName("id")
    @Expose
    val id: Int = 0,
    @SerializedName("imdb_id")
    @Expose
    val imdbId: String = "",
    @SerializedName("original_language")
    @Expose
    val originalLanguage: String,
    @SerializedName("original_title")
    @Expose
    val originalTitle: String = "",
    @SerializedName("overview")
    @Expose
    val overview: String = "",
    @SerializedName("popularity")
    @Expose
    val popularity: Double = 0.0,
    @SerializedName("poster_path")
    @Expose
    val posterPath: String = "",
    @SerializedName("production_companies")
    @Expose
    val productionCompanies: List<ProductionCompany>,
    @SerializedName("production_countries")
    @Expose
    val productionCountries: List<ProductionCountry>,
    @SerializedName("release_date")
    @Expose
    val releaseDate: String = "",
    @SerializedName("revenue")
    @Expose
    val revenue: Int = 0,
    @SerializedName("runtime")
    @Expose
    val runtime: Int = 0,
    @SerializedName("status")
    @Expose
    val status: String = "",
    @SerializedName("tagline")
    @Expose
    val tagline: String = "",
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("video")
    @Expose
    val video: Boolean,
    @SerializedName("vote_average")
    @Expose
    val voteAverage: Double = 0.0,
    @SerializedName("vote_count")
    @Expose
    val voteCount: Int = 0,
    @SerializedName("videos")
    @Expose
    val videos: VideosResponse,
    @SerializedName("credits")
    @Expose
    val casts: CastsResponse,
    @SerializedName("similar")
    @Expose
    val similar: MoviesResponse,
) : Parcelable

@Parcelize
data class MoviesResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<MovieDetail>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("cast")
    val cast: List<MovieDetail>,
) : Parcelable