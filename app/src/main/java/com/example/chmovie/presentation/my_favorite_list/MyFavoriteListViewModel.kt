package com.example.chmovie.presentation.my_favorite_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chmovie.data.models.Favorite
import com.example.chmovie.data.repositories.FavoriteRepository
import com.example.chmovie.shared.base.BaseViewModel

class MyFavoriteListViewModel(private val favoriteRepository: FavoriteRepository) : BaseViewModel() {

    private val _favoriteList = MutableLiveData<MutableList<Favorite>>()
    val favoriteList: LiveData<MutableList<Favorite>> = _favoriteList

    fun getFavoriteList() {
        launchTaskSync(
            onRequest = { favoriteRepository.getFavoriteMovies() },
            onSuccess = { _favoriteList.value = it },
            onError = { exception.value = it }
        )
    }

    fun deleteFavoriteMovie(favorite: Favorite) {
        launchTaskSync(
            onRequest = { favoriteRepository.deleteFavoriteMovie(favorite) },
            onError = { exception.value = it }
        )
    }

}