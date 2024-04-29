package com.example.chmovie.presentation.movie

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.chmovie.data.repositories.MovieRepository
import com.example.chmovie.databinding.FragmentMovieBinding

class MovieFragment : Fragment() {
    companion object {
        fun newInstance() = MovieFragment()
    }

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!
    private val movieRepository: MovieRepository by lazy {
        MovieRepository()
    }
    private val movieViewModelFactory: MovieViewModelFactory by lazy {
        MovieViewModelFactory(movieRepository)
    }
    private val viewModel: MovieViewModel by lazy {
        ViewModelProvider(this, movieViewModelFactory)[MovieViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.popularMovies.observe(requireActivity(), Observer { popularMovies ->
            Log.d("PopularMovies", popularMovies.toString())
        })

        viewModel.loadPopularMovies(page = 1)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}