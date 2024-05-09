package com.example.chmovie.data.source

import com.example.chmovie.data.models.Favorite

interface FavoriteDataSource {
    interface Local {
        suspend fun getFavoriteMovies(): MutableList<Favorite>

        suspend fun saveFavoriteMovie(favorite: Favorite): Any

        suspend fun deleteFavoriteMovie(favorite: Favorite): Any
    }

    interface Remote {
    }
}