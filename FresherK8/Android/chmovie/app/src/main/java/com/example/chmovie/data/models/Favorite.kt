package com.example.chmovie.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class Favorite(
    @PrimaryKey
    val id: Int = 0,
    val title: String = "",
    val overview: String = "",
    val posterPath: String = "",
    val voteAverage: Double = 0.0,
    val mediaType: String,
) {
    companion object {
        fun <T> of(currentItem: T): Favorite {
            val mediaType = when (currentItem) {
                is MovieDetail -> "movie"
                is Series -> "tv"
                else -> throw IllegalArgumentException("Unknown type")
            }

            return when (currentItem) {
                is MovieDetail -> Favorite(
                    currentItem.id,
                    currentItem.title,
                    currentItem.overview,
                    currentItem.posterPath,
                    currentItem.voteAverage,
                    mediaType
                )

                is Series -> Favorite(
                    currentItem.id,
                    currentItem.name,
                    currentItem.overview,
                    currentItem.posterPath,
                    currentItem.voteAverage,
                    mediaType
                )

                else -> throw IllegalArgumentException("Unknown type")
            }
        }
    }
}
