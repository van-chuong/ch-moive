package com.example.chmovie.presentation.series_detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MotionEvent
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
import com.example.chmovie.data.models.filterPersonsWithProfilePath
import com.example.chmovie.data.models.filterSeriesWithPosterPath
import com.example.chmovie.data.models.randomSubList
import com.example.chmovie.databinding.FragmentSeriesDetailBinding
import com.example.chmovie.presentation.movie_detail.MovieDetailViewModel
import com.example.chmovie.presentation.movie_detail.adapter.CastsAdapter
import com.example.chmovie.presentation.movie_detail.adapter.RatingAdapter
import com.example.chmovie.presentation.series_detail.adapter.SimilarSeriesAdapter
import com.example.chmovie.presentation.watch_video.WatchVideoActivity.Companion.navigateToWatchVideo
import com.example.chmovie.shared.scheduler.DataResult
import com.example.chmovie.shared.widget.dialogManager.DialogManagerImpl
import com.example.chmovie.shared.widget.dialogManager.hideLoadingWithDelay
import com.example.chmovie.shared.widget.showAlertSnackbar
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
    private var ratingAdapter: RatingAdapter = RatingAdapter()

    private var isFavorite = false
    private var isExpanded = false
    private var videoKey: String? = null

    private fun onClickItem(item: Any) {
        when (item) {
            is Series -> {
                findNavController().navigate(SeriesDetailFragmentDirections.actionNavSeriesDetailSelf(item.id))
            }

            is Cast -> {
                findNavController().navigate(SeriesDetailFragmentDirections.actionNavSeriesDetailToNavPersonDetail(item.id))
            }
        }
    }

    private val dialogManager by lazy {
        DialogManagerImpl(context)
    }

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        getData()
        loadData()
        _binding = FragmentSeriesDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
    }

    private fun loadData() {
        with(viewModel) {
            viewModel.seriesId.value?.let {
                loadSeriesDetail(it)
                movieDetailViewModel.loadRating(it)
            }
        }

        movieDetailViewModel.getFavoriteMovies()
    }

    private fun bindView() {
        with(binding) {
            rvCasts.adapter = castsAdapter
            rvSimilar.adapter = similarSeriesAdapter
            rvRating.adapter = ratingAdapter
        }
    }

    private fun registerLiveData() {
        with(viewModel) {
            isLoading.observe(viewLifecycleOwner) {
                if (it)
                    dialogManager.showLoading()
                else {
                    dialogManager.hideLoadingWithDelay()

                    if (isSuccess.value == false) {
                        binding.root.showFailedSnackbar("Lost network connection, failed data download")
                    }
                }
            }

            seriesDetail.observe(viewLifecycleOwner) {
                movieDetailViewModel.checkRatingExists(it.id)
                videoKey = viewModel.getVideoKey(it.videos.results)

                castsAdapter.submitList(it.casts.casts.filterPersonsWithProfilePath())
                similarSeriesAdapter.submitList(it.similar.results.filterSeriesWithPosterPath())

                if(it.overview.isEmpty()){
                    binding.txtReadMore.visibility = View.GONE
                }
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

        movieDetailViewModel.rating.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.rvRating.visibility = View.GONE
                binding.txtRatingBeingUpdate.visibility = View.VISIBLE
            } else {
                ratingAdapter.submitList(it)
                binding.rvRating.visibility = View.VISIBLE
                binding.txtRatingBeingUpdate.visibility = View.GONE
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun handleEvent() {
        binding.btnBack.setOnClickListener {
            if (args.isFromNotification){
                findNavController().navigate(R.id.nav_series)
            }else{
                findNavController().navigateUp()
            }
        }

        binding.btnBack.setOnLongClickListener {
            handler.postDelayed({
                findNavController().navigate(SeriesDetailFragmentDirections.actionNavSeriesDetailToNavMovies())
            }, 1000)
            true
        }

        binding.btnBack.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP || motionEvent.action == MotionEvent.ACTION_CANCEL) {
                handler.removeCallbacksAndMessages(null)
            }
            false
        }

        binding.btnPlay.setOnClickListener {
            if (videoKey.isNullOrEmpty()) {
                requireView().showFailedSnackbar("Video is not available, being updated, please try again later!")
            } else {
                navigateToWatchVideo(requireActivity(), videoKey!!)
                viewModel.seriesDetail.value?.similar?.results?.let { it1 ->
                    viewModel.saveRecommendSeries(
                        it1.filterSeriesWithPosterPath().toList().randomSubList(2)
                    )
                }
            }
        }

        binding.btnFavorite.setOnClickListener {
            handleFavorite()
        }


        binding.btnStarRoom.setOnClickListener {
            if (videoKey.isNullOrEmpty()) {
                view?.showFailedSnackbar("Video is not available, being updated, please try again later!")
            } else {
                viewModel.checkRoomCodeExist(videoKey!!, requireContext())
            }
        }

        binding.btnAddRating.setOnClickListener {
            if (movieDetailViewModel.checkRatingExists.value == false) {
                viewModel.seriesId.value?.let { id ->
                    findNavController().navigate(SeriesDetailFragmentDirections.actionNavSeriesDetailToNavRatingDetail(id,Media.TV))
                }
            } else {
                binding.view.showAlertSnackbar("You have already rated this movie !")
            }
        }

        binding.txtReadMore.setOnClickListener {
            handleReadMore()
        }
    }

    private fun handleReadMore() {
        binding.txtSynopsis.maxLines = if (isExpanded) 4 else Integer.MAX_VALUE
        binding.txtReadMore.text = getString(if (isExpanded) R.string.read_more else R.string.read_less)
        isExpanded = !isExpanded
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