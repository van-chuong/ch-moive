package com.example.chmovie.presentation.my_favorite_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.chmovie.data.models.Favorite
import com.example.chmovie.databinding.FragmentMyFavoriteListBinding
import com.example.chmovie.presentation.my_favorite_list.adapter.FavoriteAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyFavoriteListFragment : Fragment() {

    private var _binding: FragmentMyFavoriteListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyFavoriteListViewModel by viewModel()

    private var favoriteAdapter: FavoriteAdapter = FavoriteAdapter(::onClickItem)

    private fun onClickItem(favorite: Favorite) {
        if (favorite.mediaType == Favorite.TV) {
            findNavController().navigate(MyFavoriteListFragmentDirections.actionNavMyFavoriteListToNavSeriesDetail(favorite.id))
        } else if (favorite.mediaType == Favorite.MOVIE) {
            findNavController().navigate(MyFavoriteListFragmentDirections.actionNavMyFavoriteListToNavMovieDetail(favorite.id))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyFavoriteListBinding.inflate(inflater, container, false)
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
            getFavoriteList()
        }
    }

    private fun setUpView() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this@MyFavoriteListFragment.viewLifecycleOwner
        bindView()
    }

    private fun bindView() {
        with(binding) {
            rvFavorite.adapter = favoriteAdapter
        }
    }

    private fun registerLiveData() = with(viewModel) {
        favoriteList.observe(viewLifecycleOwner) {
            if (it.size > 0) {
                with(binding) {
                    rvFavorite.visibility = View.VISIBLE
                    txtEmpty.visibility = View.GONE
                }
            } else {
                with(binding) {
                    rvFavorite.visibility = View.GONE
                    txtEmpty.visibility = View.VISIBLE
                }
            }
            favoriteAdapter.submitList(it)
        }
    }

    private fun handleEvent() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                return makeMovementFlags(0, ItemTouchHelper.START or ItemTouchHelper.END)
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.START || direction == ItemTouchHelper.END) {
                    val currentList = favoriteAdapter.currentList.toMutableList()
                    val deletedItem = currentList.removeAt(viewHolder.bindingAdapterPosition)
                    viewModel.deleteFavoriteMovie(deletedItem)
                    favoriteAdapter.submitList(currentList)
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.rvFavorite)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}