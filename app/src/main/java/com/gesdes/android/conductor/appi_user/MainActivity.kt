package com.gesdes.android.conductor.appi_user

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.gesdes.android.conductor.appi_user.Fragment.Incidencias
import com.gesdes.android.conductor.appi_user.Fragment.Perfil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    var TOKEN: String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationViewinicio)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val vent = Perfil.newInstance()
        openFragment(vent)


    }


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.perfi -> {
                val artistsFragment = Perfil.newInstance()
                openFragment(artistsFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.incidencia -> {
                val inci = Incidencias.newInstance()
                openFragment(inci)
                return@OnNavigationItemSelectedListener true
            }


        }
        false
    }
    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
fun cerrarsesion(view: View){
    val preferencias = this.getSharedPreferences("variables", Context.MODE_PRIVATE)

    val editor = preferencias.edit()
        editor.putString("login", "sesioncerrada")
        editor.commit()
        val intent = Intent(this, Login_activity::class.java)

        // start your next activity
        startActivity(intent)
        this.finish();
    }
    fun agregaToken(){

        FirebaseApp.initializeApp(this)
        TOKEN = FirebaseInstanceId.getInstance().getToken().toString()
        RegisterToken()

    }

    fun RegisterToken() {
        val preferencias = getSharedPreferences("variables", Context.MODE_PRIVATE)
        var use = preferencias.getString("usuario", "")

        val jsonObject: JSONObject? = null
        val datos = JSONObject()
        try {
            datos.put("USUARIO", use)
            datos.put("TOKEN", TOKEN)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val requestQueue = Volley.newRequestQueue(this)

        val jsonObjectRequest = object : JsonObjectRequest(com.android.volley.Request.Method.POST, "https://appi-atah.azurewebsites.net/api/UpdateTokenDriver", datos,
                Response.Listener { },
                Response.ErrorListener { }
        ) {

            //here I want to post data to sever
        }

        val MY_SOCKET_TIMEOUT_MS = 15000
        val maxRetries = 2
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                maxRetries,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        requestQueue.add(jsonObjectRequest)
    }


}
