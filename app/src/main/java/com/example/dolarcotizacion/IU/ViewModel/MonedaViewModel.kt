package com.example.dolarcotizacion.IU.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dolarcotizacion.model.data.DolarEntity
import com.example.dolarcotizacion.model.repository.DolarRepository
import com.example.dolarcotizacion.model.data.MonedaResponse
import kotlinx.coroutines.launch

class MonedaViewModel(private val repository: DolarRepository) : ViewModel() {


    // respuesta de la API
    private val _monedaData = MutableLiveData<List<DolarEntity>?>()

    val monedaData: LiveData<List<DolarEntity>?> = _monedaData

    // Aquí guardaremos el error
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun fetchPrecios() {

        viewModelScope.launch {
            try {
                // Llamamos al repositorio
                val resultado = repository.getPreciosDolar()



                if (resultado !=null) {
                    // Si la API responde, metemos el objeto en el LiveData
                    _monedaData.value = resultado

                } else {
                    // Si el servidor falla (ej. error 500 o 404)
                    _error.value = ("no hay datos disponible ( sin internet )")
                }
            } catch (e: Exception) {
                // Si no hay internet o la URL está mal escrita
                _error.value= "Error de conexión: ${e.message}"
            }
        }
    }
}