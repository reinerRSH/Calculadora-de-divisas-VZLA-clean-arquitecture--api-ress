package com.example.dolarcotizacion.model.network

import com.example.dolarcotizacion.model.data.MonedaResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("v1/dolares")
    suspend fun getPromedio(): Response<List<MonedaResponse>>
}