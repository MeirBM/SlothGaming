package com.example.SlothGaming.ui.home_page

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.SlothGaming.R
import com.example.SlothGaming.ui.home_page.adapters.ParentAdapter
import com.example.SlothGaming.data.models.GameItem
import com.example.SlothGaming.data.models.Section
import com.example.SlothGaming.databinding.HomePageLayoutBinding
import com.example.SlothGaming.ui.view_models.HomePageViewModel
import com.example.SlothGaming.ui.view_models.ReviewViewModel
import com.example.SlothGaming.utils.Resource
import com.example.SlothGaming.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class HomePageFragment: Fragment() {

    private var binding : HomePageLayoutBinding by autoCleared()

    private val viewModel : HomePageViewModel by viewModels()

    private val parentAdapter = ParentAdapter(emptyList())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomePageLayoutBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.mainRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = parentAdapter
            setHasFixedSize(true)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.homePageState.collectLatest { resource ->
                    when(resource){
                        is Resource.Success ->
                            resource.data?.let{section ->
                                parentAdapter.updateList(section)
                            }
                        is Resource.Error ->{Toast.makeText(requireContext(),
                            resource.message, Toast.LENGTH_LONG).show()}
                    }

                }
            }
        }


        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.login_icon -> {if(!viewModel.isUserLoggedIn())
                    findNavController().navigate(R.id.action_homePageFragment_to_loginFragment)
                true}
                else -> false
            }
        }

        val menuHost: MenuHost = requireActivity()
        inflateMenu(menuHost)

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun inflateMenu(menuHost: MenuHost){
        menuHost.addMenuProvider(
            object : MenuProvider {

                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.home_top_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.my_reviews -> {
                            if (!viewModel.isUserLoggedIn()) {
                                AlertDialog.Builder(requireContext())
                                    .setMessage("You must log in to use this feature")
                                    .setPositiveButton(getString(R.string.ok),null).show()
                            }else{
                                findNavController().navigate(R.id.action_homePageFragment_to_myReviewsFragment)
                            }
                            true
                        }

                        R.id.sign_out -> {
                            if (!viewModel.isUserLoggedIn()) {
                                AlertDialog.Builder(requireContext())
                                    .setMessage("You must be logged in")
                                    .setPositiveButton(getString(R.string.ok), null)
                                    .show()
                            } else {
                                AlertDialog.Builder(requireContext()).apply {
                                    setTitle("Sign Out")
                                    setMessage("Are you sure you want to sign out?")
                                    setPositiveButton(getString(R.string.yes)) { _,_ ->

                                        viewModel.useSignOut()
                                        Toast.makeText(
                                            requireContext(),
                                            ("Sign Out Successfully"),
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        findNavController().navigate(R.id.action_homePageFragment_to_loginFragment)
                                    }
                                    setNegativeButton(getString(R.string.no)) { _, _ ->
                                    }
                                    show()
                                }
                            }
                            true
                        }

                        else -> false
                    }
                }
            },
            viewLifecycleOwner
        )
    }
}