package com.example.koperasi_admin.Menu

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.koperasi_admin.R
import com.example.koperasi_admin.Server
import kotlinx.android.synthetic.main.activity_list_tabungan.*
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.activity_setting.edKasKoperasi
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.TimeUnit

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        loadsetting()

        btSimpan.setOnClickListener {
            simpan()
        }

        btAkhiriPeriode.setOnClickListener {
            akhiri_periode()
        }

    }

    private fun akhiri_periode(){
        val loading = ProgressDialog(this)
        loading.setMessage("Silahkan Tunggu Sebentar")
        loading.show()

        AndroidNetworking.get(Server.AKHIRIPERIODE)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    loading.dismiss()
                    Toast.makeText(applicationContext,response?.getString("message"),
                        Toast.LENGTH_SHORT).show()
                }
                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("ONERROR",anError?.errorDetail?.toString())
                    Toast.makeText(applicationContext,"Connection Failure",
                        Toast.LENGTH_SHORT).show()}
            })
    }

    private fun loadsetting() {
        val loading = ProgressDialog(this)
        loading.setMessage("Memuat data...")
        loading.show()
        AndroidNetworking.post(Server.AMBILSETTING)
            .addBodyParameter("id","1")
            .setPriority(Priority.HIGH)
            .getResponseOnlyFromNetwork()
            .doNotCacheResponse()
            .setMaxAgeCacheControl(0, TimeUnit.SECONDS)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val jsonArray = response?.optJSONArray("result")
                    loading.dismiss()
                    println(response)
                    val jsonObject = jsonArray?.optJSONObject(0)
                    edSimpananWajib.setText(jsonObject!!.getString("simpanan_wajib"))
                    edKasKoperasi.setText(jsonObject!!.getString("kas_koperasi"))
//                    edTabunganWajib.setText(jsonObject!!.getString("tabungan_wajib"))
//                    edIuranSukarela.setText(jsonObject!!.getString("iuran_sukarela"))
                }
                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("ONERROR",anError?.errorDetail?.toString())
                    Toast.makeText(applicationContext,"Connection Failure", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun simpan() {
        val loading = ProgressDialog(this)
        loading.setMessage("Silahkan Tunggu Sebentar")
        loading.show()

        AndroidNetworking.post(Server.SIMPANSETTING)
            .addBodyParameter("simpanan_wajib", edSimpananWajib.text.toString())
//            .addBodyParameter("tabungan_wajib", edTabunganWajib.text.toString())
//            .addBodyParameter("iuran_sukarela", edIuranSukarela.text.toString())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    loading.dismiss()
                    Toast.makeText(applicationContext,response?.getString("message"),
                        Toast.LENGTH_SHORT).show()
                }
                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("ONERROR",anError?.errorDetail?.toString())
                    Toast.makeText(applicationContext,"Connection Failure",
                        Toast.LENGTH_SHORT).show()}
            })
    }

}
