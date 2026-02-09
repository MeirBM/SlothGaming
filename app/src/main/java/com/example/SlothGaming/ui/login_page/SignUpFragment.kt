package com.example.SlothGaming.ui.login_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.SlothGaming.databinding.SignUpLayoutBinding
import com.example.SlothGaming.ui.view_models.SignUpViewModel
import com.example.SlothGaming.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint

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
        binding.signupBtn.setOnClickListener {
            val firstname = binding.etFirstName.text.toString().trim()
            val lastname = binding.etLastName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val phone = binding.etPhoneNumber.text.toString().trim()
            val password = binding.etPassword.text.toString()

            if (firstname.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.signUpUser(firstname,lastname,email,phone,password)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}