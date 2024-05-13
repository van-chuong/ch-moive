package com.example.chmovie.presentation.person_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chmovie.data.models.Cast
import com.example.chmovie.data.repositories.ProviderRepository
import com.example.chmovie.shared.base.BaseViewModel

class PersonDetailViewModel(private val providerRepository: ProviderRepository) : BaseViewModel() {

    private val _personDetail = MutableLiveData<Cast>()
    val personDetail: LiveData<Cast> = _personDetail

    fun loadPersonDetail(personId: Int) {
        launchTaskSync(
            onRequest = { providerRepository.getPersonDetail(personId) },
            onSuccess = { _personDetail.value = it },
            onError = { exception.value = it }
        )
    }
}