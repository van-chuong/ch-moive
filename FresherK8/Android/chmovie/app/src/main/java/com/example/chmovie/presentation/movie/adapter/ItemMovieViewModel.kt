package com.example.chmovie.presentation.movie.adapter

import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.chmovie.BR
import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.shared.helper.OnItemClickListener

class ItemMovieViewModel(private val itemClickListener: OnItemClickListener<MovieDetail>? = null) : BaseObservable() {

    @Bindable
    var movie: MovieDetail? = null

    fun setData(data: MovieDetail?) {
        data?.let {
            movie = it
            notifyPropertyChanged(BR.movie)
        }
    }

    fun onItemClicked(view: View) {
        itemClickListener?.let { listener ->
            movie?.let {
                listener.onItemViewClick(it)
            }
        }
    }
}
