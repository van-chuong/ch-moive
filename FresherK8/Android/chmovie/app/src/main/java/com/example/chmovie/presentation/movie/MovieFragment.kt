package com.example.chmovie.presentation.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.databinding.FragmentMovieBinding
import com.example.chmovie.presentation.movie.adapter.ComingSoonMoviesAdapter
import com.example.chmovie.presentation.movie.adapter.InTheaterMoviesAdapter
import com.example.chmovie.presentation.movie.adapter.PopularMoviesAdapter
import com.example.chmovie.presentation.movie.adapter.TrendingMoviesAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieFragment : Fragment(){
    companion object {
        fun newInstance() = MovieFragment()
    }

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieViewModel by viewModel()
    private var trendingAdapter: TrendingMoviesAdapter = TrendingMoviesAdapter(::onClickItem)
    private var popularAdapter: PopularMoviesAdapter = PopularMoviesAdapter(::onClickItem)
    private var inTheaterAdapter: InTheaterMoviesAdapter = InTheaterMoviesAdapter(::onClickItem)
    private var comingSoonAdapter: ComingSoonMoviesAdapter = ComingSoonMoviesAdapter(::onClickItem)

    private fun onClickItem(movie: MovieDetail) {
//        val bundle = Bundle().apply {
//            putInt(MovieDetailFragment.ARGUMENT_MOVIE, movie.id)
//        }
//        findNavController().navigate(R.id.nav_movie_detail, bundle)
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
            loadTrendingMovies(1)
            loadPopularMovies(1)
            loadInTheaterMovies(1)
            loadComingSoonMovies(1)
        }
    }

    private fun registerLiveData() = with(viewModel) {
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