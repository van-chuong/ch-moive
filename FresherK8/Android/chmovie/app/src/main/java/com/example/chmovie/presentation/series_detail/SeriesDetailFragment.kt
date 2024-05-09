package com.example.chmovie.presentation.series_detail

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
import com.example.chmovie.data.models.Series
import com.example.chmovie.databinding.FragmentSeriesDetailBinding
import com.example.chmovie.presentation.movie_detail.MovieDetailFragment
import com.example.chmovie.presentation.movie_detail.MovieDetailViewModel
import com.example.chmovie.presentation.movie_detail.adapter.CastsAdapter
import com.example.chmovie.presentation.series_detail.adapter.SimilarSeriesAdapter
import com.example.chmovie.presentation.watch_video.WatchVideoActivity.Companion.navigateToWatchVideo
import com.example.chmovie.shared.scheduler.DataResult
import com.example.chmovie.shared.widget.showFailedSnackbar
import com.example.chmovie.shared.widget.showSuccessSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class SeriesDetailFragment : Fragment() {
    private var _binding: FragmentSeriesDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SeriesDetailViewModel by viewModel()
    private val movieDetailViewModel: MovieDetailViewModel by viewModel()
    private val args: SeriesDetailFragmentArgs by navArgs()
    private var similarSeriesAdapter: SimilarSeriesAdapter = SimilarSeriesAdapter(::onClickItem)
    private var castsAdapter: CastsAdapter = CastsAdapter(::onClickItem)
    private var isFavorite = false
    private fun onClickItem(item: Any) {
        when (item) {
            is Series -> {
                findNavController().navigate(SeriesDetailFragmentDirections.actionNavSeriesDetailSelf(item.id))
            }

            is Cast -> {

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeriesDetailBinding.inflate(inflater, container, false)
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
        viewModel.setSeriesId(args.seriesId)
    }

    private fun setUpView() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this@SeriesDetailFragment.viewLifecycleOwner
        bindView()
        loadData()
    }

    private fun loadData() {
        with(viewModel) {
            viewModel.seriesId.value?.let { loadSeriesDetail(it) }
        }
        movieDetailViewModel.getFavoriteMovies()
    }

    private fun bindView() {
        with(binding) {
            rvCasts.adapter = castsAdapter
            rvSimilar.adapter = similarSeriesAdapter
        }
    }

    private fun registerLiveData() {
        with(viewModel) {
            seriesDetail.observe(viewLifecycleOwner) {
                castsAdapter.submitList(it.casts.casts.toMutableList())
                similarSeriesAdapter.submitList(it.similar.results.toMutableList())
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
        with(movieDetailViewModel) {
            favoriteMovies.observe(viewLifecycleOwner) { favorites ->
                isFavorite = favorites.firstOrNull { it.id == args.seriesId }?.let {
                    binding.btnFavorite.setImageResource(R.drawable.baseline_favorite_24)
                    true
                } ?: false
            }
        }
    }

    private fun handleEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnPlay.setOnClickListener {
            val videoKey = viewModel.getVideoKey(viewModel.seriesDetail.value?.videos?.results)
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
            viewModel.watchList(Media.of(viewModel.seriesDetail.value))
        }
    }

    private fun handleFavorite() {
        viewModel.seriesDetail.value?.let { currentMovie ->
            val favorite = Favorite.of(currentMovie)
            movieDetailViewModel.apply {
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
}