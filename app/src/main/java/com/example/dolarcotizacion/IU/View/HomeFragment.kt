package com.example.dolarcotizacion.IU.View

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.dolarcotizacion.IU.ViewModel.MonedaViewModel
import com.example.dolarcotizacion.R
import com.example.dolarcotizacion.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var tasaBcv = 0.0f
    private var tasaParalelo = 0.0f

    private val viewModel: MonedaViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        // 1. Iniciar la carga de datos
        viewModel.fetchPrecios()

        // 2. Configurar vistas y observadores
        setupObservers()
        textoEditable()

        // 3. Configurar clics
        binding.btnShare.setOnClickListener {
            // Pasamos la vista raíz del fragmento
            shareImage(requireView())
        }

        binding.switchDivisa.setOnCheckedChangeListener { _, isChecked ->
            // En Fragment usamos ContextCompat para los colores
            val colorOn = ContextCompat.getColorStateList(requireContext(), R.color.colorThumb)
            val colorOff = ContextCompat.getColorStateList(requireContext(), R.color.colorThumbOff)

            binding.switchDivisa.thumbTintList = if (isChecked) colorOn else colorOff

            if (isChecked) {
                binding.etCalculator.hint = "Bs. 0,00"
                binding.etCalculator.text.clear()
            } else {
                binding.etCalculator.hint = "1,00 $"
                binding.etCalculator.text.clear()
            }
            ejecutarCalculoSegunEstado()
        }
    }

    private fun setupObservers() {
        // En Fragments usamos viewLifecycleOwner en lugar de "this"
        viewModel.monedaData.observe(viewLifecycleOwner) { lista ->
            lista?.let { it ->
                if (it.isNotEmpty()) {
                    val fechaIso = it[0].fechaServidor
                    if (!fechaIso.isNullOrEmpty()) {
                        try {
                            val fechaUtc = ZonedDateTime.parse(fechaIso)
                            val fechaLocal = fechaUtc.withZoneSameInstant(ZoneId.systemDefault())
                            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy",
                                Locale("es", "ES")
                            )
                            binding.tvFecha.text = "Actualizado: ${fechaLocal.format(formatter)}"
                        } catch (e: Exception) {
                            try {
                                val soloFecha = fechaIso.split("T")[0]
                                binding.tvFecha.text = "Fecha: $soloFecha"
                            } catch (splitError: Exception) {
                                binding.tvFecha.text = "Actualizado: Reciente"
                            }
                        }
                    } else {
                        binding.tvFecha.text = "Fecha no disponible"
                    }
                }

                it.forEach { moneda ->
                    when (moneda.nombre) {
                        "Dólar" -> {
                            tasaBcv = moneda.valor
                            binding.tvBcv.text = "Bs. ${String.format(Locale("es", "ES"), "%.2f", moneda.valor)}"
                        }
                        "Paralelo" -> {
                            tasaParalelo = moneda.valor
                            binding.tvParalelo.text = "Bs. ${String.format(Locale("es", "ES"), "%.2f", moneda.valor)}"
                        }
                    }
                }
            }
        }
    }

    private fun textoEditable() {
        binding.etCalculator.addTextChangedListener(object : android.text.TextWatcher {
            private var current = ""
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                if (input != current) {
                    binding.etCalculator.removeTextChangedListener(this)
                    val cleanString = input.replace(Regex("[^\\d]"), "")
                    if (cleanString.isNotEmpty()) {
                        try {
                            val parsed = cleanString.toDouble()
                            val symbols = DecimalFormatSymbols(Locale("es", "ES"))
                            symbols.groupingSeparator = '.'
                            symbols.decimalSeparator = ','
                            val decimalFormat = DecimalFormat("#,##0.00", symbols)
                            val formatted = decimalFormat.format(parsed / 100)
                            current = formatted
                            binding.etCalculator.setText(formatted)
                            binding.etCalculator.setSelection(formatted.length)
                        } catch (e: Exception) { }
                    } else {
                        current = ""
                        binding.etCalculator.setText("")
                    }
                    binding.etCalculator.addTextChangedListener(this)
                    ejecutarCalculoSegunEstado()
                }
            }
        })
        if (binding.etCalculator.text.isEmpty()) limpiar()
    }

    private fun ejecutarCalculoSegunEstado() {
        val rawText = binding.etCalculator.text.toString()
        val textoLimpio = rawText.replace(".", "").replace(",", ".")
        if (binding.switchDivisa.isChecked) {
            calcularDolar(textoLimpio)
        } else {
            calcular(textoLimpio)
        }
    }

    fun calcular(texto: String) {
        if (texto.isEmpty()) {
            limpiar()
        } else {
            try {
                val dolares = texto.toFloat()
                binding.tvBcv.text = "Bs. ${String.format(Locale("es", "ES"), "%.2f", dolares * tasaBcv)}"
                binding.tvParalelo.text = "Bs. ${String.format(Locale("es", "ES"), "%.2f", dolares * tasaParalelo)}"
            } catch (e: Exception) { limpiar() }
        }
    }

    fun calcularDolar(texto: String) {
        if (texto.isEmpty()) {
            limpiar()
        } else {
            try {
                val bolivares = texto.toFloat()
                binding.tvBcv.text = "$ ${String.format(Locale("es", "ES"), "%.2f", bolivares / tasaBcv)}"
                binding.tvParalelo.text = "$ ${String.format(Locale("es", "ES"), "%.2f", bolivares / tasaParalelo)}"
            } catch (e: Exception) { limpiar() }
        }
    }

    fun limpiar() {
        viewModel.monedaData.value?.forEach { moneda ->
            when (moneda.nombre) {
                "Dólar" -> binding.tvBcv.text = "Bs.${String.format(Locale("es", "ES"), "%.2f", moneda.valor)}"
                "Paralelo" -> binding.tvParalelo.text = "Bs.${String.format(Locale("es", "ES"), "%.2f", moneda.valor)}"
            }
        }
    }

    private fun shareImage(view: View) {
        try {
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)

            // En fragment usamos requireContext().cacheDir
            val cachePath = File(requireContext().cacheDir, "images")
            cachePath.mkdirs()
            val stream = FileOutputStream("$cachePath/image.png")
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()

            val imagePath = File(requireContext().cacheDir, "images/image.png")
            val contentUri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.fileprovider", imagePath)

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, contentUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(shareIntent, "Compartir captura"))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}