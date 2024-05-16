package com.example.chmovie.presentation.series_detail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chmovie.data.models.Genre
import com.example.chmovie.data.models.Media
import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.data.models.ProductionCountry
import com.example.chmovie.data.models.Room
import com.example.chmovie.data.models.RoomResponse
import com.example.chmovie.data.models.Series
import com.example.chmovie.data.models.Video
import com.example.chmovie.data.repositories.SeriesRepository
import com.example.chmovie.data.source.local.PrefManager
import com.example.chmovie.presentation.room.start_room.StartRoomActivity
import com.example.chmovie.shared.base.BaseViewModel
import com.example.chmovie.shared.constant.Constant
import com.example.chmovie.shared.extension.next5DigitId
import com.example.chmovie.shared.helper.formatRuntime
import com.example.chmovie.shared.helper.formatVoteAverage
import com.example.chmovie.shared.scheduler.DataResult
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.random.Random

class SeriesDetailViewModel(
    private val seriesRepository: SeriesRepository,
    private val prefManager: PrefManager,
    private val realTimeDbRepository: DatabaseReference
) : BaseViewModel() {

    private val _seriesId = MutableLiveData<Int>()
    val seriesId: LiveData<Int> = _seriesId

    private val _seriesDetail = MutableLiveData<Series>()
    val seriesDetail: LiveData<Series> = _seriesDetail

    private val accountId = prefManager.getString(Constant.USERNAME_KEY, "")
    private val sessionId = prefManager.getString(Constant.SESSION_KEY, "")

    private val _editWatchListResult = MutableLiveData<DataResult<String>>()
    val editWatchListResult: LiveData<DataResult<String>> = _editWatchListResult

    private val recommendRef = realTimeDbRepository.child(Constant.RECOMMEND_REALTIME_DB)

    fun setSeriesId(data: Int) {
        _seriesId.value = data
    }

    fun loadSeriesDetail(seriesId: Int) {
        launchTaskSync(
            onRequest = { seriesRepository.getSeriesDetail(seriesId) },
            onSuccess = { _seriesDetail.value = it },
            onError = { exception.value = it }
        )
    }

    fun watchList(media: Media) {
        launchTaskSync(
            onRequest = { seriesRepository.watchList(accountId.toString(), sessionId.toString(), media) },
            onSuccess = { _editWatchListResult.value = DataResult.Success(it) },
            onError = { _editWatchListResult.value = DataResult.Error(it) }
        )
    }

    fun checkRoomCodeExist(videoKey: String, context: Context) {
        val roomCode = Random.next5DigitId().toString()
        realTimeDbRepository.child(Constant.ROOM_REALTIME_DB).child(roomCode).get().addOnSuccessListener {
            if (it.exists()) {
                checkRoomCodeExist(videoKey, context)
            } else {
                val roomResponse = RoomResponse(roomCode, Room(0f, 0, accountId.toString(), "play", videoKey))
                createRoom(roomResponse, context)
            }
        }
    }

    private fun createRoom(roomResponse: RoomResponse, context: Context) {
        realTimeDbRepository.child(Constant.ROOM_REALTIME_DB).child(roomResponse.key).setValue(roomResponse.value).addOnSuccessListener {
            StartRoomActivity.newInstance(context, roomResponse)
        }.addOnFailureListener {
            exception.value = it
        }
    }

    fun saveRecommendSeries(series: List<Series>) {
        addRecommendSeriesListener()

        recommendRef.child(accountId.toString()).child("series").get().addOnSuccessListener {
            val type = object : TypeToken<List<MovieDetail>>() {}.type
            val movieList: List<MovieDetail> = Gson().fromJson(Gson().toJson(((it.children.mapNotNull { it.value }))), type)

            series.forEach { movie ->
                val isExist = movieList.any { it.id == movie.id }
                if (!isExist) {
                    recommendRef.child(accountId.toString()).child("series").push().setValue(movie)
                }
            }
        }
    }

    private fun addRecommendSeriesListener() {
        recommendRef.child(accountId.toString()).child("series").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                recommendRef.child(accountId.toString()).child("series").get().addOnSuccessListener {
                    if (it.exists() && it.childrenCount > 20) {
                        recommendRef.child(accountId.toString()).child("series").child((it.children.first().key).toString()).removeValue()
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun formatSeriesRuntime(runtimes: List<Int?>?): String {
        if (runtimes.isNullOrEmpty()) {
            return "Unknown"
        }
        val sum = runtimes.filterNotNull().sum()
        return sum.formatRuntime()
    }

    fun formatVoteAverage(value: Double?): String {
        if (value == null || value == 0.0) {
            return "Unknown"
        }
        return value.formatVoteAverage()
    }

    fun formatGenres(genres: List<Genre>?): String? {
        return genres?.joinToString(separator = ", ") { it.name }
    }

    fun formatCountries(genres: List<ProductionCountry>?): String? {
        return genres?.joinToString(separator = ", ") { it.name }
    }

    fun getVideoKey(videos: List<Video>?): String? {
        return videos?.shuffled()?.firstOrNull()?.key
    }
}