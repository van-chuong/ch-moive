package com.example.chmovie.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.chmovie.data.models.Favorite

@Database(entities = [(Favorite::class)], version = 1)
abstract class FavoriteDataBase : RoomDatabase() {
    abstract fun getFavoriteDao() : FavoriteDao
}
