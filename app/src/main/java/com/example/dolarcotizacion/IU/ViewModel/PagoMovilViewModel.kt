package com.example.dolarcotizacion.IU.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.dolarcotizacion.model.Daos.PagoMovilDao
import com.example.dolarcotizacion.model.data.Entitys.PagoMovilEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PagoMovilViewModel @Inject constructor(
    private val daoPagoMovil: PagoMovilDao
) : ViewModel() {


    val listaPagoMovil: LiveData<List<PagoMovilEntity>> = daoPagoMovil.getAllPagoMovil().asLiveData()


    fun guardarpago(nombre: String,banco: String, telefono: String, ci: String) {
        viewModelScope.launch {
            val nuevoPago = PagoMovilEntity(
                nombre = nombre,
                banco = banco,
                telefono = telefono,
                ci = ci)
            daoPagoMovil.insertPago(nuevoPago)
        }
    }

    fun actualizarDatos(datoPGM: PagoMovilEntity) {
        viewModelScope.launch {
            daoPagoMovil.updatePago(datoPGM)
        }
    }


    }
