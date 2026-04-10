package com.example.dolarcotizacion.IU.View

import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.dolarcotizacion.IU.ViewModel.MonedaViewModel
import com.example.dolarcotizacion.R
import com.example.dolarcotizacion.databinding.ActivityMainBinding
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.collections.get

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private var tasaBcv = 0.0f
    private var tasaParalelo = 0.0f


    private val viewModel: MonedaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.fetchPrecios()
        setupObservers()
        TextoEditable()

        binding.switchDivisa.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                binding.etCalculator.hint = "Bs. 0,00"
                binding.etCalculator.text.clear()


            } else {
                binding.etCalculator.hint = "1,00 $"
                binding.etCalculator.text.clear()
            }
            ejecutarCalculoSegunEstado()
        }





        binding.btnUpdate.setOnClickListener {
            Toast.makeText(this, "Actualizando...", Toast.LENGTH_SHORT).show()

            viewModel.fetchPrecios()


        }


    }

    fun setupObservers() {


        viewModel.monedaData.observe(this) { lista ->
            lista?.let {


                if (!it.isNullOrEmpty()) {

                    // . Buscamos la fecha de la primera moneda
                    val fechaIso = it[0].fechaServidor


                    if (!fechaIso.isNullOrEmpty()) {
                        try {

                            val fechaUtc = ZonedDateTime.parse(fechaIso)
                            val fechaLocal = fechaUtc.withZoneSameInstant(ZoneId.systemDefault())
                            val formatter =
                                DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale("es", "ES"))

                            binding.tvFecha.text = "Actualizado: ${fechaLocal.format(formatter)}"

                        } catch (e: Exception) {
                            // Si el ZonedDateTime falla (por formato), usamos el split de emergencia
                            try {
                                val soloFecha = fechaIso.split("T")[0]
                                binding.tvFecha.text = "Fecha: $soloFecha"
                            } catch (splitError: Exception) {
                                binding.tvFecha.text = "Actualizado: Reciente"
                            }
                            android.util.Log.e("FECHA_ERROR", "Error: ${e.message}")
                        }
                    } else {
                        // Si la fecha viene null de la API, ponemos un texto por defecto
                        binding.tvFecha.text = "Fecha no disponible"
                    }
                }




                it.forEach { moneda ->
                    // Log para ver en el Logcat si los datos llegan
                    android.util.Log.d(
                        "API_DEBUG", "Moneda: ${moneda.nombre} - Valor: ${moneda.promedio}"
                    )

                    when (moneda.nombre) {
                        "Dólar" -> {
                            tasaBcv = moneda.promedio


                            binding.tvResultBcv.text =
                                "Bs. ${String.format(Locale("es", "ES"), "%.2f", moneda.promedio)}"
                        }


                        "Paralelo" -> {
                            tasaParalelo = moneda.promedio

                            binding.tvResultParalelo.text =
                                "Bs. ${String.format(Locale("es", "ES"), "%.2f", moneda.promedio)}"
                        }
                    }
                }

                // Formatear la fecha del primer elemento
                if (it.isNotEmpty()) {
                    val fechaRaw = it[0].fechaActualizacion
                    binding.tvFecha.text = "Actualizado: ${fechaRaw.split("T")[0]}"
                }
            }
        }
    }

    private fun TextoEditable() {

        binding.etCalculator.addTextChangedListener(object : android.text.TextWatcher {

            private var current = ""
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {

                val input = s.toString()
                if (input != current) {
                    binding.etCalculator.removeTextChangedListener(this)
                    
                    // Limpiamos todo lo que no sea números
                    val cleanString = input.replace(Regex("[^\\d]"), "")

                    if (cleanString.isNotEmpty()) {
                        try {
                            val parsed = cleanString.toDouble()
                            
                            // Configuramos el formato con punto para miles y coma para decimales
                            val symbols = DecimalFormatSymbols(Locale("es", "ES"))
                            symbols.groupingSeparator = '.'
                            symbols.decimalSeparator = ','
                            val decimalFormat = DecimalFormat("#,##0.00", symbols)
                            
                            val formatted = decimalFormat.format(parsed / 100)

                            current = formatted
                            binding.etCalculator.setText(formatted)
                            binding.etCalculator.setSelection(formatted.length)
                        } catch (e: Exception) {
                            android.util.Log.e("CALC_ERROR", "Error al formatear: ${e.message}")
                        }
                    } else {
                        current = ""
                        binding.etCalculator.setText("")
                    }
                    
                    binding.etCalculator.addTextChangedListener(this)
                    ejecutarCalculoSegunEstado()
                }


            }
        })

    }

    fun calcular(texto: String) {

        when {
            texto.isEmpty() -> {

                limpiar()

            }

            texto.isNotEmpty() -> {
                try {
                    val dolares = texto.toFloat()

                    // Calculamos ambos valores
                    val bsBcv = dolares * tasaBcv
                    val bsParalelo = dolares * tasaParalelo

                    // Mostramos resultados con formato regional
                    binding.tvBcv.text = "Bs. ${String.format(Locale("es", "ES"), "%.2f", bsBcv)}"
                    binding.tvParalelo.text = "Bs. ${String.format(Locale("es", "ES"), "%.2f", bsParalelo)}"


                } catch (e: Exception) {
                    limpiar()

                }
            }

            else -> {
                limpiar()
            }
        }


    }

    fun limpiar() {
        viewModel.monedaData.value?.let { lista ->
            lista.forEach { moneda ->
                when (moneda.nombre) {
                    "Dólar" -> {
                        binding.tvBcv.text = "Bs.${String.format(Locale("es", "ES"), "%.2f", moneda.promedio)}"
                    }

                    "Paralelo" -> {
                        binding.tvParalelo.text = "Bs.${String.format(Locale("es", "ES"), "%.2f", moneda.promedio)}"
                    }
                }
            }
        }
    }

    fun calcularDolar(texto: String) {
        when {
            texto.isEmpty() -> {

                limpiar()

            }

            texto.isNotEmpty() -> {
                try {
                    val dolares = texto.toFloat()

                    // Calculamos ambos valores
                    val bsBcv = dolares / tasaBcv
                    val bsParalelo = dolares / tasaParalelo

                    // Mostramos resultados
                    binding.tvBcv.text = "$ ${String.format(Locale("es", "ES"), "%.2f", bsBcv)}"
                    binding.tvParalelo.text = "$ ${String.format(Locale("es", "ES"), "%.2f", bsParalelo)}"


                } catch (e: Exception) {
                    limpiar()

                }
            }

            else -> {
                limpiar()
            }
        }


    }

    private fun ejecutarCalculoSegunEstado() {
        // Quitamos los puntos y cambiamos coma por punto para que float pueda entenderlo
        val rawText = binding.etCalculator.text.toString()
        val textoLimpio = rawText.replace(".", "").replace(",", ".")

        if (binding.switchDivisa.isChecked) {
            calcularDolar(textoLimpio)
        } else {
            calcular(textoLimpio)
        }
    }

}
