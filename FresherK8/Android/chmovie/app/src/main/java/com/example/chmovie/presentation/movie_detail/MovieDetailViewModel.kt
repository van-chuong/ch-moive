package com.example.chmovie.presentation.movie_detail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chmovie.data.models.Favorite
import com.example.chmovie.data.models.Genre
import com.example.chmovie.data.models.Media
import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.data.models.ProductionCountry
import com.example.chmovie.data.models.Room
import com.example.chmovie.data.models.RoomResponse
import com.example.chmovie.data.models.Video
import com.example.chmovie.data.repositories.FavoriteRepository
import com.example.chmovie.data.repositories.MovieRepository
import com.example.chmovie.data.source.local.PrefManager
import com.example.chmovie.presentation.room.start_room.StartRoomActivity
import com.example.chmovie.shared.base.BaseViewModel
import com.example.chmovie.shared.constant.Constant.ROOM_REALTIME_DB
import com.example.chmovie.shared.constant.Constant.SESSION_KEY
import com.example.chmovie.shared.constant.Constant.USERNAME_KEY
import com.example.chmovie.shared.extension.next5DigitId
import com.example.chmovie.shared.helper.formatRuntime
import com.example.chmovie.shared.scheduler.DataResult
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.NumberFormat
import java.util.Locale
import kotlin.random.Random

class MovieDetailViewModel(
    private val movieRepository: MovieRepository,
    private val favoriteRepository: FavoriteRepository,
    prefManager: PrefManager,
) : BaseViewModel() {

    private val _movieId = MutableLiveData<Int?>()
    val movieId: LiveData<Int?> = _movieId

    private val _movieDetail = MutableLiveData<MovieDetail>()
    val movieDetail: LiveData<MovieDetail> = _movieDetail

    private val _favoriteMovies = MutableLiveData<MutableList<Favorite>>()
    val favoriteMovies: LiveData<MutableList<Favorite>> = _favoriteMovies

    private val accountId = prefManager.getString(USERNAME_KEY, "")
    private val sessionId = prefManager.getString(SESSION_KEY, "")

    private val _editWatchListResult = MutableLiveData<DataResult<String>>()
    val editWatchListResult: LiveData<DataResult<String>> = _editWatchListResult

    private val roomRef = realTimeDbRepository.child(ROOM_REALTIME_DB)
    private val recommendRef = realTimeDbRepository.child(RECOMMEND_REALTIME_DB)

    fun setMovieId(data: Int?) {
        _movieId.value = data
    }

    fun loadMovieDetail(movieId: Int) {
        launchTaskSync(
            onRequest = { movieRepository.getMovieDetails(movieId) },
            onSuccess = { _movieDetail.value = it },
            onError = { exception.value = it }
        )
    }

    fun getFavoriteMovies() {
        launchTaskSync(
            onRequest = { favoriteRepository.getFavoriteMovies() },
            onSuccess = { _favoriteMovies.value = it },
            onError = { exception.value = it }
        )
    }

    fun deleteFavoriteMovie(favorite: Favorite) {
        launchTaskSync(
            onRequest = { favoriteRepository.deleteFavoriteMovie(favorite) },
            onError = { exception.value = it }
        )
    }

    fun saveFavoriteMovie(favorite: Favorite) {
        launchTaskSync(
            onRequest = { favoriteRepository.saveFavoriteMovie(favorite) },
            onError = { exception.value = it }
        )
    }

    fun watchList(media: Media) {
        launchTaskSync(
            onRequest = { movieRepository.watchList(accountId.toString(), sessionId.toString(), media) },
            onSuccess = { _editWatchListResult.value = DataResult.Success(it) },
            onError = { _editWatchListResult.value = DataResult.Error(it) }
        )
    }


    fun checkRoomCodeExist(videoKey: String, context: Context) {
        val roomCode = Random.next5DigitId().toString()
        roomRef.child(roomCode).get().addOnSuccessListener {
            if (it.exists()) {
                checkRoomCodeExist(videoKey, context)
            } else {
                val roomResponse = RoomResponse(roomCode, Room(0f, 0, accountId.toString(), "play", videoKey))
                createRoom(roomResponse, context)
            }
        }
    }

    private fun createRoom(roomResponse: RoomResponse, context: Context) {
        roomRef.child(roomResponse.key).setValue(roomResponse.value).addOnSuccessListener {
            StartRoomActivity.newInstance(context, roomResponse)
        }.addOnFailureListener {
            exception.value = it
        }
    }

    fun saveRecommendMovie(movies: List<MovieDetail>) {
        addRecommendMovieListener()

        recommendRef.child(accountId.toString()).child("movies").get().addOnSuccessListener {
            val type = object : TypeToken<List<MovieDetail>>() {}.type
            val movieList: List<MovieDetail> = Gson().fromJson(Gson().toJson(((it.children.mapNotNull { it.value }))), type)

            movies.forEach { movie ->
                val isExist = movieList.any { it.id == movie.id }
                if (!isExist) {
                    recommendRef.child(accountId.toString()).child("movies").push().setValue(movie)
                }
            }
        }
    }

    private fun addRecommendMovieListener() {
        recommendRef.child(accountId.toString()).child("movies").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                recommendRef.child(accountId.toString()).child("movies").get().addOnSuccessListener {
                    if (it.exists() && it.childrenCount > 20) {
                        recommendRef.child(accountId.toString()).child("movies").child((it.children.first().key).toString()).removeValue()
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

    fun formatMovieRuntime(runtime: Int): String {
        return runtime.formatRuntime()
    }

    fun formatGenres(genres: List<Genre>?): String? {
        return genres?.joinToString(separator = ", ") { it.name }
    }

    fun formatCountries(genres: List<ProductionCountry>?): String? {
        return genres?.joinToString(separator = ", ") { it.name }
    }

    fun formatMoney(value: Int): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale.US)
        return formatter.format(value.toDouble() / 100.0)
    }

    fun getVideoKey(videos: List<Video>?): String? {
        val firstTrailer = videos?.firstOrNull { it.type == "Trailer" }
        return firstTrailer?.key
    }
}