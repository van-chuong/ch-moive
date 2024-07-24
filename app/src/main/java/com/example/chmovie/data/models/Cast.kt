package com.example.chmovie.data.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cast(
    @SerializedName("gender")
    val gender: Int = 0,
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("known_for_department")
    val knownForDepartment: String = "",
    @SerializedName("biography")
    val biography: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("original_name")
    val originalName: String = "",
    @SerializedName("popularity")
    val popularity: Double = 0.0,
    @SerializedName("profile_path")
    val profilePath: String = "",
    @SerializedName("movie_credits")
    val movieCredits: MoviesResponse,
    @SerializedName("tv_credits")
    val tvCredits: SeriesResponse
) : Parcelable

@Parcelize
data class CastsResponse(
    @SerializedName("cast")
    val casts: List<Cast>,
    @SerializedName("results")
    val results: List<Cast>,
) : Parcelable

fun List<Cast>.filterPersonsWithProfilePath(): MutableList<Cast> {
    return this.filter { it.profilePath != null && it.profilePath.isNotEmpty() }.toMutableList()
}

