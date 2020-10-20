package com.example.koperasi_admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.User
import com.example.koperasi_admin.Menu.*
import com.example.kotlinnotifikasi.DataStorage
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    internal var dataStorage: DataStorage = DataStorage()
    private var nama = "nama"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        nama = dataStorage.getNama(applicationContext)

        chat.setOnClickListener {
            masuk_chat_room(nama);
        }

        peminjaman.setOnClickListener {
            val intent = Intent(applicationContext, ListPeminjamanActivity::class.java)
            startActivity(intent)
        }

        tabungan.setOnClickListener {
            val intent = Intent(applicationContext, ListTabunganActivity::class.java)
            startActivity(intent)
        }

        notifikasi.setOnClickListener {
            val intent = Intent(applicationContext, NotifikasiActivity::class.java)
            startActivity(intent)
        }

        user.setOnClickListener {
            val intent = Intent(applicationContext, UserActivity::class.java)
            startActivity(intent)
        }

        setting.setOnClickListener {
            val intent = Intent(applicationContext, SettingActivity::class.java)
            startActivity(intent)
        }

        total.setOnClickListener {
            val intent = Intent(applicationContext, TotalActivity::class.java)
            startActivity(intent)
        }

        logout.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun masuk_chat_room(nama: String) {
        CometChat.login(nama, getString(R.string.apiKey), object : CometChat.CallbackListener<User>() {
            override fun onSuccess(user: User) {
                val intent = Intent(applicationContext, MessagesActivity::class.java)
                startActivity(intent)
            }
            override fun onError(e: CometChatException) {
                Toast.makeText(applicationContext, "Error or username doesn't exist.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
