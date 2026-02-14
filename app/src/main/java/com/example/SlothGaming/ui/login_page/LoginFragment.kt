package com.example.SlothGaming.ui.login_page


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.SlothGaming.R
import com.example.SlothGaming.databinding.LoginLayoutBinding
import com.example.SlothGaming.extensions.startLightingAnimation
import com.example.SlothGaming.view_models.LoginViewModel
import com.example.SlothGaming.utils.Error
import com.example.SlothGaming.utils.Loading
import com.example.SlothGaming.utils.Success
import com.example.SlothGaming.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment: Fragment() {
    private val viewModel: LoginViewModel by viewModels()
    private var binding: LoginLayoutBinding by autoCleared()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LoginLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signupnowBtn.setOnClickListener { _ ->
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
        startLightingAnimation(binding.loginBackground)


        binding.signInButton.setOnClickListener { viewModel.signInUser(
            binding.emailSignIn.text.toString().trim(),
            binding.passwordSignIn.text.toString(),
            getString(R.string.error_fill_all_fields))}


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginStatus.collectLatest {
                    when (it?.status) {
                        is Loading -> {
                            binding.loginProgress.isVisible = true
                        }

                        is Success -> {

                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.sign_in_successful),
                                    Toast.LENGTH_SHORT
                                ).show()
                                findNavController().navigate(R.id.action_loginFragment_to_homePageFragment)


                        }

                        is Error-> {
                            Toast.makeText(
                                requireContext(),
                                it.status.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.loginProgress.isVisible = false
                        }else -> null

                    }
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentUser.collectLatest {
                    when (it?.status) {
                        is Loading -> {
                            binding.loginProgress.isVisible = true
                        }

                        is Success -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.welcome),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is Error -> {
                            Toast.makeText(
                                requireContext(),
                                it.status.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.loginProgress.isVisible = false
                        }else -> null

                    }
                }
            }
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
    }
}