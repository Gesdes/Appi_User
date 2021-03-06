package com.gesdes.android.conductor.polarlocatario.Fragment

import IncidenciasModel
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.gesdes.android.conductor.polarlocatario.Incidenciasadapter

import com.gesdes.android.conductor.polarlocatario.R


import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_incidencias.view.*
import org.json.JSONException
import org.json.JSONObject

import java.util.*

lateinit var mRecyclerView : RecyclerView
val mAdapter : Incidenciasadapter = Incidenciasadapter()



class Incidencias : Fragment() {

    var listGuias= ArrayList<IncidenciasModel>()
    var URL:String=""
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

URL += getString(R.string.URL) + "ObtenerPedidosPorPkTienda"
        getGuiasSocio()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView =inflater.inflate(R.layout.fragment_incidencias, container, false)



        val context = this

        mRecyclerView = rootView.listaGuias as RecyclerView
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        mAdapter.RecyclerAdapter(listGuias, requireActivity())
        mRecyclerView.adapter = mAdapter


        return rootView
    }

    companion object {

        @JvmStatic
        fun newInstance() = Incidencias()
    }

    fun getGuiasSocio()
    {

        val preferencias = this.requireActivity().getSharedPreferences("variables", Context.MODE_PRIVATE)
        var pk = preferencias.getString("pk", "")


        val datos = JSONObject()
        try {
            datos.put("PK_TIENDA",pk )

        } catch (e: Exception) {
            e.printStackTrace()
        }
        val requstQueue = Volley.newRequestQueue(activity)

        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, URL, datos,
                object : Response.Listener<JSONObject> {
                    override fun onResponse(response: JSONObject) {

                        try {

                            var mensaje = response.getInt("resultado")


                            if (mensaje == 1) {

                                try {
                                    val guias = response.getJSONArray("pedidos")
                                    listGuias.clear()
                                    for(i in 0..(guias.length()-1)){
                                        var aux = IncidenciasModel()
                                        aux.FOLIO="Repartidor: "+guias.getJSONObject(i).getString("repartidor")
                                        aux.CIUDADANO="Ciudadano: "+guias.getJSONObject(i).getString("cliente")
                                        aux.ESTADO="Estatus: "+guias.getJSONObject(i).getString("estatus")
                                        aux.PK1=guias.getJSONObject(i).getString("pk")
                                        aux.FECHA="FOLIO: "+guias.getJSONObject(i).getString("pk")

                                        listGuias.add(aux)

                                    }

                                    mAdapter.RecyclerAdapter(listGuias, activity!!)
                                    mRecyclerView.adapter = mAdapter
                                    //listaGuias.adapter=guiaAdapter
                                    //listaGuias.setOnItemClickListener(this@ReportesFragment)

                                }catch (es :Exception){
                                }

                            }else{
                            }

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    }
                },
                object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError) {

                    }
                }
        ) {

            //here I want to post data to sever
        }

        val MY_SOCKET_TIMEOUT_MS = 15000
        val maxRetries = 2
        jsonObjectRequest.setRetryPolicy(
                DefaultRetryPolicy(
                        MY_SOCKET_TIMEOUT_MS,
                        maxRetries,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        )

        requstQueue.add(jsonObjectRequest)


    }



}
