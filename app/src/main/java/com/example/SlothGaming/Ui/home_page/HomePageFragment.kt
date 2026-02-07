package com.example.SlothGaming.Ui.home_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.SlothGaming.R
import com.example.SlothGaming.databinding.HomePageLayoutBinding
import com.example.SlothGaming.utils.autoCleared

class HomePageFragment: Fragment() {

    private var binding : HomePageLayoutBinding by autoCleared()


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

        val menu : MenuHost = requireActivity()
        menu.addMenuProvider(object : MenuProvider{
            override fun onCreateMenu(
                menu: Menu,
                menuInflater: MenuInflater
            ) {
                menuInflater.inflate(R.menu.home_page_bottom_bar,menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
              return when (menuItem.itemId){
                  R.id.login_icon -> {findNavController().navigate(R.id.action_homePageFragment_to_loginFragment)
                         return true}
                  else ->false
              }
            }
        },viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}