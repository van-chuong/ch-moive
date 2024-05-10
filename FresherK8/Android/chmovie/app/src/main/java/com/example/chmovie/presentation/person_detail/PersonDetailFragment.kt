package com.example.chmovie.presentation.person_detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.chmovie.data.models.Cast
import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.data.models.Series
import com.example.chmovie.databinding.FragmentPersonDetailBinding
import com.example.chmovie.presentation.movie.adapter.PopularMoviesAdapter
import com.example.chmovie.presentation.movie_detail.MovieDetailFragmentDirections
import com.example.chmovie.presentation.series.adapter.TopRatedSeriesAdapter
import com.example.chmovie.presentation.series.adapter.TrendingSeriesAdapter
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
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
        personDetail.observe(viewLifecycleOwner) {
            popularMoviesAdapter.submitList(it.movieCredits.cast)
            popularSeriesAdapter.submitList(it.tvCredits.cast)
        }
    }

    private fun handleEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}