package com.example.koperasi_admin.Menu

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.koperasi_admin.HomeActivity
import com.example.koperasi_admin.R
import com.example.koperasi_admin.Server
import kotlinx.android.synthetic.main.activity_edit.*
import org.json.JSONObject
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class EditActivity : AppCompatActivity() {

    var id_user = ""
    var myCalendar = Calendar.getInstance();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val bundle = intent.extras
        id_user = bundle!!.getString("id_user").toString()
        loaduser(id_user)

        edTanggalLahir.setOnClickListener {
            tanggal(edTanggalLahir)
        }

        btEdit.setOnClickListener {
            simpan(id_user, edNama.text.toString(), edHp.text.toString(), edTanggalLahir.text.toString(), edAlamat.text.toString(), edPekerjaan.text.toString() )
        }

    }

    private fun loaduser(id: String) {
        val loading = ProgressDialog(this)
        loading.setMessage("Memuat data...")
        loading.show()
        AndroidNetworking.post(Server.AMBILEDIT)
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
                    edNama.setText(jsonObject?.getString("nama"))
                    edHp.setText(jsonObject?.getString("handphone"))
                    edTanggalLahir.setText(jsonObject?.getString("tanggal_lahir"))
                    edAlamat.setText(jsonObject?.getString("alamat"))
                    edPekerjaan.setText(jsonObject?.getString("pekerjaan"))
                }
                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("ONERROR",anError?.errorDetail?.toString())
                    Toast.makeText(applicationContext,"Connection Failure", Toast.LENGTH_SHORT).show()
                }
            })

    }

    private fun simpan(idUser: String, nama: String, handphone: String, tanggal_lahir: String, alamat: String, pekerjaan: String) {
        val loading = ProgressDialog(this)
        loading.setMessage("Silahkan Tunggu Sebentar")
        loading.show()

        AndroidNetworking.post(Server.SIMPANEDIT)
            .addBodyParameter("id_user",idUser)
            .addBodyParameter("nama",nama)
            .addBodyParameter("handphone",handphone)
            .addBodyParameter("tanggal_lahir",tanggal_lahir)
            .addBodyParameter("alamat",alamat)
            .addBodyParameter("pekerjaan",pekerjaan)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    loading.dismiss()
                    Toast.makeText(applicationContext,response?.getString("message"),
                        Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, HomeActivity::class.java)
                    startActivity(intent)
                }
                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("ONERROR",anError?.errorDetail?.toString())
                    Toast.makeText(applicationContext,"Connection Failure",
                        Toast.LENGTH_SHORT).show()}
            })
    }

    fun tanggal(checkin: TextView?){
        DatePickerDialog(
            this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val formatTanggal = "yyyy-MM-dd"
                val sdf = SimpleDateFormat(formatTanggal)
                checkin?.setText(sdf.format(myCalendar.getTime()))
            },
            myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}
