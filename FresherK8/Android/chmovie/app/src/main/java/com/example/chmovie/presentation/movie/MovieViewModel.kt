package com.example.chmovie.presentation.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.data.repositories.MovieRepository
import kotlinx.coroutines.launch

class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val _popularMovies = MutableLiveData<List<MovieDetail>>()
    val popularMovies: MutableLiveData<List<MovieDetail>> = _popularMovies

    fun loadPopularMovies(page: Int) {
        viewModelScope.launch {
            val movies = movieRepository.getPopularMovies(page)
            _popularMovies.postValue(movies?.results)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class MovieViewModelFactory(private val movieRepository: MovieRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        MovieViewModel(movieRepository) as T
}