package com.example.dolarcotizacion.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Precio_dolar")
data class DolarEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val valor: Float,
    val fechaServidor: String?
)
