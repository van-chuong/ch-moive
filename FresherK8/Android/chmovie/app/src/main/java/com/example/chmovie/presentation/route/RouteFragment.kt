package com.example.chmovie.presentation.route

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.chmovie.data.models.Media
import com.example.chmovie.data.source.local.PrefManager
import com.example.chmovie.shared.constant.Constant

class RouteFragment : Fragment() {

    private val args: RouteFragmentArgs by navArgs()

    override fun onStart() {
        super.onStart()
        val sessionId = PrefManager.with(requireContext()).getString(Constant.SESSION_KEY, "")

        if (sessionId.isNullOrEmpty()) {
            navigateToLogin()
        } else {
            navigateBasedOnArgs()
        }
    }

    private fun navigateToLogin() {
        findNavController().navigate(RouteFragmentDirections.actionNavRouteToNavLogin())
    }

    private fun navigateBasedOnArgs() {
        with(findNavController()) {
            when {
                args.id == 0 -> navigate(RouteFragmentDirections.actionNavRouteToNavMovies())
                args.type == Media.MOVIE -> navigate(RouteFragmentDirections.actionNavRouteToNavMovieDetail(args.id))
                args.type == Media.TV -> navigate(RouteFragmentDirections.actionNavRouteToNavSeriesDetail(args.id))
            }
        }
    }
}