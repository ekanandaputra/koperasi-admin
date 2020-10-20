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
import kotlinx.android.synthetic.main.activity_info.*
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.TimeUnit

class InfoActivity : AppCompatActivity() {

    var id_user = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val bundle = intent.extras
        id_user = bundle!!.getString("id_user").toString()
        loadinfo()
    }

    private fun loadinfo(){
        val localeID = Locale("in", "ID")
        val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
        val loading = ProgressDialog(this)
        loading.setMessage("Memuat data...")
        loading.show()
        AndroidNetworking.post(Server.AMBILINFO)
            .addBodyParameter("id_user", id_user)
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
                    tvTabungan.text = (formatRupiah.format(Integer.parseInt(jsonObject!!.getString("tabungan")))).toString()
                    tvTotalPeminjaman.text = (formatRupiah.format(Integer.parseInt(jsonObject!!.getString("total_pinjaman")))).toString()
                    tvShu.text = (formatRupiah.format(Integer.parseInt(jsonObject!!.getString("shu")))).toString()
                    tvKas.text = (formatRupiah.format(Integer.parseInt(jsonObject!!.getString("kas")))).toString()
                    tvJasaPinjaman.text = (formatRupiah.format(Integer.parseInt(jsonObject!!.getString("jasa_pinjaman")))).toString()
                    tvJasaTabungan.text = (formatRupiah.format(Integer.parseInt(jsonObject!!.getString("jasa_tabungan")))).toString()
                    tvKeuntungan.text = (formatRupiah.format(Integer.parseInt(jsonObject!!.getString("keuntungan")))).toString()
                }
                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("ONERROR",anError?.errorDetail?.toString())
                    Toast.makeText(applicationContext,"Connection Failure", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
