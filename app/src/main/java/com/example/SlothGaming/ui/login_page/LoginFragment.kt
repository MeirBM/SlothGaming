package com.example.SlothGaming.ui.login_page


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.SlothGaming.R
import com.example.SlothGaming.databinding.LoginLayoutBinding
import com.example.SlothGaming.extensions.startLightingAnimation
import com.example.SlothGaming.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment: Fragment() {

    private var binding : LoginLayoutBinding by autoCleared()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LoginLayoutBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signupnowBtn.setOnClickListener { _ ->
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
        startLightingAnimation(binding.loginBackground)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}