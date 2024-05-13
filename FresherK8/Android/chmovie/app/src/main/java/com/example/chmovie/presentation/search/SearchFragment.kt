package com.example.chmovie.presentation.search

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.chmovie.R
import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.data.models.Series
import com.example.chmovie.databinding.FragmentSearchBinding
import com.example.chmovie.presentation.movie.adapter.PopularMoviesAdapter
import com.example.chmovie.presentation.series.adapter.OnTheAirSeriesAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    companion object {
        private const val SEARCH_TIME_DELAY = 1000L
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModel()

    private var moviesAdapter: PopularMoviesAdapter = PopularMoviesAdapter(::onClickItem)
    private var seriesAdapter: OnTheAirSeriesAdapter = OnTheAirSeriesAdapter(::onClickItem)

    private fun onClickItem(item: Any) {
        when (item) {
            is MovieDetail -> {
                findNavController().navigate(SearchFragmentDirections.actionNavSearchToNavMovieDetail(item.id))
            }

            is Series -> {
                findNavController().navigate(SearchFragmentDirections.actionNavSearchToNavSeriesDetail(item.id))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        registerLiveData()
        handleEvent(view)
    }

    private fun setUpView() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this@SearchFragment.viewLifecycleOwner
        bindView()
    }

    private fun bindView() {
        with(binding) {
            rvMovies.adapter = moviesAdapter
            rvSeries.adapter = seriesAdapter
        }
    }

    private fun registerLiveData() = with(viewModel) {
        movies.observe(viewLifecycleOwner) {
            checkExistResults()
            moviesAdapter.submitList(it)
        }

        series.observe(viewLifecycleOwner) {
            checkExistResults()
            seriesAdapter.submitList(it)
        }

        query.observe(viewLifecycleOwner) {
            if (it.toString().isEmpty()) {
                binding.txtTitle.text = getString(R.string.start_seaching)
                binding.txtDes.text = getString(R.string.search_des)
                showIntroView()
            }
        }
    }

    private fun checkExistResults() {
        if (viewModel.movies.value.isNullOrEmpty() && viewModel.series.value.isNullOrEmpty()) {
            binding.txtTitle.text = getString(R.string.no_results)
            binding.txtDes.text = getString(R.string.for_results, viewModel.query.value)
            showIntroView()
        } else {
            showResultView()
        }
    }

    private fun showIntroView() {
        binding.searchResultLayout.visibility = View.GONE
        binding.introLayout.visibility = View.VISIBLE
    }

    private fun showResultView() {
        binding.searchResultLayout.visibility = View.VISIBLE
        binding.introLayout.visibility = View.GONE
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun handleEvent(view: View) {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        view.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (binding.edtSearch.isFocused) {
                    val imm: InputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(binding.edtSearch.windowToken, 0)
                    binding.edtSearch.clearFocus()
                }
            }
            true
        }

        var searchJob: Job? = null

        binding.edtSearch.addTextChangedListener {
            viewModel.query.value = it.toString()
            searchJob?.cancel()
            searchJob = viewLifecycleOwner.lifecycleScope.launch {
                delay(SEARCH_TIME_DELAY)
                if (it.toString().isNotEmpty()) {
                    viewModel.searchMovies(it.toString(), 1)
                    viewModel.searchSeries(it.toString(), 1)
                }
            }
        }
    }
}