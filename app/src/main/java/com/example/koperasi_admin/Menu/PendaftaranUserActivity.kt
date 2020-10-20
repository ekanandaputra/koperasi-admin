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
import com.example.koperasi_admin.Adapter.AdapterPendaftaran
import com.example.koperasi_admin.R
import com.example.koperasi_admin.Server
import com.example.koperasi_admin.User
import kotlinx.android.synthetic.main.activity_pendaftaran_user.*
import kotlinx.android.synthetic.main.activity_pendaftaran_user.rvUser
import kotlinx.android.synthetic.main.activity_user.*
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class PendaftaranUserActivity : AppCompatActivity() {

    var arrayList = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pendaftaran_user)
        var arrayList = ArrayList<User>()
        rvUser.layoutManager = LinearLayoutManager(applicationContext)
        loadUser()
    }
    private fun loadUser(){
        val loading = ProgressDialog(this)
        loading.setMessage("Memuat data...")
        loading.show()
        AndroidNetworking.get(Server.AMBILPENDAFTARAN)
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
                            User(jsonObject.getString("id"),
                                jsonObject.getString("nama")
                            )
                        )
                        if(jsonArray?.length() - 1 == i){
                            loading.dismiss()
                            val adapter = AdapterPendaftaran(applicationContext ,arrayList)
                            adapter.notifyDataSetChanged()
                            rvUser.adapter = adapter
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
