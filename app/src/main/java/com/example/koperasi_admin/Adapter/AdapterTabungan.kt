package com.example.koperasi_admin.Adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.koperasi_admin.Menu.KonfirmasiTabunganActivity
import com.example.koperasi_admin.R
import com.example.koperasi_admin.Tabungan
import kotlinx.android.synthetic.main.layout_peminjaman.view.*

class AdapterTabungan(private val context: Context, private val arrayList: ArrayList<Tabungan>) : RecyclerView.Adapter<AdapterTabungan.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.layout_tabungan,parent,false))
    }

    override fun getItemCount(): Int = arrayList!!.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.view.tvNama.text = arrayList!!.get(position)?.nama_user
        holder.view.tvNominal.text = arrayList?.get(position)?.nominal
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", arrayList!!.get(position)?.id)
            bundle.putString("id_user", arrayList!!.get(position)?.id_user)
            val intent = Intent(context, KonfirmasiTabunganActivity::class.java)
            intent.putExtras(bundle)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent)
        }
    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)

}