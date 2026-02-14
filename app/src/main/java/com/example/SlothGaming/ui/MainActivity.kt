package com.example.SlothGaming.ui

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.SlothGaming.R
import com.example.SlothGaming.databinding.ActivityMainBinding
import com.example.SlothGaming.utils.NotificationHelper
import com.example.SlothGaming.view_models.HomePageViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.content.edit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: HomePageViewModel by viewModels()

    // Saving provider for manage it on other fragments
    private var topMenuProvider: MenuProvider? = null

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            NotificationHelper.scheduleReminder(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //file for saving preference status.
        val pref = getSharedPreferences("SlothPref",MODE_PRIVATE)
        val seenWelcome = pref.getBoolean("seenWelcome",false)

        //Welcome message for first time
        if(!viewModel.isUserLoggedIn() && !seenWelcome){
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.welcome_title))
                .setMessage(getString(R.string.welcome_message))
                .setPositiveButton(getString(R.string.ok)) { _, _ ->
                    pref.edit { putBoolean("seenWelcome", true) }} // Change preference status to True.
                .show()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configure nav controller
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Listening to changes -> Remove / keep menu
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homePageFragment -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                    showTopMenu(navController)
                    requestNotificationPermission()
                }
                R.id.myReviewsFragment -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                    removeTopMenu()
                }
                R.id.loginFragment -> {
                    binding.bottomNavigation.visibility =
                        View.VISIBLE
                }
                else -> {
                    // if not said -> remove
                    binding.bottomNavigation.visibility = View.GONE
                    removeTopMenu()
                }
            }
        }

        // Bottom navigation
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.login_icon -> {
                    if (!viewModel.isUserLoggedIn()) {
                        navController.navigate(R.id.loginFragment)
                    } else {
                        Toast.makeText(this, getString(R.string.already_logged_in), Toast.LENGTH_SHORT).show()
                    }
                    true
                }
                R.id.search_games -> {
                    navController.navigate(R.id.action_homePageFragment_to_searchFragment)
                    true
                }
                R.id.homePage_icon -> {
                    // Only if current dest != home
                    if (navController.currentDestination?.id != R.id.homePageFragment) {
                        navController.navigate(R.id.homePageFragment)
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun showTopMenu(navController: NavController) {
        if (topMenuProvider != null) return // Prevent double add

        topMenuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.home_top_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.my_reviews -> {
                        if (!viewModel.isUserLoggedIn()) {
                            showLoginRequiredDialog()
                        } else {
                            navController.navigate(R.id.action_homePageFragment_to_myReviewsFragment)
                        }
                        true
                    }
                    R.id.sign_out -> {
                        if (!viewModel.isUserLoggedIn()) {
                            Toast.makeText(this@MainActivity, getString(R.string.not_logged_in), Toast.LENGTH_SHORT).show()
                        } else {
                            showSignOutDialog(navController)
                        }
                        true
                    }
                    else -> false
                }
            }
        }

        topMenuProvider?.let {
            addMenuProvider(it, this, Lifecycle.State.RESUMED)
        }
    }

    private fun removeTopMenu() {
        topMenuProvider?.let {
            removeMenuProvider(it)
            topMenuProvider = null
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                NotificationHelper.scheduleReminder(this)
            } else {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            NotificationHelper.scheduleReminder(this)
        }
    }

    private fun showLoginRequiredDialog() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.must_log_in))
            .setPositiveButton(getString(R.string.ok), null)
            .show()
    }

    private fun showSignOutDialog(navController: NavController) {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.sign_out))
            setMessage(getString(R.string.sign_out_confirmation))
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.useSignOut()
                Toast.makeText(this@MainActivity, getString(R.string.sign_out_success), Toast.LENGTH_SHORT).show()
                navController.navigate(R.id.loginFragment)
            }
            setNegativeButton(getString(R.string.no), null)
            show()
        }
    }
}