package com.example.SlothGaming.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.SlothGaming.R
import com.example.SlothGaming.databinding.ActivityMainBinding
import com.example.SlothGaming.view_models.HomePageViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.content.edit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: HomePageViewModel by viewModels()

    // Saving provider for manage it on other fragments
    private var topMenuProvider: MenuProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //file for saving preference status.
        val pref = getSharedPreferences("SlothPref",MODE_PRIVATE)
        val seenWelcome = pref.getBoolean("seenWelcome",false)

        //Welcome message for first time
        if(!viewModel.isUserLoggedIn() && !seenWelcome){
            AlertDialog.Builder(this)
                .setTitle("Welcome To Sloth Gaming"+"\n")
                .setMessage("Hello Sloth Gamer!"+"\n"+"Here, you can get all the information about the latest games, top rated games and more."+"\n"+"Additionaly you can write your own reviews for any game and any platform with your own rating!"+"\n"+"Have fun and stay Sloth.")
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
                        Toast.makeText(this, "You're already logged in", Toast.LENGTH_SHORT).show()
                    }
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
                            navController.navigate(R.id.myReviewsFragment)
                        }
                        true
                    }
                    R.id.sign_out -> {
                        if (!viewModel.isUserLoggedIn()) {
                            Toast.makeText(this@MainActivity, "You are not logged in", Toast.LENGTH_SHORT).show()
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

    private fun showLoginRequiredDialog() {
        AlertDialog.Builder(this)
            .setMessage("You must log in to use this feature")
            .setPositiveButton(getString(R.string.ok), null)
            .show()
    }

    private fun showSignOutDialog(navController: NavController) {
        AlertDialog.Builder(this).apply {
            setTitle("Sign Out")
            setMessage("Are you sure you want to sign out?")
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.useSignOut()
                Toast.makeText(this@MainActivity, "Sign Out Successfully", Toast.LENGTH_SHORT).show()
                navController.navigate(R.id.loginFragment)
            }
            setNegativeButton(getString(R.string.no), null)
            show()
        }
    }
}