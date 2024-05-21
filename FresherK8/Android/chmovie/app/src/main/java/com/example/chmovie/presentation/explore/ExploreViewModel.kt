package com.example.chmovie.presentation.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chmovie.data.models.Cast
import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.data.models.MovieProvider
import com.example.chmovie.data.models.Series
import com.example.chmovie.data.models.filterPersonsWithProfilePath
import com.example.chmovie.data.repositories.ProviderRepository
import com.example.chmovie.data.source.local.PrefManager
import com.example.chmovie.data.source.remote.firebase.FirebaseManager.recommendRef
import com.example.chmovie.shared.base.BaseViewModel
import com.example.chmovie.shared.constant.Constant
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ExploreViewModel(
    private val providerRepository: ProviderRepository,
    private val prefManager: PrefManager,
) : BaseViewModel() {

    private val _popularProvider = MutableLiveData<MutableList<MovieProvider>>()
    val popularProvider: LiveData<MutableList<MovieProvider>> = _popularProvider

    private val _popularPerson = MutableLiveData<MutableList<Cast>>()
    val popularPerson: LiveData<MutableList<Cast>> = _popularPerson

    private val _recommendMovies = MutableLiveData<MutableList<MovieDetail>>()
    val recommendMovies: LiveData<MutableList<MovieDetail>> = _recommendMovies

    private val _recommendSeries = MutableLiveData<MutableList<Series>>()
    val recommendSeries: LiveData<MutableList<Series>> = _recommendSeries

    private val accountId = prefManager.getString(Constant.USERNAME_KEY, "")

    private val moviesRef = recommendRef.child(accountId.toString()).child("movies")
    private val seriesRef = recommendRef.child(Constant.RECOMMEND_REALTIME_DB).child(accountId.toString()).child("series")

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
            onSuccess = { _popularPerson.value = it.results.filterPersonsWithProfilePath() },
            onError = { exception.value = it }
        )
    }

    fun loadRecommendMovies() {
        isLoading.postValue(true)

        moviesRef.get().addOnSuccessListener {
            val type = object : TypeToken<List<MovieDetail>>() {}.type
            val movieList: List<MovieDetail> = Gson().fromJson(Gson().toJson(((it.children.mapNotNull { it.value }))), type)
            _recommendMovies.value = movieList.toMutableList()
        }

        isLoading.postValue(false)
    }

    fun loadRecommendSeries() {
        isLoading.postValue(true)

        seriesRef.get().addOnSuccessListener {
            val type = object : TypeToken<List<Series>>() {}.type
            val seriesList: List<Series> = Gson().fromJson(Gson().toJson(((it.children.mapNotNull { it.value }))), type)
            _recommendSeries.value = seriesList.toMutableList()
        }

        isLoading.postValue(false)
    }
}