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
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_total.*
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.TimeUnit

class TotalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_total)

        loadTotal();
    }

    private fun loadTotal() {
        val localeID = Locale("in", "ID")
        val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
        val loading = ProgressDialog(this)
        loading.setMessage("Memuat data...")
        loading.show()
        AndroidNetworking.get(Server.TOTAL)
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
                    tvTotalPinjaman.text = (formatRupiah.format(Integer.parseInt(jsonObject!!.getString("total_pinjaman")))).toString()
                    tvSahamAwal.text = (formatRupiah.format(Integer.parseInt(jsonObject!!.getString("saham_awal")))).toString()
                    tvKas.text = (formatRupiah.format(Integer.parseInt(jsonObject!!.getString("kas")))).toString()
                    tvTabungan.text = (formatRupiah.format(Integer.parseInt(jsonObject!!.getString("tabungan")))).toString()
                    tvJasaTabungan.text = (formatRupiah.format(Integer.parseInt(jsonObject!!.getString("jasa")))).toString()
                    tvShu.text = (formatRupiah.format(Integer.parseInt(jsonObject!!.getString("shu")))).toString()
                    tvKas.text = (formatRupiah.format(Integer.parseInt(jsonObject!!.getString("kas")))).toString()
                    tvKeuntungan.text = (formatRupiah.format(Integer.parseInt(jsonObject!!.getString("keuntungan")))).toString()
                    tvTotal.text = (formatRupiah.format(Integer.parseInt(jsonObject!!.getString("total")))).toString()
                    tvPerOrang.text = (formatRupiah.format(Integer.parseInt(jsonObject!!.getString("keuntungan_orang")))).toString()

                }
                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("ONERROR",anError?.errorDetail?.toString())
                    Toast.makeText(applicationContext,"Connection Failure", Toast.LENGTH_SHORT).show()
                }
            })

    }
}
