package com.example.koperasi_admin

data class Peminjaman (val id:String?, val id_user:String?, val nama_user:String?, val nominal:String?, val status:String? )
data class Tabungan (val id:String?, val tanggal:String?, val id_user:String?, val nama_user:String?, val nominal:String?)
data class Notifikasi (val id:String?, val judul:String?, val deskripsi:String? )
data class User (val id:String?, val nama_user:String? )
data class Angsuran (val id:String?, val id_user:String?, val id_peminjaman:String?, val status:String?, val tanggal_peminjaman:String?, val tanggal_jatuhtempo:String?, val tanggal_pembayaran:String? )
data class Periode (val id:String?, val periode_awal:String?, val periode_akhir:String?, val id_user:String? )
data class RiwayatTabungan (val id:String?, val id_user:String?, val tanggal:String?, val nominal:String? )