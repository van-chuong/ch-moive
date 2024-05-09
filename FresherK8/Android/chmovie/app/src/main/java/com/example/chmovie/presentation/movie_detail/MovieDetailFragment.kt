package com.example.chmovie.presentation.movie_detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.chmovie.R
import com.example.chmovie.data.models.Cast
import com.example.chmovie.data.models.Favorite
import com.example.chmovie.data.models.Media
import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.databinding.FragmentMovieDetailBinding
import com.example.chmovie.presentation.movie_detail.adapter.CastsAdapter
import com.example.chmovie.presentation.movie_detail.adapter.SimilarMoviesAdapter
import com.example.chmovie.presentation.watch_video.WatchVideoActivity
import com.example.chmovie.presentation.watch_video.WatchVideoActivity.Companion.navigateToWatchVideo
import com.example.chmovie.shared.scheduler.DataResult
import com.example.chmovie.shared.widget.showFailedSnackbar
import com.example.chmovie.shared.widget.showSuccessSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class MovieDetailFragment : Fragment() {
    private val args: MovieDetailFragmentArgs by navArgs()
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MovieDetailViewModel by viewModel()
    private var similarMoviesAdapter: SimilarMoviesAdapter = SimilarMoviesAdapter(::onClickItem)
    private var castsAdapter: CastsAdapter = CastsAdapter(::onClickItem)
    private var isFavorite = false
    private fun onClickItem(item: Any) {
        when (item) {
            is MovieDetail -> {
                findNavController().navigate(MovieDetailFragmentDirections.actionNavMovieDetailSelf(item.id))
            }

            is Cast -> {
                findNavController().navigate(MovieDetailFragmentDirections.actionNavMovieDetailToNavPersonDetail(item.id))
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
        viewModel.setMovieId(args.movieId)
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
                requireView().showFailedSnackbar("Something went wrong try again later")
            } else {
                navigateToWatchVideo(requireActivity(), videoKey)
            }
        }
        binding.btnFavorite.setOnClickListener {
            handleFavorite()
        }
        binding.btnAddWatchList.setOnClickListener {
            viewModel.watchList(Media.of(viewModel.movieDetail.value))
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
            isFavorite = favoriteMovies.firstOrNull { it.id == args.movieId }?.let {
                binding.btnFavorite.setImageResource(R.drawable.baseline_favorite_24)
                true
            } ?: false
        }
        editWatchListResult.observe(viewLifecycleOwner) {
            when (it) {
                is DataResult.Success -> {
                    requireView().showSuccessSnackbar(it.data)
                }

                is DataResult.Error -> {
                    requireView().showFailedSnackbar(it.exception.message.toString())
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

    private fun handleFavorite() {
        viewModel.movieDetail.value?.let { currentMovie ->
            val favorite = Favorite.of(currentMovie)
            viewModel.apply {
                if (isFavorite) {
                    deleteFavoriteMovie(favorite)
                    requireView().showSuccessSnackbar("Successfully removed from favorites list")
                } else {
                    saveFavoriteMovie(favorite)
                    requireView().showSuccessSnackbar("Successfully added to favorites list")
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