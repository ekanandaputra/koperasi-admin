package com.example.koperasi_admin

class Server {
    companion object {
        private val SERVER = "http://enjoycoding.my.id/koperasi/admin/"
        val LOGIN = SERVER +"proses_login.php"
        val AMBILPEMINJAMAN = SERVER +"ambil_peminjaman.php"
        val AMBILTABUNGAN = SERVER +"ambil_tabungan.php"
        val AMBILDETAILTABUNGAN = SERVER +"ambil_detail_tabungan.php"
        val SIMPAN_TABUNGAN = SERVER +"simpan_tabungan.php"
        val AMBILDETAILPEMINJAMAN = SERVER +"ambil_detail_peminjaman.php"
        val SIMPAN_PEMINJAMAN = SERVER +"simpan_peminjaman.php"
        val AMBILNOTIFIKASI = SERVER +"ambil_notifikasi.php"
        val AMBILUSER = SERVER +"ambil_user.php"
        val AMBIL_DETAIL_USER = SERVER +"ambil_detail_user.php"
        val AMBILANGSURAN = SERVER +"ambil_angsuran.php"
        val SIMPANANGSURAN = SERVER +"simpan_angsuran.php"
        val SIMPANTABUNGANUSER = SERVER +"simpan_tabungan_user.php"
        val AMBILSETTING = SERVER +"ambil_setting.php"
        val AMBILSETTINGUSER = SERVER +"ambil_setting_user.php"
        val SIMPANSETTING = SERVER +"simpan_setting.php"
        val SIMPANSETTINGUSER = SERVER +"simpan_setting_user.php"
        val AMBILPENDAFTARAN = SERVER +"ambil_pendaftaran.php"
        val AMBILDETAILPENDAFTARAN = SERVER +"ambil_detail_pendaftaran.php"
        val KONFIRMASIPENDAFTARAN = SERVER +"konfirmasi_pendaftaran.php"
        val TOTAL = SERVER +"total.php"
        val AMBILINFO = SERVER +"ambil_info.php"
        val AMBILPERIODE = SERVER +"ambil_periode.php"
        val AMBILRIWAYAT = SERVER +"ambil_riwayat.php"
        val AMBILEDIT = SERVER +"ambil_edit.php"
        val SIMPANEDIT = SERVER +"simpan_edit.php"
        val AMBILRIWAYATTABUNGAN = SERVER +"ambil_riwayat_tabungan.php"
        val AKHIRIPERIODE = SERVER +"akhiri_periode.php"
    }
}