package com.pedo.laporkan.data.model

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.*
import com.pedo.laporkan.utils.Constants.FilterDaftarLaporan.LAPORAN_BARU
import com.pedo.laporkan.utils.Constants.FilterDaftarLaporan.LAPORAN_GAGAL
import com.pedo.laporkan.utils.Constants.FilterDaftarLaporan.LAPORAN_PROSES
import com.pedo.laporkan.utils.Constants.FilterDaftarLaporan.LAPORAN_SELESAI
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.LocalDate

@Entity(
    tableName = "laporan",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns =["id"],
        childColumns = ["id_user"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["id_user"],name = "userId"), Index(value = ["tanggal"],name = "tanggalLaporan")]
)
@Parcelize
data class Laporan(
    @PrimaryKey var id: String,
    var tanggal: LocalDate,
    @ColumnInfo(name = "id_user") var idUser: String,
    var judul: String,
    var isi: String,
    var foto: Bitmap? = null,
    var status: StatusLaporan = StatusLaporan.BARU
) : Parcelable {
    fun convertStatus(): String {
        return when (status) {
            StatusLaporan.BARU -> LAPORAN_BARU
            StatusLaporan.PROSES -> LAPORAN_PROSES
            StatusLaporan.GAGAL -> LAPORAN_GAGAL
            StatusLaporan.SELESAI -> LAPORAN_SELESAI
        }
    }

    fun printStatus(): String{
        return when (status) {
            StatusLaporan.BARU -> "Belum divalidasi"
            StatusLaporan.PROSES -> "Diproses"
            StatusLaporan.GAGAL -> "Gagal validasi"
            StatusLaporan.SELESAI -> "Selesai"
        }
    }

    fun printStatusWithDetail() : String{
        return when (status) {
            StatusLaporan.BARU -> "Laporan Baru"
            StatusLaporan.PROSES -> "Masih Diproses"
            StatusLaporan.GAGAL -> "Gagal Divalidasi"
            StatusLaporan.SELESAI -> "Sudah Selesai"
        }
    }
}

enum class StatusLaporan {
    BARU, PROSES, GAGAL, SELESAI
}