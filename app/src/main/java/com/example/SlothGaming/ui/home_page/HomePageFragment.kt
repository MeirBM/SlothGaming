package com.example.SlothGaming.ui.home_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.SlothGaming.R
import com.example.SlothGaming.ui.home_page.adapters.ParentAdapter
import com.example.SlothGaming.data.models.GameItem
import com.example.SlothGaming.data.models.Section
import com.example.SlothGaming.databinding.HomePageLayoutBinding
import com.example.SlothGaming.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        val sections = listOf(
            Section(
                "Trending Games",
                listOf(
                    GameItem(1, "Elden Ring", ""),
                    GameItem(1, "Elden Ring", ""),
                    GameItem(1, "Elden Ring", ""),
                    GameItem(2, "Hades II", "")
                )
            ),
            Section(
                "Popular This Month",
                listOf(GameItem(3, "Silksong", ""), GameItem(4, "Stardew Valley", ""))
            ), Section(
                "Trending Games",
                listOf(GameItem(1, "Elden Ring", ""), GameItem(2, "Hades II", ""))
            ), Section(
                "Trending Games",
                listOf(GameItem(1, "Elden Ring", ""), GameItem(2, "Hades II", ""))
            )
        )

        binding.mainRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            // Pass data to the ParentAdapter (using View Binding)
            adapter = ParentAdapter(sections)
            setHasFixedSize(true)
        }

        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.login_icon -> {findNavController().navigate(R.id.action_homePageFragment_to_loginFragment)
                true}
                else -> false
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}