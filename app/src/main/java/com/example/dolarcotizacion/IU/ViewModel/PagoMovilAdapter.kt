package com.example.dolarcotizacion.IU.ViewModel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dolarcotizacion.R
import com.example.dolarcotizacion.model.data.Entitys.PagoMovilEntity

class PagoMovilAdapter(private var listaPagoMovil: List<PagoMovilEntity> = emptyList(),
private val onEditPagoMovil: (PagoMovilEntity) -> Unit
) :
RecyclerView.Adapter<PagoMovilAdapter.PagoMovilViewHolder>() {

    class PagoMovilViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.NombreRegistroPGM)
        val banco: TextView = view.findViewById(R.id.BancoRegistroPGM)
        val telefono: TextView = view.findViewById(R.id.telefonoRegistroPGM)
        val cedula: TextView = view.findViewById(R.id.cedulaRegistroPGM)
        val btEditar: Button = view.findViewById(R.id.EditarDatos)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagoMovilViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.targeta_datos_pgm, parent, false)
        return PagoMovilViewHolder(view)


    }

    override fun onBindViewHolder(holder: PagoMovilViewHolder, position: Int) {
        val pago = listaPagoMovil[position]
        holder.nombre.text = pago.nombre
        holder.banco.text = pago.banco
        holder.telefono.text = pago.telefono
        holder.cedula.text = pago.ci

        holder.btEditar.setOnClickListener {
            onEditPagoMovil(pago)
        }

    }

    override fun getItemCount(): Int = listaPagoMovil.size

    fun updateList(nuevalista: List<PagoMovilEntity>) {

        this.listaPagoMovil = nuevalista
        notifyDataSetChanged()

    }




}