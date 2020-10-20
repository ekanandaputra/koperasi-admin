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
import com.example.koperasi_admin.Adapter.AdapterNotif
import com.example.koperasi_admin.Notifikasi
import com.example.koperasi_admin.R
import com.example.koperasi_admin.Server
import kotlinx.android.synthetic.main.activity_notifikasi.*
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class NotifikasiActivity : AppCompatActivity() {

    var arrayList = ArrayList<Notifikasi>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifikasi)

        rvNotif.layoutManager = LinearLayoutManager(applicationContext)
        loadNotif()

    }

    private fun loadNotif(){
        val loading = ProgressDialog(this)
        loading.setMessage("Memuat data...")
        loading.show()
        AndroidNetworking.get(Server.AMBILNOTIFIKASI)
            .setPriority(Priority.HIGH)
            .getResponseOnlyFromNetwork()
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
                        Toast.makeText(applicationContext,"Tidak Ditemukan Data Menu", Toast.LENGTH_SHORT).show()
                    }
                    for(i in 0 until jsonArray?.length()!!){
                        val jsonObject = jsonArray?.optJSONObject(i)
                        println(jsonObject)
                        arrayList.add(
                            Notifikasi(jsonObject.getString("id"),
                                jsonObject.getString("judul"),
                                jsonObject.getString("deskripsi")
                            )
                        )
                        if(jsonArray?.length() - 1 == i){
                            loading.dismiss()
                            val adapter = AdapterNotif(applicationContext ,arrayList)
                            adapter.notifyDataSetChanged()
                            rvNotif.adapter = adapter
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

