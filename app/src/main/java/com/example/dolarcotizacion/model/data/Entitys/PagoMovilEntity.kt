package com.example.dolarcotizacion.model.data.Entitys

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pago_movil")
data class PagoMovilEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val banco: String,
    val ci: String,
    val telefono: String
)
