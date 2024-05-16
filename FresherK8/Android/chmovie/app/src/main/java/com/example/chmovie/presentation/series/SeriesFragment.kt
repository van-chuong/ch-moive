package com.example.chmovie.presentation.series

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chmovie.data.models.Series
import com.example.chmovie.databinding.FragmentSeriesBinding
import com.example.chmovie.presentation.main.MainActivity
import com.example.chmovie.presentation.series.adapter.AirTodaySeriesAdapter
import com.example.chmovie.presentation.series.adapter.OnTheAirSeriesAdapter
import com.example.chmovie.presentation.series.adapter.TopRatedSeriesAdapter
import com.example.chmovie.presentation.series.adapter.TrendingSeriesAdapter
import com.example.chmovie.shared.widget.dialogManager.DialogManagerImpl
import com.example.chmovie.shared.widget.dialogManager.hideLoadingWithDelay
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.random.Random

class SeriesFragment : Fragment() {

    private var _binding: FragmentSeriesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SeriesViewModel by viewModel()

    private var trendingAdapter: TrendingSeriesAdapter = TrendingSeriesAdapter(::onClickItem)
    private var topRatedAdapter: TopRatedSeriesAdapter = TopRatedSeriesAdapter(::onClickItem)
    private var onTheAirAdapter: OnTheAirSeriesAdapter = OnTheAirSeriesAdapter(::onClickItem)
    private var airTodayAdapter: AirTodaySeriesAdapter = AirTodaySeriesAdapter(::onClickItem)

    private fun onClickItem(series: Series) {
        findNavController().navigate(SeriesFragmentDirections.actionNavSeriesToNavSeriesDetail(series.id))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeriesBinding.inflate(inflater, container, false)
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
            loadAirTodaySeries(Random.nextInt(1,3))
            loadTrendingSeries(Random.nextInt(1,3))
            loadOnTheAirSeries(Random.nextInt(1,3))
            loadTopRatedSeries(Random.nextInt(1,3))
        }
    }

    private fun bindView() {
        with(binding) {
            rvTrending.adapter = trendingAdapter
            rvOnTheAir.adapter = onTheAirAdapter
            rvAirToday.adapter = airTodayAdapter
            rvMostPopular.adapter = topRatedAdapter
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

        trendingSeries.observe(viewLifecycleOwner) {
            trendingAdapter.submitList(it)
        }

        topRatedSeries.observe(viewLifecycleOwner) {
            topRatedAdapter.submitList(it)
        }

        airTodaySeries.observe(viewLifecycleOwner) {
            airTodayAdapter.submitList(it)
        }

        onTheAirSeries.observe(viewLifecycleOwner) {
            onTheAirAdapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}