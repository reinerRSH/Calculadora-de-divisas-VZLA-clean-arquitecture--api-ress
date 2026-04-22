package com.example.dolarcotizacion.IU.View

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.dolarcotizacion.R
import com.example.dolarcotizacion.databinding.FragmentPagoMovilBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PagoMovilFragment : Fragment(R.layout.fragment_pago_movil) {

    private var _binding: FragmentPagoMovilBinding? = null
    private val binding get() = _binding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPagoMovilBinding.bind(view)
        //Logica del fragment


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }


}