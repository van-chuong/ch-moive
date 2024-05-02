package com.example.chmovie.presentation.movie

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.chmovie.databinding.FragmentMovieBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieFragment : Fragment() {
    companion object {
        fun newInstance() = MovieFragment()
    }

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.popularMovies.observe(viewLifecycleOwner) {
            Log.d("PopularMovies", it.toString())
        }
        viewModel.loadPopularMovies(1)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}