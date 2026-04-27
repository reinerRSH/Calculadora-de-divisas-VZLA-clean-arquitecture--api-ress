package com.example.dolarcotizacion.model.repository

import com.example.dolarcotizacion.model.Daos.DolarDao
import com.example.dolarcotizacion.model.data.DolarEntity
import com.example.dolarcotizacion.model.network.ApiService
import javax.inject.Inject

class DolarRepository @Inject constructor(private val api: ApiService, private val dao: DolarDao) {


    suspend fun getPreciosDolar(): List<DolarEntity>? {
        return try {
            val response = api.getPromedio()
            val body = response.body()

            if (response.isSuccessful && body != null) {
                val listaLimpia = body.map { dto ->
                    DolarEntity(
                        nombre = dto.nombre,
                        valor = dto.promedio ?: 0f,
                        fechaServidor = dto.fechaServidor ?: "sin fecha"
                    )
                }

                // CORRECCIÓN: Le pasamos el objeto 'it' a la función
                listaLimpia.forEach { dao.insertDolar(it) }

                listaLimpia
            } else {
                // CORRECCIÓN: Devolvemos la lista completa de Room
                dao.obtenerTodosLosDolares()
            }
        } catch (e: Exception) {
            // CORRECCIÓN: Si falla internet, también devolvemos la lista de Room
            dao.obtenerTodosLosDolares()
        }
    }
}
