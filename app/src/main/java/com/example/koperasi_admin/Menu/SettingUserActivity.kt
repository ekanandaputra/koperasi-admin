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
import kotlinx.android.synthetic.main.activity_setting_user.*
import kotlinx.android.synthetic.main.activity_setting_user.btSimpan
import kotlinx.android.synthetic.main.activity_setting_user.edJasaPinjaman
import kotlinx.android.synthetic.main.activity_setting_user.edJasaTabungan
import kotlinx.android.synthetic.main.activity_setting_user.edShu
import kotlinx.android.synthetic.main.activity_setting_user.edSku
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class SettingUserActivity : AppCompatActivity() {

    var id_user = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_user)

        val bundle = intent.extras
        id_user = bundle!!.getString("id_user").toString()
        loadsetting()
        btSimpan.setOnClickListener {
            simpan(id_user, edShu.text.toString(), edSku.text.toString(), edJasaPinjaman.text.toString(), edJasaTabungan.text.toString())
        }
    }

    private fun loadsetting() {
        val loading = ProgressDialog(this)
        loading.setMessage("Memuat data...")
        loading.show()
        AndroidNetworking.post(Server.AMBILSETTINGUSER)
            .addBodyParameter("id_user",id_user)
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
                    edShu.setText(jsonObject!!.getString("shu"))
                    edSku.setText(jsonObject!!.getString("kas"))
                    edJasaPinjaman.setText(jsonObject!!.getString("jasa_pinjaman"))
                    edJasaTabungan.setText(jsonObject!!.getString("jasa_tabungan"))
                }
                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("ONERROR",anError?.errorDetail?.toString())
                    Toast.makeText(applicationContext,"Connection Failure", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun simpan(idUser: String, shu: String, sku: String, jasa_pinjaman: String, jasa_tabungan: String) {
        val loading = ProgressDialog(this)
        loading.setMessage("Silahkan Tunggu Sebentar")
        loading.show()

        AndroidNetworking.post(Server.SIMPANSETTINGUSER)
            .addBodyParameter("id_user",idUser)
            .addBodyParameter("shu",shu)
            .addBodyParameter("sku",sku)
            .addBodyParameter("jasa_pinjaman",jasa_pinjaman)
            .addBodyParameter("jasa_tabungan",jasa_tabungan)
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
