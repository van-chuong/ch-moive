package com.example.chmovie.presentation.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.chmovie.R
import com.example.chmovie.databinding.ActivityMainBinding
import com.example.chmovie.shared.scheduler.DataResult
import com.example.chmovie.shared.widget.showFailedSnackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


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

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(binding.root)

        // Get the navigation graph
        navController.setGraph(R.navigation.mobile_navigation)

        checkPostNotificationPermission()
        initView()
        registerLiveData()
        handleEvent()
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent) {
        if (Intent.ACTION_VIEW == intent.action && intent.data != null) {
            val id = intent.data!!.getQueryParameter("id")
            val type = intent.data!!.getQueryParameter("type")

            if (id != null && type != null) {
                val startDestinationArgs = Bundle()
                startDestinationArgs.putInt("id", id.toInt())
                startDestinationArgs.putString("type", type)
                navController.setGraph(R.navigation.mobile_navigation, startDestinationArgs)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.bottomNavView, navController)
        NavigationUI.setupWithNavController(binding.navView, navController)

        binding.navView.menu.getItem(0).subMenu?.getItem(0)?.setActionView(R.layout.nav_item_mage)
        binding.navView.menu.getItem(1).setActionView(R.layout.nav_item_mage)
    }

    private fun registerLiveData() = with(viewModel) {
        signOutResult.observe(this@MainActivity) {
            when (it) {
                is DataResult.Success -> {
                    navController.navigate(R.id.nav_login)
                }

                is DataResult.Error -> {
                    binding.root.showFailedSnackbar("An error occurred. Please try again later!")
                }

                is DataResult.Loading -> {}
            }
        }
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

                R.id.nav_watch_list, R.id.nav_join_room, R.id.nav_movie_detail, R.id.nav_series_detail, R.id.nav_person_detail,
                R.id.nav_search, R.id.nav_rating_detail -> binding.bottomNavView.visibility = View.GONE

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
                    viewModel.signOut()
                }

                R.id.nav_join_room -> {
                    navController.navigate(R.id.nav_join_room)
                }
            }

            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }

        binding.navView.getHeaderView(0).setOnClickListener {
            navController.navigate(R.id.nav_route)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
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

    private fun checkPostNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 100)
            }
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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        // Handle intent data if activity receives a new intent while already running
        intent?.let {
            handleIntentData(it)
            handleDeepLink(it)
        }

    }

    private fun handleIntentData(intent: Intent) {
        if (intent.hasExtra("id") && intent.hasExtra("type")) {
            val id = intent.getIntExtra("id", 0)
            val type = intent.getStringExtra("type")
            val startDestinationArgs = Bundle()

            startDestinationArgs.putInt("id", id)
            startDestinationArgs.putString("type", type)
            navController.setGraph(R.navigation.mobile_navigation, startDestinationArgs)
        }
    }
}