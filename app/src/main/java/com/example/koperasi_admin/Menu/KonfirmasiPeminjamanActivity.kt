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
import kotlinx.android.synthetic.main.activity_konfirmasi_peminjaman.*
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.TimeUnit

class KonfirmasiPeminjamanActivity : AppCompatActivity() {

    var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_konfirmasi_peminjaman)

        val bundle = intent.extras
        id = bundle!!.getString("id").toString()
        println(id)

        loadpeminjaman(id)

        btAjukan.setOnClickListener {
            konfirmasi(id)
        }
    }

    private fun loadpeminjaman(id: String) {
        val localeID = Locale("in", "ID")
        val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
        val loading = ProgressDialog(this)
        loading.setMessage("Memuat data...")
        loading.show()
        AndroidNetworking.post(Server.AMBILDETAILPEMINJAMAN)
            .addBodyParameter("id",id)
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
                    tvNama.text = jsonObject!!.getString("nama_user")
                    tvNominal.text = (formatRupiah.format(Integer.parseInt(jsonObject!!.getString("nominal")))).toString()
                    tvAdmin.text = (formatRupiah.format(Integer.parseInt(jsonObject!!.getString("biaya_admin")))).toString()
                    tvCicilan.text = (formatRupiah.format(Integer.parseInt(jsonObject!!.getString("jumlah_angsuran")))).toString()
                    tvTotal.text = (formatRupiah.format(Integer.parseInt(jsonObject!!.getString("total_bayar")))).toString()

                }
                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("ONERROR",anError?.errorDetail?.toString())
                    Toast.makeText(applicationContext,"Connection Failure", Toast.LENGTH_SHORT).show()
                }
            })

    }

    private fun konfirmasi(id:String?){
        val loading = ProgressDialog(this)
        loading.setMessage("Silahkan Tunggu Sebentar")
        loading.show()

        AndroidNetworking.post(Server.SIMPAN_PEMINJAMAN)
            .addBodyParameter("id",id)
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
