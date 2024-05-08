package com.example.chmovie.presentation.movie_detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chmovie.R
import com.example.chmovie.data.models.Cast
import com.example.chmovie.data.models.Favorite
import com.example.chmovie.data.models.Media
import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.databinding.FragmentMovieDetailBinding
import com.example.chmovie.presentation.movie_detail.adapter.CastsAdapter
import com.example.chmovie.presentation.movie_detail.adapter.SimilarMoviesAdapter
import com.example.chmovie.presentation.watch_video.WatchVideoActivity
import com.example.chmovie.presentation.watch_video.WatchVideoActivity.Companion.ARGUMENT_WATCH_VIDEO
import com.example.chmovie.shared.extension.navigateToMovieDetail
import com.example.chmovie.shared.scheduler.DataResult
import com.example.chmovie.shared.widget.CustomSnackbar
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class MovieDetailFragment : Fragment() {

    companion object {
        const val ARGUMENT_MOVIE = "ARGUMENT_MOVIE"
    }

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MovieDetailViewModel by viewModel()
    private var similarMoviesAdapter: SimilarMoviesAdapter = SimilarMoviesAdapter(::onClickItem)
    private var castsAdapter: CastsAdapter = CastsAdapter(::onClickItem)
    private var movieId: Int? = null
    private var isFavorite = false
    private fun onClickItem(item: Any) {
        when (item) {
            is MovieDetail -> {
                findNavController().navigateToMovieDetail(item.id, true)
            }

            is Cast -> {

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        setUpView()
        registerLiveData()
        handleEvent()
    }

    private fun getData() {
        arguments?.run {
            movieId = getInt(ARGUMENT_MOVIE)
            viewModel.setMovieId(movieId)
        }
    }

    private fun setUpView() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this@MovieDetailFragment.viewLifecycleOwner
        bindView()
        loadData()
    }

    private fun handleEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnPlay.setOnClickListener {
            val videoKey = viewModel.getVideoKey(viewModel.movieDetail.value?.videos?.results)
            if (videoKey.isNullOrEmpty()) {
                Snackbar.make(requireView(), "Something went wrong try again later", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.red))
                    .setActionTextColor(resources.getColor(R.color.white))
                    .show()
            } else {
                sendDataToActivity(videoKey)
            }
        }
        binding.btnFavorite.setOnClickListener {
            handleFavorite()
        }
        binding.btnAddWatchList.setOnClickListener {
            viewModel.watchList(Media(mediaId = movieId!!))
        }

    }

    private fun loadData() {
        with(viewModel) {
            movieId.value?.let {
                loadMovieDetail(it)
                getFavoriteMovies()
            }
        }
    }

    private fun registerLiveData() = with(viewModel) {
        movieDetail.observe(viewLifecycleOwner) {
            castsAdapter.submitList(it.casts.casts.toMutableList())
            similarMoviesAdapter.submitList(it.similar.results.toMutableList())
        }
        favoriteMovies.observe(viewLifecycleOwner) { favoriteMovies ->
            isFavorite = favoriteMovies.firstOrNull { it.id == this@MovieDetailFragment.movieId }?.let {
                binding.btnFavorite.setImageResource(R.drawable.baseline_favorite_24)
                true
            } ?: false
        }
        editWatchListResult.observe(viewLifecycleOwner) {
            when (it) {
                is DataResult.Success -> {
                    CustomSnackbar.showSuccessMessage(requireView(), it.data)
                }

                is DataResult.Error -> {
                    CustomSnackbar.showFailedMessage(requireView(), it.exception.message.toString())
                }

                DataResult.Loading -> {}
            }
        }
    }

    private fun bindView() {
        with(binding) {
            rvCasts.adapter = castsAdapter
            rvSimilar.adapter = similarMoviesAdapter
        }
    }

    private fun sendDataToActivity(data: String) {
        val intent = Intent(activity, WatchVideoActivity::class.java)
        intent.putExtra(ARGUMENT_WATCH_VIDEO, data)
        startActivity(intent)
    }

    private fun handleFavorite() {
        viewModel.movieDetail.value?.let { currentMovie ->
            val favorite =
                Favorite(currentMovie.id, currentMovie.title, currentMovie.overview, currentMovie.posterPath, currentMovie.voteAverage)
            viewModel.apply {
                if (isFavorite) {
                    deleteFavoriteMovie(favorite)
                    CustomSnackbar.showSuccessMessage(requireView(), "Successfully removed from favorites list ")
                } else {
                    saveFavoriteMovie(favorite)
                    CustomSnackbar.showSuccessMessage(requireView(), "Successfully added to favorites list")
                }
            }
            binding.btnFavorite.setImageResource(if (isFavorite) R.drawable.baseline_favorite_border_24 else R.drawable.baseline_favorite_24)
            isFavorite = !isFavorite
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}