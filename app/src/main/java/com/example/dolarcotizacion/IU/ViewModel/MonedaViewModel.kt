package com.example.dolarcotizacion.IU.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dolarcotizacion.model.data.MonedaResponse
import com.example.dolarcotizacion.model.repository.MonedaRepository
import kotlinx.coroutines.launch

class MonedaViewModel: ViewModel() {

    private val repository = MonedaRepository()

    // Aquí guardaremos la respuesta de la API
    val monedaData = MutableLiveData<List<MonedaResponse>?>()

    // Para mostrar un error en la interfaz
    val error = MutableLiveData<String>()

    // Esta es la función que llamarás desde el botón
    fun fetchPrecios() {
        // Iniciamos la corrutina en el hilo de fondo
        viewModelScope.launch {
            try {
                // Llamamos al repositorio
                val response = repository.getPreciosDolar()



                if (response.isSuccessful) {
                    // Si la API responde, metemos el objeto en el LiveData
                    monedaData.postValue(response.body())

                } else {
                    // Si el servidor falla (ej. error 500 o 404)
                    error.postValue("Error del servidor: ${response.code()}")
                }
            } catch (e: Exception) {
                // Si no hay internet o la URL está mal escrita
                error.postValue("Error de conexión: ${e.message}")
            }
        }
    }
}