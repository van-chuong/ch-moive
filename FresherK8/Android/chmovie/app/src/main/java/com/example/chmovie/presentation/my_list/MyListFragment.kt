package com.example.chmovie.presentation.my_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.chmovie.databinding.FragmentMyListBinding

class MyListFragment : Fragment() {
    companion object {
        fun newInstance() = MyListFragment()
    }

    private var _binding: FragmentMyListBinding? = null
    private val binding get() = _binding!!


    private val viewModel: MyListViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.text.observe(viewLifecycleOwner) {
            binding.text.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}