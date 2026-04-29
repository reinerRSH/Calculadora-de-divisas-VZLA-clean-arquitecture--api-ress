package com.example.dolarcotizacion.model.data

import com.google.gson.annotations.SerializedName

data class MonedaResponse(
    val nombre: String,
    val promedio: Float?,
    val venta: Float?,
    @SerializedName("fechaActualizacion")
    val fechaServidor: String? = null
)
