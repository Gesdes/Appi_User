package com.gesdes.android.conductor.polarlocatario
import IncidenciasModel
import android.os.Bundle
import android.content.Context
import android.content.Intent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.gesdes.android.conductor.polarlocatario.Fragment.Pedidosdetalle

import java.util.*


class Pedidos() : RecyclerView.Adapter<Pedidos.ViewHolder>() {

    var incidenciasModel: MutableList<IncidenciasModel>  = ArrayList()
    lateinit var context:Context



    fun RecyclerAdapter(guiasList : MutableList<IncidenciasModel>, context: Context){
        this.incidenciasModel = guiasList
        this.context = context
    }

    override fun onBindViewHolder(holder: Pedidos.ViewHolder, position: Int) {
        val item = incidenciasModel.get(position)
        holder.bind(item, context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return Pedidos.ViewHolder(layoutInflater.inflate(R.layout.incidencias_item, parent, false))
    }

    override fun getItemCount(): Int {
        return incidenciasModel.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rowView = view

        val tvFolio=rowView.findViewById<TextView>(R.id.tvFolio)as TextView
        val tvciudadano=rowView.findViewById<TextView>(R.id.tvciudadano)
        val tvestatus=rowView.findViewById<TextView>(R.id.tvestatus)
        val tvfecha=rowView.findViewById<TextView>(R.id.tvfecha)


        fun bind(incidencia:IncidenciasModel, context: Context){
            val incidencia=incidencia as IncidenciasModel
            tvFolio.text = incidencia.FOLIO
            tvciudadano.text=incidencia.CIUDADANO
            tvestatus.text=incidencia.ESTADO
            tvfecha.text=incidencia.FECHA

            itemView.setOnClickListener(View.OnClickListener {
                    val intent = Intent(context, Pedidosdetalle::class.java)
                // start your next activity
                var bund=Bundle()
                bund.putString("pkcorrida","DFDS")
                intent.putExtra("PK", incidencia.PK1)

                startActivity(context,intent,bund)
            })

        }

    }


}