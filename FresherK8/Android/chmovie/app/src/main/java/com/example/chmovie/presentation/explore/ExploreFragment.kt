package com.example.chmovie.presentation.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chmovie.data.models.Cast
import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.data.models.MovieProvider
import com.example.chmovie.data.models.Series
import com.example.chmovie.databinding.FragmentExploreBinding
import com.example.chmovie.presentation.explore.adapter.PopularPersonAdapter
import com.example.chmovie.presentation.explore.adapter.PopularProviderAdapter
import com.example.chmovie.presentation.main.MainActivity
import com.example.chmovie.presentation.movie.adapter.PopularMoviesAdapter
import com.example.chmovie.presentation.series.adapter.TopRatedSeriesAdapter
import com.example.chmovie.shared.widget.showFailedSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExploreViewModel by viewModel()

    private var featuredAdapter: PopularProviderAdapter = PopularProviderAdapter(::onClickItem)
    private var popularPersonAdapter: PopularPersonAdapter = PopularPersonAdapter(::onClickItem)
    private var seriesAdapter: TopRatedSeriesAdapter = TopRatedSeriesAdapter(::onClickItem)
    private var moviesAdapter: PopularMoviesAdapter = PopularMoviesAdapter(::onClickItem)

    private fun onClickItem(item: Any) {
        when (item) {
            is MovieDetail -> {
                findNavController().navigate(ExploreFragmentDirections.actionNavExploreToNavMovieDetail(item.id))
            }

            is Series -> {
                findNavController().navigate(ExploreFragmentDirections.actionNavExploreToNavSeriesDetail(item.id))
            }

            is Cast -> {
                findNavController().navigate(ExploreFragmentDirections.actionNavExploreToNavPersonDetail(item.id))
            }

            is MovieProvider -> {
                binding.root.showFailedSnackbar("Content about providers is being updated, please try again later")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        bindView()
        registerLiveData()
    }

    private fun loadData() {
        with(viewModel) {
            loadPopularProvider()
            loadPopularPerson()
            loadRecommendMovies()
            loadRecommendSeries()
        }
    }

    private fun bindView() {
        with(binding) {
            rvFeaturedProvider.adapter = featuredAdapter
            rvPopularPerson.adapter = popularPersonAdapter
            rvRecommendMovies.adapter = moviesAdapter
            rvRecommendSeries.adapter = seriesAdapter
        }
    }

    private fun registerLiveData() = with(viewModel) {
        isLoading.observe(viewLifecycleOwner) {
            if (it) {
                (activity as MainActivity).showLoading()
            } else {
                (activity as MainActivity).hideLoading(isSuccess.value == false)
            }
        }

        popularProvider.observe(viewLifecycleOwner) {
            featuredAdapter.submitList(it)
        }

        popularPerson.observe(viewLifecycleOwner) {
            popularPersonAdapter.submitList(it)
        }

        recommendMovies.observe(viewLifecycleOwner) {
            if (it.size > 0) {
                moviesAdapter.submitList(it)
                binding.recommendMovieLayout.visibility = View.VISIBLE
            } else {
                binding.recommendMovieLayout.visibility = View.GONE
            }
        }

        recommendSeries.observe(viewLifecycleOwner) {
            if (it.size > 0) {
                seriesAdapter.submitList(it)
                binding.recommendSeriesLayout.visibility = View.VISIBLE
            } else {
                binding.recommendSeriesLayout.visibility = View.GONE
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}