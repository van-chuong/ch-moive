package com.example.chmovie.data.repositories

import com.example.chmovie.data.models.Favorite
import com.example.chmovie.shared.scheduler.DataResult

interface FavoriteRepository {
    suspend fun getFavoriteMovies(): DataResult<MutableList<Favorite>>
    suspend fun saveFavoriteMovie(favorite: Favorite): DataResult<Any>
    suspend fun deleteFavoriteMovie(favorite: Favorite): DataResult<Any>
}