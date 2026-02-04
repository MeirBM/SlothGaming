package com.example.SlothGaming.Ui.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.SlothGaming.data.repository.ReviewListRepository
/*instead of using the AndroidViewModel
   we created a factory which allow's as to pass a parameter
   into the viewModel class */
class ReviewViewModelFactory(private val repository : ReviewListRepository):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ReviewViewModel::class.java))
            return ReviewViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}