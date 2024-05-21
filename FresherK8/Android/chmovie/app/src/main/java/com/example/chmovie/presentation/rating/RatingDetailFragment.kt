package com.example.chmovie.presentation.rating

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.chmovie.R
import com.example.chmovie.databinding.FragmentRatingDetailBinding
import com.example.chmovie.presentation.rating.adapter.RatingButtonAdapter
import com.example.chmovie.shared.scheduler.DataResult
import com.example.chmovie.shared.widget.dialogManager.DialogManagerImpl
import com.example.chmovie.shared.widget.dialogManager.hideLoadingWithDelay
import com.example.chmovie.shared.widget.showFailedSnackbar
import com.example.chmovie.shared.widget.showSuccessSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class RatingDetailFragment : Fragment() {

    private var _binding: FragmentRatingDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RatingDetailViewModel by viewModel()
    private val args: RatingDetailFragmentArgs by navArgs()

    private val dialogManager by lazy {
        DialogManagerImpl(context)
    }

    private var buttonAdapter: RatingButtonAdapter = RatingButtonAdapter(::onClickItem)

    private var rating = 0
    private fun onClickItem(i: Int) {
        rating = i
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRatingDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        bindView()
        registerLiveData()
        handleEvent()
    }

    private fun getData() {
        viewModel.setId(args.id)
    }

    private fun bindView() {
        binding.rvRating.adapter = buttonAdapter
        buttonAdapter.submitList((1..10).toList().reversed())
    }

    private fun registerLiveData() = with(viewModel) {
        addRatingResult.observe(viewLifecycleOwner) {
            when (it) {
                is DataResult.Success -> {
                    dialogManager.hideLoadingWithDelay()
                    requireView().showSuccessSnackbar("Your rating has been successfully added")
                    findNavController().navigateUp()
                }

                is DataResult.Error -> {
                    dialogManager.hideLoadingWithDelay()
                    requireView().showFailedSnackbar(it.exception.message.toString())
                }

                DataResult.Loading -> {
                    dialogManager.showLoading()
                }
            }
        }
    }

    private fun handleEvent() {
        with(binding) {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btnSubmit.setOnClickListener {
                if (rating == 0) {
                    root.showFailedSnackbar("You have not selected a rating yet")
                } else {
                    val comment = binding.edtComment.text.toString().trim()

                    if (comment.isEmpty()) {
                        requireView().showFailedSnackbar("Comments cannot be left blank")
                    } else {
                        viewModel.addRating(rating, comment, args.type)
                    }
                }
            }

            edtComment.addTextChangedListener(textWatcher)
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val comment = binding.edtComment.text.toString().trim()

            if (comment.isNotEmpty() && rating !=0) {
                binding.btnSubmit.setBackgroundResource(R.drawable.button_primary_bg)
                binding.btnSubmit.setTextColor(resources.getColor(R.color.tint_primary))
            } else {
                binding.btnSubmit.setBackgroundResource(R.drawable.button_bg)
                binding.btnSubmit.setTextColor(resources.getColor(R.color.tint_secondary))
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
}