package com.pedo.laporkan.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tanggapan")
data class Tanggapan(
    @PrimaryKey var id : Int,
    @ColumnInfo(name = "id_laporan") var idLaporan : String,
    var tanggal : Date,
    @ColumnInfo(name = "id_petugas") var idPetugas : String,
    var tanggapan : String
)