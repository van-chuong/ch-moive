package com.example.chmovie.presentation.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chmovie.data.models.Cast
import com.example.chmovie.data.models.MovieProvider
import com.example.chmovie.data.repositories.ProviderRepository
import com.example.chmovie.shared.base.BaseViewModel

class ExploreViewModel(private val providerRepository: ProviderRepository) : BaseViewModel() {
    private val _popularProvider = MutableLiveData<MutableList<MovieProvider>>()
    val popularProvider: LiveData<MutableList<MovieProvider>> = _popularProvider
    private val _popularPerson = MutableLiveData<MutableList<Cast>>()
    val popularPerson: LiveData<MutableList<Cast>> = _popularPerson
    fun loadPopularProvider() {
        launchTaskSync(
            onRequest = { providerRepository.getPopularProvider() },
            onSuccess = { _popularProvider.value = it.results.toMutableList() },
            onError = { exception.value = it }
        )
    }

    fun loadPopularPerson() {
        launchTaskSync(
            onRequest = { providerRepository.getPopularPerson() },
            onSuccess = { _popularPerson.value = it.results.toMutableList() },
            onError = { exception.value = it }
        )
    }
}