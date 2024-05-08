package com.example.chmovie.shared.extension

import android.os.Bundle
import androidx.navigation.NavController
import com.example.chmovie.R
import com.example.chmovie.presentation.movie_detail.MovieDetailFragment

fun NavController.navigateToMovieDetail(movieId: Int, shouldPopCurrent: Boolean = false) {
    if (shouldPopCurrent) {
        popBackStack()
    }

    val bundle = Bundle().apply {
        putInt(MovieDetailFragment.ARGUMENT_MOVIE, movieId)
    }
    navigate(R.id.nav_movie_detail, bundle)
}