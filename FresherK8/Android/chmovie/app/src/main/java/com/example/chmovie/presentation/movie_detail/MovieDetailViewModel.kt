package com.example.chmovie.presentation.movie_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chmovie.data.models.Favorite
import com.example.chmovie.data.models.Genre
import com.example.chmovie.data.models.Media
import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.data.models.ProductionCountry
import com.example.chmovie.data.models.Video
import com.example.chmovie.data.repositories.FavoriteRepository
import com.example.chmovie.data.repositories.MovieRepository
import com.example.chmovie.data.source.local.PrefManager
import com.example.chmovie.shared.base.BaseViewModel
import com.example.chmovie.shared.constant.Constant.SESSION_KEY
import com.example.chmovie.shared.constant.Constant.USERNAME_KEY
import com.example.chmovie.shared.helper.formatRuntime
import com.example.chmovie.shared.scheduler.DataResult
import java.text.NumberFormat
import java.util.Locale

class MovieDetailViewModel(
    private val movieRepository: MovieRepository,
    private val favoriteRepository: FavoriteRepository,
    prefManager: PrefManager
) :
    BaseViewModel() {
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
            onError = { exception.value = it }
        )
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
        return '$' + formatter.format(value.toDouble() / 100.0)
    }

    fun getVideoKey(videos: List<Video>?): String? {
        val firstTrailer = videos?.firstOrNull { it.type == "Trailer" }
        return firstTrailer?.key
    }
}