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
import com.example.SlothGaming.view_models.HomePageViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: HomePageViewModel by viewModels()

    // Saving provider for manage it on other fragments
    private var topMenuProvider: MenuProvider? = null// already in activity no need for menuHost

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

        /* Listening to changes -> Remove / keep menu
            based on which page you are positioned show/hide buttons
         */
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homePageFragment -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                    // No reason for home page to be visible on home page ..
                    binding.bottomNavigation.menu.findItem(R.id.homePage_icon)?.isVisible = false

                    // Home page is the only place to preform a search so show it here
                    binding.bottomNavigation.menu.findItem(R.id.search_games)?.isVisible = true
                    binding.bottomNavigation.menu.findItem(R.id.search_games)?.isEnabled = true

                    /* Check if user is logged in,
                    based on status show the "Login button"
                    yes -> don't show
                    no -> show
                     */
                    if(viewModel.isUserLoggedIn()) {
                        showTopMenu(navController)
                        binding.bottomNavigation.menu.findItem(R.id.login_icon)?.isVisible = false
                    }
                    else {
                        binding.bottomNavigation.menu.findItem(R.id.login_icon)?.isVisible = true
                        removeTopMenu()
                    }
                }
                R.id.myReviewsFragment -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                    //show home page button
                    binding.bottomNavigation.menu.findItem(R.id.homePage_icon)?.isVisible = true
                    // hide search button
                    binding.bottomNavigation.menu.findItem(R.id.search_games)?.isVisible = false
                    // reviews has its own top menu implemented in MyReviewsFragment
                    removeTopMenu()
                }
                R.id.loginFragment -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                    //show home page button
                    binding.bottomNavigation.menu.findItem(R.id.homePage_icon)?.isVisible = true
                    //hide search button on login page
                    binding.bottomNavigation.menu.findItem(R.id.search_games)?.isVisible = false
                    // no reason for login button to show on login page
                    binding.bottomNavigation.menu.findItem(R.id.login_icon)?.isVisible = false
                    removeTopMenu()
                }
                R.id.searchFragment ->{
                    // Show home while searching
                    binding.bottomNavigation.visibility = View.VISIBLE

                    binding.bottomNavigation.menu.findItem(R.id.homePage_icon)?.isVisible = true

                    binding.bottomNavigation.menu.findItem(R.id.search_games)?.isVisible = false
                }
                else -> {
                    // All other pages that doesn't require bottom bar
                    binding.bottomNavigation.visibility = View.GONE
                    removeTopMenu()
                }
            }
        }

        // Manage's all the bottom bar actions
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.login_icon -> {
                    if (!viewModel.isUserLoggedIn()) {
                        navController.navigate(R.id.action_homePageFragment_to_loginFragment)
                    }
                    true
                }
                R.id.search_games -> {
                    navController.navigate(R.id.action_homePageFragment_to_searchFragment)
                    true
                }
                R.id.homePage_icon -> {
                    /* Home page has users check from which fragment
                    home page was called preform correct action
                     */
                    when(navController.currentDestination?.id != R.id.homePageFragment){

                        (navController.currentDestination?.id == R.id.myReviewsFragment) ->
                            navController.navigate(R.id.action_myReviewsFragment_to_homePageFragment)

                        (navController.currentDestination?.id == R.id.loginFragment) ->
                        navController.navigate(R.id.action_loginFragment_to_homePageFragment)

                        (navController.currentDestination?.id == R.id.searchFragment) ->
                            navController.navigate(R.id.action_searchFragment_to_homePageFragment)
                        else -> false
                    }
                    true
                }
                else -> false
            }
        }
    }
// Manage Main top menu and who will share and use it
    private fun showTopMenu(navController: NavController) {
        if (topMenuProvider != null) return // Prevent multi layered menu

        //standard menu host
        topMenuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.home_top_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.my_reviews -> {
                        navController.navigate(R.id.action_homePageFragment_to_myReviewsFragment)

                        true
                    }
                    R.id.sign_out -> {
                        showSignOutDialog(navController)
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