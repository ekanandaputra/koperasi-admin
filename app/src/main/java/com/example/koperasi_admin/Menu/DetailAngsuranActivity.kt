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
import kotlinx.android.synthetic.main.activity_detail_angsuran.*
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.TimeUnit

class DetailAngsuranActivity : AppCompatActivity() {

    var id_angsuran = ""
    var id_peminjaman = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_angsuran)

        val bundle = intent.extras
        id_angsuran = bundle!!.getString("id_angsuran").toString()
        id_peminjaman = bundle!!.getString("id_peminjaman").toString()

        loadpeminjaman(id_peminjaman)

        btBayar.setOnClickListener {
            konfirmasi(id_angsuran)
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
                    tvIdPeminjaman.text = jsonObject!!.getString("id")
                    tvAngsuran.text = (formatRupiah.format(Integer.parseInt(jsonObject!!.getString("angsuran")))).toString()
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

        AndroidNetworking.post(Server.SIMPANANGSURAN)
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
