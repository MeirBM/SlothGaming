package com.example.SlothGaming.Ui.login_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.SlothGaming.databinding.SignUpLayoutBinding
import com.example.SlothGaming.utils.autoCleared

class SignUpFragment : Fragment() {

    private var binding : SignUpLayoutBinding by autoCleared()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SignUpLayoutBinding.inflate(inflater,container,false)
        return binding.root
    }
}