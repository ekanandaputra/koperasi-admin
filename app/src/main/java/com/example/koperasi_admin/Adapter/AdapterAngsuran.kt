package com.example.koperasi_admin.Adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.koperasi_admin.Angsuran
import com.example.koperasi_admin.Menu.DetailAngsuranActivity
import com.example.koperasi_admin.R
import kotlinx.android.synthetic.main.layout_angsuran.view.*

class AdapterAngsuran(private val context: Context, private val arrayList: ArrayList<Angsuran>) : RecyclerView.Adapter<AdapterAngsuran.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.layout_angsuran,parent,false))
    }

    override fun getItemCount(): Int = arrayList!!.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.view.tvIdPeminjaman.text = "ID Peminjaman #"+arrayList!!.get(position)?.id_peminjaman
        holder.view.tvStatus.text = arrayList?.get(position)?.status
        holder.view.tvTanggalPeminjaman.text = arrayList?.get(position)?.tanggal_peminjaman
        holder.view.tvTanggalJatuhTempo.text = arrayList?.get(position)?.tanggal_jatuhtempo
        holder.view.tvTanggalPembayaran.text = arrayList?.get(position)?.tanggal_pembayaran
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id_peminjaman", arrayList!!.get(position)?.id_peminjaman)
            bundle.putString("id_angsuran", arrayList!!.get(position)?.id)
            val intent = Intent(context, DetailAngsuranActivity::class.java)
            intent.putExtras(bundle)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent)
        }
    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)

}