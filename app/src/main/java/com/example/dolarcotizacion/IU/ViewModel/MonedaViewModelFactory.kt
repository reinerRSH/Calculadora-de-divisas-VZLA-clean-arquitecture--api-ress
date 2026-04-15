package com.example.dolarcotizacion.IU.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dolarcotizacion.model.repository.DolarRepository

class MonedaViewModelFactory(private val repository: DolarRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MonedaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MonedaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
