package com.example.SlothGaming.ui.login_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.SlothGaming.R
import com.example.SlothGaming.databinding.SignUpLayoutBinding
import com.example.SlothGaming.view_models.SignUpViewModel
import com.example.SlothGaming.utils.Error
import com.example.SlothGaming.utils.Loading
import com.example.SlothGaming.utils.Success
import com.example.SlothGaming.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var binding : SignUpLayoutBinding by autoCleared()
    private val viewModel : SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SignUpLayoutBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val anim = AnimationUtils.loadAnimation(requireContext(),R.anim.spinning_for_sloth)

        binding.signupBtn.setOnClickListener {
            val firstname = binding.etFirstName.text.toString().trim()
            val lastname = binding.etLastName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val phone = binding.etPhoneNumber.text.toString().trim()
            val password = binding.etPassword.text.toString()

            viewModel.signUpUser(firstname,lastname,email,phone,password)
        }
        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.signupState.collectLatest {
                    when(it?.status){
                        is Loading -> {
                            binding.loadingProgressRegister.apply{
                                startAnimation(anim)
                                isVisible = true
                               }
                            binding.signupBtn.isEnabled = false
                        }
                        is Success -> {
                            binding.loadingProgressRegister.apply{
                                isVisible = false
                                clearAnimation()}
                            Toast.makeText(requireContext(),getString(R.string.register_successfully),Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
                        }
                        is Error -> {
                            Toast.makeText(requireContext(),it.status.message,Toast.LENGTH_SHORT).show()
                            binding.loadingProgressRegister.apply{
                                isVisible = false
                                clearAnimation()}
                            binding.signupBtn.isEnabled = true
                        }else->null
                    }
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}