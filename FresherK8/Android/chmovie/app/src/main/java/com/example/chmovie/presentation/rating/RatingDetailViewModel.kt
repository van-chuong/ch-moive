package com.example.chmovie.presentation.rating

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chmovie.data.models.Rating
import com.example.chmovie.data.repositories.MovieRepository
import com.example.chmovie.data.source.local.PrefManager
import com.example.chmovie.data.source.remote.firebase.FirebaseManager.ratingRef
import com.example.chmovie.shared.base.BaseViewModel
import com.example.chmovie.shared.constant.Constant
import com.example.chmovie.shared.scheduler.DataResult

class RatingDetailViewModel(private val prefManager: PrefManager, private val movieRepository: MovieRepository) : BaseViewModel() {

    private val _id = MutableLiveData<Int>()
    val id: LiveData<Int> = _id

    private val _addRatingResult = MutableLiveData<DataResult<Boolean>>()
    val addRatingResult: LiveData<DataResult<Boolean>> = _addRatingResult

    private val accountId = prefManager.getString(Constant.USERNAME_KEY, "").toString()

    fun setId(id: Int) {
        _id.value = id
    }

    fun addRating(rating: Int, comment: String) {
        _addRatingResult.value = DataResult.Loading
        ratingRef.child(id.value.toString()).child(accountId).setValue(Rating(accountId, comment, rating.toDouble())).addOnSuccessListener {
            reviewNotification(id.value.toString(), "movie")
        }.addOnFailureListener {
            _addRatingResult.value = DataResult.Error(it)
        }
    }

    private fun reviewNotification(id: String, type: String) {

        launchTaskSync(
            onRequest = { movieRepository.reviewNotification(id, accountId, type) },
            onSuccess = { _addRatingResult.value = DataResult.Success(true) },
            onError = { _addRatingResult.value = DataResult.Error(it) }
        )
    }
}