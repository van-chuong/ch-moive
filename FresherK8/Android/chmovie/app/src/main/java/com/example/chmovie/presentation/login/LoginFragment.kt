package com.example.chmovie.presentation.login

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chmovie.databinding.FragmentLoginBinding
import com.example.chmovie.presentation.main.MainActivity
import com.example.chmovie.shared.constant.Constant.SIGNUP_URL
import com.example.chmovie.shared.scheduler.DataResult
import com.example.chmovie.shared.widget.dialogManager.DialogManagerImpl
import com.example.chmovie.shared.widget.dialogManager.hideLoadingWithDelay
import com.example.chmovie.shared.widget.showFailedSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModel()

    private val dialogManager by lazy {
        DialogManagerImpl(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        handleEvent(view)
    }


    private fun initView() {
        viewModel.loginResultLiveData.observe(viewLifecycleOwner) { loginResult ->
            when (loginResult) {
                is DataResult.Success -> {
                    dialogManager.hideLoadingWithDelay()
                    findNavController().navigate(LoginFragmentDirections.actionNavLoginToNavMovies())
                }

                is DataResult.Error -> {
                    dialogManager.hideLoadingWithDelay()
                    loginResult.exception.message?.let {
                        requireView().showFailedSnackbar(it)
                    }
                }

                is DataResult.Loading -> {
                    dialogManager.showLoading()
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            (activity as MainActivity).hideLoading(false)
        }
    }

    @SuppressLint("ClickableViewAccessibility", "QueryPermissionsNeeded")
    private fun handleEvent(view: View) {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })

        binding.btnLogin.setOnClickListener {
            val username = binding.edtUsername.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                requireView().showFailedSnackbar("Please enter username and password")
            } else {
                viewModel.login(username, password)
            }
        }

        binding.btnSignup.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(SIGNUP_URL)

            startActivity(intent)
        }
    }
}