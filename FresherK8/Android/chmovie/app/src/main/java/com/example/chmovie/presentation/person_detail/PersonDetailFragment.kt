package com.example.chmovie.presentation.person_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.data.models.Series
import com.example.chmovie.data.models.filterMoviesWithPosterPath
import com.example.chmovie.data.models.filterSeriesWithPosterPath
import com.example.chmovie.databinding.FragmentPersonDetailBinding
import com.example.chmovie.presentation.movie.adapter.PopularMoviesAdapter
import com.example.chmovie.presentation.series.adapter.TopRatedSeriesAdapter
import com.example.chmovie.shared.widget.dialogManager.DialogManagerImpl
import com.example.chmovie.shared.widget.dialogManager.hideLoadingWithDelay
import com.example.chmovie.shared.widget.showFailedSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class PersonDetailFragment : Fragment() {

    private var _binding: FragmentPersonDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PersonDetailViewModel by viewModel()
    private val args: PersonDetailFragmentArgs by navArgs()

    private var popularMoviesAdapter: PopularMoviesAdapter = PopularMoviesAdapter(::onClickItem)
    private var popularSeriesAdapter: TopRatedSeriesAdapter = TopRatedSeriesAdapter(::onClickItem)

    private fun onClickItem(item: Any) {
        when (item) {
            is MovieDetail -> {
                findNavController().navigate(PersonDetailFragmentDirections.actionNavPersonDetailToNavMovieDetail(item.id))
            }

            is Series -> {
                findNavController().navigate(PersonDetailFragmentDirections.actionNavPersonDetailToNavSeriesDetail(item.id))
            }
        }
    }

    private val dialogManager by lazy {
        DialogManagerImpl(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loadData()
        _binding = FragmentPersonDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        registerLiveData()
        handleEvent()
    }

    private fun loadData() {
        with(viewModel) {
            loadPersonDetail(args.personId)
        }
    }

    private fun setUpView() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this@PersonDetailFragment.viewLifecycleOwner
        bindView()
    }

    private fun bindView() {
        with(binding){
            rvPopularMovie.adapter = popularMoviesAdapter
            rvPopularSeries.adapter = popularSeriesAdapter
        }
    }

    private fun registerLiveData() = with(viewModel) {
        isLoading.observe(viewLifecycleOwner) {
            if (it)
                dialogManager.showLoading()
            else {
                dialogManager.hideLoadingWithDelay()

                if(isSuccess.value == false){
                    binding.root.showFailedSnackbar("Lost network connection, failed data download")
                }
            }
        }

        personDetail.observe(viewLifecycleOwner) {
            popularMoviesAdapter.submitList(it.movieCredits.cast.filterMoviesWithPosterPath())
            popularSeriesAdapter.submitList(it.tvCredits.cast.filterSeriesWithPosterPath())
        }
    }

    private fun handleEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}