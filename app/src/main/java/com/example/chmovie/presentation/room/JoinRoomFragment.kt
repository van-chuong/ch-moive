package com.example.chmovie.presentation.room

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.chmovie.databinding.FragmentJoinRoomBinding
import com.example.chmovie.presentation.main.MainActivity
import com.example.chmovie.presentation.room.start_room.StartRoomActivity
import com.example.chmovie.shared.scheduler.DataResult
import com.example.chmovie.shared.widget.dialogManager.DialogManagerImpl
import com.example.chmovie.shared.widget.dialogManager.hideLoadingWithDelay
import com.example.chmovie.shared.widget.showFailedSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class JoinRoomFragment : Fragment() {

    private var _binding: FragmentJoinRoomBinding? = null
    private val binding get() = _binding!!

    private val args: JoinRoomFragmentArgs by navArgs()
    private val viewModel: JoinRoomViewModel by viewModel()

    private val dialogManager by lazy {
        DialogManagerImpl(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJoinRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        handleEvent(view)
        registerLiveData()
    }

    private fun initView() {
        if (args.roomId != 0) {
            binding.edtRoomCode.setText(args.roomId.toString())
        }
    }

    private fun registerLiveData() = with(viewModel) {
        isLoading.observe(viewLifecycleOwner) {
            (activity as MainActivity).hideLoading(false)
        }

        roomCodeIsExist.observe(viewLifecycleOwner) {
            when (it) {
                is DataResult.Success -> {
                    dialogManager.hideLoadingWithDelay()
                    if (it.data) {
                        viewModel.room.value?.let { it1 ->
                            StartRoomActivity.newInstance(requireContext(), it1)
                        }
                    } else {
                        requireView().showFailedSnackbar("Room does not exist")
                    }
                }

                is DataResult.Error -> {
                    dialogManager.hideLoadingWithDelay()
                    requireView().showFailedSnackbar(it.exception.message.toString())
                }

                is DataResult.Loading -> {
                    dialogManager.showLoading()
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun handleEvent(view: View) {
        binding.btnBack.setOnClickListener {
            if (args.isFromDeeplink) {
                findNavController().navigate(JoinRoomFragmentDirections.actionNavJoinRoomToNavMovies())
            } else {
                findNavController().navigateUp()
            }

        }

        binding.btnJoinRoom.setOnClickListener {
            val roomCode = binding.edtRoomCode.text.toString().trim()

            if (roomCode.isEmpty()) {
                requireView().showFailedSnackbar("Please enter valid room code")
            } else {
                viewModel.checkRoomCodeExist(roomCode)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}