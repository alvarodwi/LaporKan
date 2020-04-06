package com.pedo.laporkan.utils

object Constants {
    const val DEFAULT_TAG = "LaporKan"
    const val SP_LAPORKAN = "LaporKan_SharedPrefs"

    object SharedPrefKey{
        const val LOGGED_USER_ID = "logged_user_id"
        const val LOGGED_USER_ROLE = "logged_user_role"
        const val LOGGED_USER_NAME = "logged_user_name"
    }

    object KriteriaDaftarLaporan{
        const val SEMUA_LAPORAN = "semua_laporan"
        const val LAPORAN_YANG_DIBUAT_MASYARAKAT = "laporan_yang_dibuat_masyarakat"
        const val LAPORAN_YANG_DITANGGAPI_USER = "laporan_yang_ditanggapi"
    }

    object FilterDaftarLaporan{
        const val LAPORAN_SELESAI = "Selesai"
        const val LAPORAN_BARU = "Baru"
        const val LAPORAN_PROSES = "Proses"
        const val LAPORAN_GAGAL = "Gagal"
    }

    const val KODE_ADMIN = "SMKB1S4!"
}