package com.example.chmovie.presentation.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.databinding.FragmentMovieBinding
import com.example.chmovie.presentation.main.MainActivity
import com.example.chmovie.presentation.movie.adapter.ComingSoonMoviesAdapter
import com.example.chmovie.presentation.movie.adapter.InTheaterMoviesAdapter
import com.example.chmovie.presentation.movie.adapter.PopularMoviesAdapter
import com.example.chmovie.presentation.movie.adapter.TrendingMoviesAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.random.Random

class MovieFragment : Fragment() {

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieViewModel by viewModel()

    private var trendingAdapter: TrendingMoviesAdapter = TrendingMoviesAdapter(::onClickItem)
    private var popularAdapter: PopularMoviesAdapter = PopularMoviesAdapter(::onClickItem)
    private var inTheaterAdapter: InTheaterMoviesAdapter = InTheaterMoviesAdapter(::onClickItem)
    private var comingSoonAdapter: ComingSoonMoviesAdapter = ComingSoonMoviesAdapter(::onClickItem)

    private fun onClickItem(movie: MovieDetail) {
        findNavController().navigate(MovieFragmentDirections.actionNavMoviesToNavMovieDetail(movie.id))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)
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
            loadTrendingMovies(Random.nextInt(1, 3))
            loadPopularMovies(Random.nextInt(1, 3))
            loadInTheaterMovies(Random.nextInt(1, 3))
            loadComingSoonMovies(Random.nextInt(1, 3))
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

        trendingMovies.observe(viewLifecycleOwner) {
            trendingAdapter.submitList(it)
        }

        topRatedMovies.observe(viewLifecycleOwner) {
            popularAdapter.submitList(it)
        }

        inTheaterMovies.observe(viewLifecycleOwner) {
            inTheaterAdapter.submitList(it)
        }

        comingSoonMovies.observe(viewLifecycleOwner) {
            comingSoonAdapter.submitList(it)
        }
    }

    private fun bindView() {
        with(binding) {
            rvTrending.adapter = trendingAdapter
            rvPopular.adapter = popularAdapter
            rvInTheatre.adapter = inTheaterAdapter
            rvComingSoon.adapter = comingSoonAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}