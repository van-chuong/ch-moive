package com.example.chmovie.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chmovie.data.repositories.FavoriteRepository
import com.example.chmovie.data.source.local.PrefManager
import com.example.chmovie.data.source.remote.firebase.FirebaseManager
import com.example.chmovie.shared.base.BaseViewModel
import com.example.chmovie.shared.constant.Constant
import com.example.chmovie.shared.scheduler.DataResult

class MainViewModel(private val favoriteRepository: FavoriteRepository, private val prefManager: PrefManager) : BaseViewModel() {

    private val username = prefManager.getString(Constant.USERNAME_KEY, "").toString()
    private val token = prefManager.getString(Constant.DEVICE_TOKEN, "").toString()

    private val _signOutResult = MutableLiveData<DataResult<Boolean>>()
    val signOutResult: LiveData<DataResult<Boolean>> = _signOutResult

    fun signOut() {
        _signOutResult.value = DataResult.Loading

        prefManager.clear()
        FirebaseManager.devicesRef.child(username).child(token).removeValue()

        launchTaskSync(
            onRequest = { favoriteRepository.deleteAllFavorites() },
            onSuccess = { _signOutResult.value = DataResult.Success(true) },
            onError = { DataResult.Error(it) }
        )
    }

}