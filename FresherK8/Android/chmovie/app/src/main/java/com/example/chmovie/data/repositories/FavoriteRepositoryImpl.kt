package com.example.chmovie.data.repositories

import com.example.chmovie.data.models.Favorite
import com.example.chmovie.data.source.FavoriteDataSource
import com.example.chmovie.shared.scheduler.DataResult

class FavoriteRepositoryImpl(private val favoriteDataSource: FavoriteDataSource.Local) : FavoriteRepository {

    override suspend fun getFavoriteMovies(): DataResult<MutableList<Favorite>> {
        return try {
            val response = favoriteDataSource.getFavoriteMovies()
            DataResult.Success(response)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    override suspend fun saveFavoriteMovie(favorite: Favorite): DataResult<Any> {
        return try {
            val response = favoriteDataSource.saveFavoriteMovie(favorite)
            DataResult.Success(response)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    override suspend fun deleteFavoriteMovie(favorite: Favorite): DataResult<Any> {
        return try {
            val response = favoriteDataSource.deleteFavoriteMovie(favorite)
            DataResult.Success(response)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    override suspend fun deleteAllFavorites(): DataResult<Any> {
        return try {
            val response = favoriteDataSource.deleteAllFavorites()
            DataResult.Success(response)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

}