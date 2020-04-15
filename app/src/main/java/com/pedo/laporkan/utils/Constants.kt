package com.pedo.laporkan.utils

object Constants {
    const val DEFAULT_TAG = "LaporKan"
    const val SP_LAPORKAN = "LaporKan_SharedPrefs"

    object SharedPrefKey{
        const val LOGGED_USER_ID = "logged_user_id"
        const val LOGGED_USER_ROLE = "logged_user_role"
        const val LOGGED_USER_NAME = "logged_user_name"

        const val IS_FIRST_OPEN = "first_time_app_open"
    }

    object KriteriaDaftarLaporan{
        const val SEMUA_LAPORAN = "semua_laporan"
        const val LAPORAN_YANG_DIBUAT_MASYARAKAT = "laporan_yang_dibuat_masyarakat"
        const val LAPORAN_YANG_DITANGGAPI_USER = "laporan_yang_ditanggapi"

        const val LAPORAN_BARU = "laporan_baru"
        const val LAPORAN_DIPROSES = "laporan_proses"
        const val LAPORAN_SELESAI = "laporan_selesai"
    }

    object FilterDaftarLaporan{
        const val LAPORAN_SELESAI = "Selesai"
        const val LAPORAN_BARU = "Baru"
        const val LAPORAN_PROSES = "Proses"
        const val LAPORAN_GAGAL = "Gagal"
    }

    const val KODE_ADMIN = "SMKB1S4!"

    const val REQUEST_CODE_CAMERA = 10
    const val REQUEST_CODE_GALLERY = 11
    const val IMAGE_TYPE = "image/*"
}