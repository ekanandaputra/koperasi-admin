package com.example.koperasi_admin.Adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.koperasi_admin.Menu.DetailUserActivity
import com.example.koperasi_admin.R
import com.example.koperasi_admin.User
import kotlinx.android.synthetic.main.layout_user.view.*

class AdapterUser(private val context: Context, private val arrayList: ArrayList<User>) : RecyclerView.Adapter<AdapterUser.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_user, parent, false)
        )
    }

    override fun getItemCount(): Int = arrayList!!.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.view.tvNama.text = arrayList!!.get(position)?.nama_user
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id_user", arrayList!!.get(position)?.id)
            val intent = Intent(context, DetailUserActivity::class.java)
            intent.putExtras(bundle)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent)
        }
    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)
}
