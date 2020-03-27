package com.pedo.laporkan.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Entity(tableName = "laporan")
@Parcelize
data class Laporan(
    @PrimaryKey var id : String,
    var tanggal : Date,
    @ColumnInfo(name = "id_user") var idUser : String,
    var isi : String,
    var foto : String = "",
    var status : StatusLaporan = StatusLaporan.BARU
) : Parcelable

enum class StatusLaporan{
    BARU,PROSES,GAGAL,SELESAI
}