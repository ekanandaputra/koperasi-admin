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
import com.example.koperasi_admin.Adapter.AdapterTabungan
import com.example.koperasi_admin.R
import com.example.koperasi_admin.Server
import com.example.koperasi_admin.Tabungan
import kotlinx.android.synthetic.main.activity_list_tabungan.*
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class ListTabunganActivity : AppCompatActivity() {

    var arrayList = ArrayList<Tabungan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_tabungan)

        rvTabungan.layoutManager = LinearLayoutManager(applicationContext)
        loadKas()
        loadTabungan()

    }

    private fun loadKas() {
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

    private fun loadTabungan(){
        val loading = ProgressDialog(this)
        loading.setMessage("Memuat data...")
        loading.show()
        AndroidNetworking.get(Server.AMBILTABUNGAN)
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
                        //Toast.makeText(applicationContext,"Tidak Ditemukan Data Menu", Toast.LENGTH_SHORT).show()
                    }
                    for(i in 0 until jsonArray?.length()!!){
                        val jsonObject = jsonArray?.optJSONObject(i)
                        println(jsonObject)
                        arrayList.add(
                            Tabungan(
                                jsonObject.getString("id"),
                                jsonObject.getString("tanggal"),
                                jsonObject.getString("id_user"),
                                jsonObject.getString("nama_user"),
                                jsonObject.getString("nominal")

                            )
                        )
                        if(jsonArray?.length() - 1 == i){
                            loading.dismiss()
                            val adapter = AdapterTabungan(applicationContext ,arrayList)
                            adapter.notifyDataSetChanged()
                            rvTabungan.adapter = adapter
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

