package com.example.chmovie.presentation.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chmovie.R
import com.example.chmovie.databinding.FragmentLoginBinding
import com.example.chmovie.shared.scheduler.DataResult
import com.example.chmovie.shared.widget.CustomProgressDialog
import com.example.chmovie.shared.widget.showFailedSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : Fragment() {
    companion object {
        fun newInstance() = LoginFragment()
    }

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModel()

    private val progressDialog by lazy {
        CustomProgressDialog(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initView()
        handleEvent()
        return root
    }

    private fun initView() {
        viewModel.loginResultLiveData.observe(viewLifecycleOwner) { loginResult ->
            when (loginResult) {
                is DataResult.Success -> {
                    progressDialog.dismiss()
                    navigateToMainFragment()
                }

                is DataResult.Error -> {
                    progressDialog.dismiss()
                    loginResult.exception.message?.let {
                        requireView().showFailedSnackbar(it)
                    }
                }

                is DataResult.Loading -> {}
            }
        }
    }

    private fun handleEvent() {
        // Bắt sự kiện click vào nút đăng nhập
        binding.btnLogin.setOnClickListener {
            val username = binding.edtUsername.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            hiddenKeyBoard()
            if (username.isEmpty() || password.isEmpty()) {
                requireView().showFailedSnackbar("Please enter username and password")
            } else {
                progressDialog.show()
                viewModel.login(username, password)
            }
        }
    }

    private fun hiddenKeyBoard() {
        try {
            val imm =
                ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java)
            imm?.hideSoftInputFromWindow(view?.windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun navigateToMainFragment() {
        val navController = findNavController()
        navController.navigate(R.id.nav_movies)
    }
}