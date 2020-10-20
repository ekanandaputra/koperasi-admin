package com.example.koperasi_admin.Menu

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.koperasi_admin.Adapter.AdapterPeminjaman
import com.example.koperasi_admin.Peminjaman
import com.example.koperasi_admin.R
import com.example.koperasi_admin.Server
import kotlinx.android.synthetic.main.activity_list_peminjaman.*
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class ListPeminjamanActivity : AppCompatActivity() {

    var arrayList = ArrayList<Peminjaman>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_peminjaman)

        rvPeminjaman.layoutManager = LinearLayoutManager(applicationContext)
        loadPeminjaman()

    }

    private fun loadPeminjaman(){
        val loading = ProgressDialog(this)
        loading.setMessage("Memuat data...")
        loading.show()
        AndroidNetworking.get(Server.AMBILPEMINJAMAN)
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
                        tvData.visibility = View.VISIBLE
                        //Toast.makeText(applicationContext,"Tidak Ditemukan Data", Toast.LENGTH_SHORT).show()
                    }
                    for(i in 0 until jsonArray?.length()!!){
                        val jsonObject = jsonArray?.optJSONObject(i)
                        println(jsonObject)
                        arrayList.add(
                            Peminjaman(
                                jsonObject.getString("id"),
                                jsonObject.getString("id_user"),
                                jsonObject.getString("nama_user"),
                                jsonObject.getString("nominal"),
                                jsonObject.getString("status")
                            )
                        )
                        if(jsonArray?.length() - 1 == i){
                            loading.dismiss()
                            val adapter = AdapterPeminjaman(applicationContext ,arrayList)
                            adapter.notifyDataSetChanged()
                            rvPeminjaman.adapter = adapter
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