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
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.User
import com.example.koperasi_admin.R
import com.example.koperasi_admin.Server
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_pendaftaran.*
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.TimeUnit

class DetailPendaftaranActivity : AppCompatActivity() {

    var id_user = ""
    var nama = ""
    var username = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_pendaftaran)

        val bundle = intent.extras
        id_user = bundle!!.getString("id_user").toString()
        loaduser(id_user)

        btkonfirmas.setOnClickListener {
            konfirmasi(id_user)
        }

    }

    private fun konfirmasi(id:String?){
        val apiKey = "948e0f81829c64bb042fb12b7a81bd0842d76c44" // Replace with your API Key.
        val user = User()
        user.uid = username // Replace with your uid for the user to be created.
        user.name = nama // Replace with the name of the user

        CometChat.createUser(user, apiKey, object : CometChat.CallbackListener<User>() {
            override fun onSuccess(user: User) {
                Log.d("createUser", user.toString())
            }

            override fun onError(e: CometChatException) {
                Log.e("createUser", e.message)
            }
        })

        val loading = ProgressDialog(this)
        loading.setMessage("Silahkan Tunggu Sebentar")
        loading.show()

        AndroidNetworking.post(Server.KONFIRMASIPENDAFTARAN)
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
                        Toast.LENGTH_SHORT).show()
                }
            })


    }

    private fun loaduser(id: String) {
        val localeID = Locale("in", "ID")
        val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
        val loading = ProgressDialog(this)
        loading.setMessage("Memuat data...")
        loading.show()
        AndroidNetworking.post(Server.AMBILDETAILPENDAFTARAN)
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
                    var linkgambar = "http://enjoycoding.my.id/koperasi/upload/"+ jsonObject!!.getString("ktp")
                    Picasso.get().load(linkgambar).into(ivKtp)
                    tvNama.text = jsonObject!!.getString("nama")
                    tvHp.text = jsonObject!!.getString("handphone")
                    tvTanggalLahir.text = jsonObject!!.getString("tanggal_lahir")
                    tvAlamat.text = jsonObject!!.getString("alamat")
                    tvPekerjaan.text = jsonObject!!.getString("pekerjaan")
                    nama = jsonObject!!.getString("nama")
                    username = jsonObject!!.getString("username")
                }
                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("ONERROR",anError?.errorDetail?.toString())
                    Toast.makeText(applicationContext,"Connection Failure", Toast.LENGTH_SHORT).show()
                }
            })

    }
}
