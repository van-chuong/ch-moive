package com.example.chmovie.presentation.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.chmovie.R
import com.example.chmovie.data.source.local.PrefManager
import com.example.chmovie.databinding.ActivityMainBinding
import com.example.chmovie.shared.constant.Constant
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val navController by lazy {
        findNavController(R.id.nav_host_fragment_content_main)
    }

    private val appBarConfiguration: AppBarConfiguration by lazy {
        AppBarConfiguration.Builder(
            R.id.nav_movies, R.id.nav_series, R.id.nav_explore, R.id.nav_my_favorite_list
        ).setOpenableLayout(binding.drawerLayout).build()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(binding.root)

        // Get the navigation graph
        val navGraph = navController.navInflater.inflate(R.navigation.mobile_navigation)
        val sessionId = PrefManager.with(this).getString(Constant.SESSION_KEY, "")
        if (sessionId.isNullOrEmpty()) {
            navGraph.setStartDestination(R.id.nav_login)
        }
        navController.graph = navGraph

        initView()
        handleEvent()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.bottomNavView, navController)
        NavigationUI.setupWithNavController(binding.navView, navController)

        binding.navView.menu.getItem(0).subMenu?.getItem(0)?.setActionView(R.layout.nav_item_mage)
        binding.navView.menu.getItem(1).setActionView(R.layout.nav_item_mage)

        val txtUsername: TextView = binding.navView.getHeaderView(0).findViewById(R.id.txtUsername)
        txtUsername.text = "@${PrefManager.with(baseContext).getString(Constant.USERNAME_KEY, "")}"
    }

    private fun handleEvent() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            binding.toolbar.title = ""
            binding.toolbar.menu.findItem(R.id.nav_search).isVisible = false

            when (destination.id) {
                R.id.nav_login -> {
                    binding.bottomNavView.visibility = View.GONE
                    binding.toolbar.visibility = View.GONE
                }

                R.id.nav_watch_list -> binding.bottomNavView.visibility = View.GONE
                R.id.nav_join_room -> binding.bottomNavView.visibility = View.GONE
                R.id.nav_movie_detail -> binding.bottomNavView.visibility = View.GONE
                R.id.nav_series_detail -> binding.bottomNavView.visibility = View.GONE
                R.id.nav_person_detail -> binding.bottomNavView.visibility = View.GONE
                R.id.nav_search -> binding.bottomNavView.visibility = View.GONE

                else -> {
                    binding.bottomNavView.visibility = View.VISIBLE
                    binding.toolbar.menu.findItem(R.id.nav_search).isVisible = true
                    binding.toolbar.visibility = View.VISIBLE
                }
            }
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.nav_search -> navController.navigate(R.id.nav_search)
            }
            return@setOnMenuItemClickListener true
        }

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_sign_out -> {
                    PrefManager.with(baseContext).clear()
                    navController.navigate(R.id.nav_login)
                }

                R.id.nav_join_room -> {
                    navController.navigate(R.id.nav_join_room)
                }
            }

            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }
    }

    fun showLoading() {
        binding.loadingLayout.visibility = View.VISIBLE
    }

    fun hideLoading(isError: Boolean) {
        if (isError) {
            binding.errorLayout.visibility = View.VISIBLE
        } else {
            binding.errorLayout.visibility = View.GONE
            binding.include.root.visibility = View.VISIBLE
        }

        lifecycleScope.launch {
            delay(500)
            binding.loadingLayout.visibility = View.GONE
        }
    }

    @SuppressWarnings("ComplexCondition")
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val v = currentFocus
        if (v is EditText) {
            val scoops = IntArray(2)
            v.getLocationOnScreen(scoops)
            val x = event.rawX + v.getLeft() - scoops[0]
            val y = event.rawY + v.getTop() - scoops[1]
            if (event.action == MotionEvent.ACTION_UP &&
                (x < v.getLeft() || x >= v.getRight() || y < v.getTop() || y > v.getBottom())
            ) {
                v.clearFocus()
                hideSoftKeyboard(v)
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun hideSoftKeyboard(view: View) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}