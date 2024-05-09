package com.example.chmovie.data.source

import com.example.chmovie.data.models.Favorite
import com.example.chmovie.data.models.RequestToken
import com.example.chmovie.shared.scheduler.DataResult
import okhttp3.ResponseBody
import retrofit2.Response

interface FavoriteDataSource {
    interface Local {
        suspend fun getFavoriteMovies(): MutableList<Favorite>

        suspend fun saveFavoriteMovie(favorite: Favorite): Any

        suspend fun deleteFavoriteMovie(favorite: Favorite): Any
    }

    interface Remote {
    }
}