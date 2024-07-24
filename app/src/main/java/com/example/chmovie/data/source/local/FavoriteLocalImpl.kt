package com.example.chmovie.data.source.local

import com.example.chmovie.data.models.Favorite
import com.example.chmovie.data.source.FavoriteDataSource

class FavoriteLocalImpl(private val favoriteDao: FavoriteDao) : FavoriteDataSource.Local {

    override suspend fun getFavoriteMovies(): MutableList<Favorite> = favoriteDao.getAllFavorites()
    override suspend fun saveFavoriteMovie(favorite: Favorite): Any = favoriteDao.insertFavorite(favorite)
    override suspend fun deleteFavoriteMovie(favorite: Favorite): Any = favoriteDao.deleteFavorite(favorite)
    override suspend fun deleteAllFavorites(): Any = favoriteDao.deleteAllFavorites()
}
