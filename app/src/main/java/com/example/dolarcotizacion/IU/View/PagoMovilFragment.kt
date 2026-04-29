package com.example.dolarcotizacion.IU.View

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dolarcotizacion.IU.ViewModel.PagoMovilAdapter
import com.example.dolarcotizacion.IU.ViewModel.PagoMovilViewModel
import com.example.dolarcotizacion.R
import com.example.dolarcotizacion.databinding.FragmentPagoMovilBinding
import com.example.dolarcotizacion.model.data.Entitys.PagoMovilEntity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PagoMovilFragment : Fragment(R.layout.fragment_pago_movil) {

    private var _binding: FragmentPagoMovilBinding? = null
    private val binding get() = _binding!!
    private val viewModel : PagoMovilViewModel by viewModels()
    private lateinit var pagoAdapter : PagoMovilAdapter
    private var pagoEditado : PagoMovilEntity? = null





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPagoMovilBinding.bind(view)

        binding.scrollViewRegistro.visibility = View.GONE

        //Logica del fragment

        pagoAdapter = PagoMovilAdapter(emptyList()){editPago -> editarRegistro(editPago)}
        binding.rvPagoM.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = pagoAdapter
        }

        viewModel.listaPagoMovil.observe(viewLifecycleOwner){lista ->
           pagoAdapter.updateList(lista)
        }

        binding.AddPagoM.setOnClickListener {
            binding.scrollViewRegistro.visibility = View.VISIBLE
            binding.AddPagoM.hide()

        }

        val listaBancos = resources.getStringArray(R.array.Bancos)
        val CodigoCedula= resources.getStringArray(R.array.Tipo_cedula)
        val codigoTelefono = resources.getStringArray(R.array.Codigo_telefono)
        val adapterBanco = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, listaBancos)
        val adapterCedulas = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, CodigoCedula)
        val adapterTelefono = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, codigoTelefono)

        binding.spinnerBanco.setAdapter(adapterBanco)
        binding.spinnerTipoCedula.setAdapter(adapterCedulas)
        binding.spinnerOperadora.setAdapter(adapterTelefono)




        binding.btGuardarDatos.setOnClickListener {
            if (validarCampos()) {
                val nombre = binding.etNombre.text.toString().trim()
                val ci = binding.etCedula.text.toString().trim()
                val banco = binding.spinnerBanco.text.toString().trim()
                val telefono = binding.etTelefono.text.toString().trim()

                val cedulaCodigo = binding.spinnerTipoCedula.text.toString()
                val operadora = binding.spinnerOperadora.text.toString()

                val cedulaFinal = "$cedulaCodigo-$ci"
                val telefonoFinal = "$operadora-$telefono"

                pagoEditado?.let { actual ->
                    // Actualizar registro existente
                    val pagoActualizado = actual.copy(
                        nombre = nombre,
                        banco = banco,
                        telefono = telefonoFinal,
                        ci = cedulaFinal
                    )
                    viewModel.actualizarDatos(pagoActualizado)
                } ?: run {
                    // Nuevo registro
                    viewModel.guardarpago(nombre, banco, telefonoFinal, cedulaFinal)
                }

                finalizarEdicion()
            }
        }
    }

    private fun validarCampos(): Boolean {
        val camposAValidar = listOf(
            binding.etNombre to "Nombre requerido",
            binding.etCedula to "Cédula requerida",
            binding.etTelefono to "Teléfono requerido",
            binding.spinnerBanco to "Seleccione un banco",
            binding.spinnerTipoCedula to "Requerido",
            binding.spinnerOperadora to "Requerido"
        )

        var esValido = true

        camposAValidar.forEach { (campo, mensaje) ->
            if (campo.text.isNullOrBlank()) {
                campo.error = mensaje
                esValido = false
            }
        }

        return esValido
    }

    private fun finalizarEdicion() {
        limpiarCampos()
        binding.scrollViewRegistro.visibility = View.GONE
        binding.AddPagoM.show()
        binding.root.clearFocus()
    }

    fun limpiarCampos() {
        binding.apply {
            etNombre.text?.clear()
            etCedula.text?.clear()
            etTelefono.text?.clear()
            spinnerBanco.text.clear()
            spinnerTipoCedula.text.clear()
            spinnerOperadora.text.clear()

            etNombre.error = null
            etCedula.error = null
            etTelefono.error = null
            spinnerBanco.error = null
        }
        pagoEditado = null
    }

    fun editarRegistro(editRegistro: PagoMovilEntity) {
        pagoEditado = editRegistro

        // Llenar los campos con los datos existentes
        binding.etNombre.setText(editRegistro.nombre)
        binding.spinnerBanco.setText(editRegistro.banco, false)

        val partesCi = editRegistro.ci.split("-")
        if (partesCi.size > 1) {
            binding.spinnerTipoCedula.setText(partesCi[0], false)
            binding.etCedula.setText(partesCi[1])
        } else {
            binding.etCedula.setText(editRegistro.ci)
        }

        val partesTelf = editRegistro.telefono.split("-")
        if (partesTelf.size > 1) {
            binding.spinnerOperadora.setText(partesTelf[0], false)
            binding.etTelefono.setText(partesTelf[1])
        } else {
            binding.etTelefono.setText(editRegistro.telefono)
        }

        binding.scrollViewRegistro.visibility = View.VISIBLE
        binding.AddPagoM.hide()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }


}