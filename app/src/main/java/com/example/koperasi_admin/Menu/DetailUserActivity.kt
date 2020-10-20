package com.example.koperasi_admin.Menu

import android.app.ProgressDialog
import android.content.Intent
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
import kotlinx.android.synthetic.main.activity_detail_user.*
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.TimeUnit

class DetailUserActivity : AppCompatActivity() {

    var id_user = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        val bundle = intent.extras
        id_user = bundle!!.getString("id_user").toString()
        loaduser(id_user)

        btinfo.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id_user", id_user)
            val intent = Intent(applicationContext, InfoActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }

        btAngsuran.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id_user", id_user)
            val intent = Intent(applicationContext, ListAngsuranActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }

        btRiwayat.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id_user", id_user)
            val intent = Intent(applicationContext, ListPeriodeActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }

        btRiwayatTabungan.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id_user", id_user)
            val intent = Intent(applicationContext, RiwayatTabunganActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }

        btEdit.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id_user", id_user)
            val intent = Intent(applicationContext, EditActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }

//        btSetting.setOnClickListener {
////            val bundle = Bundle()
////            bundle.putString("id_user", id_user)
////            val intent = Intent(applicationContext, SettingUserActivity::class.java)
////            intent.putExtras(bundle)
////            startActivity(intent)
////        }

    }

    private fun loaduser(id: String) {
        val localeID = Locale("in", "ID")
        val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
        val loading = ProgressDialog(this)
        loading.setMessage("Memuat data...")
        loading.show()
        AndroidNetworking.post(Server.AMBIL_DETAIL_USER)
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
                }
                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("ONERROR",anError?.errorDetail?.toString())
                    Toast.makeText(applicationContext,"Connection Failure", Toast.LENGTH_SHORT).show()
                }
            })

    }
}
