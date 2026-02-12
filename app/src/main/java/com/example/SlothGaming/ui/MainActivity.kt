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

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: HomePageViewModel by viewModels()

    // משתנה לשמירת ה-Provider כדי שנוכל להסיר אותו במסכים אחרים
    private var topMenuProvider: MenuProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // הגדרת ה-Navigation Controller
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // מאזין לשינויי מסכים - שולט בנראות של התפריטים
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homePageFragment -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                    showTopMenu(navController)
                }
                R.id.myReviewsFragment -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                    removeTopMenu() // נסיר את התפריט העליון של הבית בפרגמנטים אחרים
                }
                R.id.loginFragment -> {
                    binding.bottomNavigation.visibility =
                        View.VISIBLE
                }
                else -> {
                    // במסך לוגין או ספלאש - נסתיר הכל
                    binding.bottomNavigation.visibility = View.GONE
                    removeTopMenu()
                }
            }
        }

        // הגדרת הלוגיקה של ה-Bottom Navigation
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
                    // מנווט לבית רק אם אנחנו לא שם כבר
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
        if (topMenuProvider != null) return // מונע הוספה כפולה

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