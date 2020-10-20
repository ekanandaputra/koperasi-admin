package com.example.koperasi_admin

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
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.User
import com.example.kotlinnotifikasi.DataStorage
import com.jacksonandroidnetworking.JacksonParserFactory
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private val TAG_SUCCESS = "success"
    private val TAG_MESSAGE = "message"
    private val TAG_ID = "id"
    private val TAG_NAMA = "nama"
    internal var dataStorage: DataStorage = DataStorage()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        AndroidNetworking.setParserFactory(JacksonParserFactory())
        btLogin.setOnClickListener {
            prosesLogin()
        }
    }

    private fun prosesLogin(){
        val loading = ProgressDialog(this)
        loading.setMessage("Proses Login...")
        loading.show()
        AndroidNetworking.post(Server.LOGIN)
            .addBodyParameter("username",edUser.editableText.toString())
            .addBodyParameter("password",edPass.editableText.toString())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.d("ONRESPONSE","Berhasil menampilkan Data")
                    loading.dismiss()
                    println(response)
                    if (response?.get(TAG_SUCCESS).toString().equals("1")){
                        val bundle = Bundle()
                        bundle.putString("success", response?.get(TAG_SUCCESS).toString())
                        bundle.putString("id", response?.get(TAG_ID).toString())
                        bundle.putString("nama", response?.get(TAG_NAMA).toString())
                        dataStorage.saveNama(applicationContext, response?.get(TAG_NAMA).toString())
                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(applicationContext, response?.get(TAG_MESSAGE).toString(),
                            Toast.LENGTH_LONG).show()
                    }
                }
                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("ONERROR",anError?.errorDetail?.toString())
                    println(anError?.errorDetail?.toString())
                    Toast.makeText(applicationContext,"Connection Failure",
                        Toast.LENGTH_SHORT).show()
                }
            })
    }
}
