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
import com.example.chmovie.shared.helper.OnItemClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieFragment : Fragment(), OnItemClickListener<MovieDetail> {
    companion object {
        fun newInstance() = MovieFragment()
    }

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieViewModel by viewModel()
    private var trendingAdapter: TrendingMoviesAdapter? = null
    private var popularAdapter: PopularMoviesAdapter? = null
    private var inTheaterAdapter: InTheaterMoviesAdapter? = null
    private var comingSoonAdapter: ComingSoonMoviesAdapter? = null
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
        setUpView()
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

    private fun setUpView() {
    }

    private fun registerLiveData() = with(viewModel) {
        trendingMovies.observe(viewLifecycleOwner) {
            trendingAdapter?.updateData(it)
        }
        topRatedMovies.observe(viewLifecycleOwner) {
            popularAdapter?.updateData(it)
        }
        inTheaterMovies.observe(viewLifecycleOwner) {
            inTheaterAdapter?.updateData(it)
        }
        comingSoonMovies.observe(viewLifecycleOwner) {
            comingSoonAdapter?.updateData(it)
        }
    }

    private fun bindView() {
        trendingAdapter = TrendingMoviesAdapter().apply {
            registerItemClickListener(this@MovieFragment)
        }
        popularAdapter = PopularMoviesAdapter().apply {
            registerItemClickListener(this@MovieFragment)
        }
        inTheaterAdapter = InTheaterMoviesAdapter().apply {
            registerItemClickListener(this@MovieFragment)
        }
        comingSoonAdapter = ComingSoonMoviesAdapter().apply {
            registerItemClickListener(this@MovieFragment)
        }
        with(binding) {
            rvTrending.adapter = trendingAdapter
            rvPopular.adapter = popularAdapter
            rvInTheatre.adapter = inTheaterAdapter
            rvComingSoon.adapter = comingSoonAdapter
        }
    }

    override fun onItemViewClick(item: MovieDetail, position: Int) {
        // Navigation to MovieDetail
    }

    override fun onStop() {
        super.onStop()
        unRegisterItemClickListeners()
    }

    private fun unRegisterItemClickListeners() {
        with(binding) {
            rvTrending.adapter?.let { (it as TrendingMoviesAdapter).unRegisterItemClickListener() }
            rvPopular.adapter?.let { (it as PopularMoviesAdapter).unRegisterItemClickListener() }
            rvInTheatre.adapter?.let { (it as InTheaterMoviesAdapter).unRegisterItemClickListener() }
            rvComingSoon.adapter?.let { (it as ComingSoonMoviesAdapter).unRegisterItemClickListener() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}