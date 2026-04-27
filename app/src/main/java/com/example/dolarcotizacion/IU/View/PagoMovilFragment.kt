package com.example.dolarcotizacion.IU.View

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.dolarcotizacion.IU.ViewModel.PagoMovilViewModel
import com.example.dolarcotizacion.R
import com.example.dolarcotizacion.databinding.FragmentPagoMovilBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PagoMovilFragment : Fragment(R.layout.fragment_pago_movil) {

    private var _binding: FragmentPagoMovilBinding? = null
    private val binding get() = _binding!!
    private val viewModel : PagoMovilViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPagoMovilBinding.bind(view)

        binding.scrollViewRegistro.visibility = View.GONE

        //Logica del fragment

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
            val nombre = binding.etNombre.text.toString()
            val telefono = binding.etTelefono.text.toString()
            val ci = binding.etCedula.text.toString()
            val banco = binding.spinnerBanco.text
            val cedulaCodigo = binding.spinnerTipoCedula.text.toString()
            val operadora = binding.spinnerOperadora.text.toString()


            val cedulFinal = "$cedulaCodigo- $ci"
            val telefonoFinal = "$operadora$telefono"




            if ( nombre.isNotEmpty() && telefono.isNotEmpty() && ci.isNotEmpty() && banco.isNotEmpty()) {
                viewModel.guardarpago(nombre,banco.toString(),telefonoFinal,cedulFinal)
                limpiarCampos()
                binding.cardRegistroPM.visibility = View.GONE
                binding.AddPagoM.show()


            }else {
                binding.etTelefono.error = "Campo requerido"
                binding.etCedula.error = "Campo requerido"
            
            }

            viewModel.listaPagoMovil.observe(viewLifecycleOwner) { pagos ->
                Log.d("PagoMovilFragment", "Lista de pagos: ${pagos.indices}")


            }
        }


    }

    fun limpiarCampos() {

        binding.etTelefono.text?.clear()
        binding.etCedula.text?.clear()
        binding.spinnerBanco.text.clear()
        binding.spinnerTipoCedula.text.clear()
        binding.spinnerOperadora.text.clear()
        binding.etNombre.text?.clear()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }


}