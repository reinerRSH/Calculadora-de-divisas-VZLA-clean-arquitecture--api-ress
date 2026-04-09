package com.example.dolarcotizacion.model.repository

import com.example.dolarcotizacion.model.data.MonedaResponse
import com.example.dolarcotizacion.model.network.RetrofitClient
import retrofit2.Response

class MonedaRepository {

    // Instanciamos el servicio que creamos antes
    private val api = RetrofitClient.apiService

    // Esta función va a internet y trae la respuesta
    suspend fun getPreciosDolar(): Response<List<MonedaResponse>> {
        return api.getPromedio()
    }
}