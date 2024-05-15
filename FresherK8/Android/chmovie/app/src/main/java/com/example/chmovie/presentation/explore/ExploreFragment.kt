package com.example.chmovie.presentation.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chmovie.data.models.Cast
import com.example.chmovie.data.models.MovieProvider
import com.example.chmovie.databinding.FragmentExploreBinding
import com.example.chmovie.presentation.explore.adapter.PopularPersonAdapter
import com.example.chmovie.presentation.explore.adapter.PopularProviderAdapter
import com.example.chmovie.presentation.main.MainActivity
import com.example.chmovie.shared.widget.dialogManager.DialogManagerImpl
import com.example.chmovie.shared.widget.dialogManager.hideLoadingWithDelay
import com.example.chmovie.shared.widget.showFailedSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExploreViewModel by viewModel()

    private var featuredAdapter: PopularProviderAdapter = PopularProviderAdapter(::onClickItem)
    private var popularPersonAdapter: PopularPersonAdapter = PopularPersonAdapter(::onClickItem)

    private val dialogManager by lazy {
        DialogManagerImpl(context)
    }

    private fun onClickItem(item: Any) {
        when (item) {
            is MovieProvider -> {
            }

            is Cast -> {
                findNavController().navigate(ExploreFragmentDirections.actionNavExploreToNavPersonDetail(item.id))
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
        }
    }

    private fun bindView() {
        with(binding) {
            rvFeaturedProvider.adapter = featuredAdapter
            rvPopularPerson.adapter = popularPersonAdapter
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}