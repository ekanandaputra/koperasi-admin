package com.example.koperasi_admin.Menu

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.koperasi_admin.Adapter.AdapterAngsuran
import com.example.koperasi_admin.Angsuran
import com.example.koperasi_admin.R
import com.example.koperasi_admin.Server
import kotlinx.android.synthetic.main.activity_list_angsuran.*
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class ListAngsuranActivity : AppCompatActivity() {

    var arrayList = ArrayList<Angsuran>()
    var id_user = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_angsuran)

        val bundle = intent.extras
        id_user = bundle!!.getString("id_user").toString()
        rvAngsuran.layoutManager = LinearLayoutManager(applicationContext)
        loadangsuran(id_user)

    }

    private fun loadangsuran(idUser: String) {
        val loading = ProgressDialog(this)
        loading.setMessage("Memuat data...")
        loading.show()
        AndroidNetworking.post(Server.AMBILANGSURAN)
            .setPriority(Priority.HIGH)
            .getResponseOnlyFromNetwork()
            .addBodyParameter("id_user",idUser)
            .doNotCacheResponse()
            .setMaxAgeCacheControl(0, TimeUnit.SECONDS)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    println(response)
                    arrayList.clear()
                    Log.d("ONRESPONSE","Berhasil menampilkan Data")
                    val jsonArray = response?.optJSONArray("result")
                    if(jsonArray?.length() == 0){
                        loading.dismiss()
                        Toast.makeText(applicationContext,"Tidak Ditemukan Data", Toast.LENGTH_SHORT).show()
                    }
                    for(i in 0 until jsonArray?.length()!!){
                        val jsonObject = jsonArray?.optJSONObject(i)
                        println(jsonObject)
                        arrayList.add(
                            Angsuran(
                                jsonObject.getString("id"),
                                jsonObject.getString("id_user"),
                                jsonObject.getString("id_pinjaman"),
                                jsonObject.getString("status"),
                                jsonObject.getString("tanggal_peminjaman"),
                                jsonObject.getString("jatuh_tempo"),
                                jsonObject.getString("tanggal_pembayaran")
                            )
                        )
                        if(jsonArray?.length() - 1 == i){
                            loading.dismiss()
                            val adapter = AdapterAngsuran(applicationContext ,arrayList)
                            adapter.notifyDataSetChanged()
                            rvAngsuran.adapter = adapter
                        }
                    }

                }
                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("ONERROR",anError?.errorDetail?.toString())
                    Toast.makeText(applicationContext,"Connection Failure", Toast.LENGTH_SHORT).show()
                }
            })

    }
}

