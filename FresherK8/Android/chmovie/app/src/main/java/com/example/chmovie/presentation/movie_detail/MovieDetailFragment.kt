package com.example.chmovie.presentation.movie_detail

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
import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.data.models.filterMoviesWithPosterPath
import com.example.chmovie.data.models.filterPersonsWithProfilePath
import com.example.chmovie.data.models.randomSubList
import com.example.chmovie.databinding.FragmentMovieDetailBinding
import com.example.chmovie.presentation.movie_detail.adapter.CastsAdapter
import com.example.chmovie.presentation.movie_detail.adapter.RatingAdapter
import com.example.chmovie.presentation.movie_detail.adapter.SimilarMoviesAdapter
import com.example.chmovie.presentation.watch_video.WatchVideoActivity.Companion.navigateToWatchVideo
import com.example.chmovie.shared.widget.dialogManager.DialogManagerImpl
import com.example.chmovie.shared.widget.dialogManager.hideLoadingWithDelay
import com.example.chmovie.shared.widget.showAlertSnackbar
import com.example.chmovie.shared.widget.showFailedSnackbar
import com.example.chmovie.shared.widget.showSuccessSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieDetailViewModel by viewModel()
    private val args: MovieDetailFragmentArgs by navArgs()

    private var similarMoviesAdapter: SimilarMoviesAdapter = SimilarMoviesAdapter(::onClickItem)
    private var castsAdapter: CastsAdapter = CastsAdapter(::onClickItem)
    private var ratingAdapter: RatingAdapter = RatingAdapter()

    private var videoKey: String? = null
    private var isFavorite = false
    private var isExpanded = false

    private val dialogManager by lazy {
        DialogManagerImpl(context)
    }

    private val handler = Handler(Looper.getMainLooper())

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
        getData()
        loadData()
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun handleEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnBack.setOnLongClickListener {
            handler.postDelayed({
                findNavController().navigate(MovieDetailFragmentDirections.actionNavMovieDetailToNavMovies())
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

                viewModel.movieDetail.value?.similar?.results?.let { it1 ->
                    viewModel.saveRecommendMovie(
                        it1.filterMoviesWithPosterPath().toList().randomSubList(2)
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
            if (viewModel.checkRatingExists.value == false) {
                viewModel.movieId.value?.let { id ->
                    findNavController().navigate(MovieDetailFragmentDirections.actionNavMovieDetailToNavRatingDetail(id))
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

    private fun loadData() {
        with(viewModel) {
            movieId.value?.let {
                loadMovieDetail(it)
                getFavoriteMovies()
                loadRating(it)
            }
        }
    }

    private fun registerLiveData() = with(viewModel) {
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

        movieDetail.observe(viewLifecycleOwner) {
            videoKey = viewModel.getVideoKey(it.videos.results)
            viewModel.checkRatingExists(it.id)

            castsAdapter.submitList(it.casts.casts.filterPersonsWithProfilePath())
            similarMoviesAdapter.submitList(it.similar.results.filterMoviesWithPosterPath())

            if(it.overview.isEmpty()){
                binding.txtReadMore.visibility = View.GONE
            }
        }

        favoriteMovies.observe(viewLifecycleOwner) { favoriteMovies ->
            isFavorite = favoriteMovies.firstOrNull { it.id == args.movieId }?.let {
                binding.btnFavorite.setImageResource(R.drawable.baseline_favorite_24)
                true
            } ?: false
        }

        rating.observe(viewLifecycleOwner) {
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

    private fun bindView() {
        with(binding) {
            rvCasts.adapter = castsAdapter
            rvSimilar.adapter = similarMoviesAdapter
            rvRating.adapter = ratingAdapter
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
