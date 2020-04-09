package com.pedo.laporkan.data.model

import androidx.room.*
import java.util.Date

@Entity(
    tableName = "tanggapan", foreignKeys = arrayOf(
        ForeignKey(
            entity = Laporan::class,
            parentColumns = ["id"],
            childColumns = ["id_laporan"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["id_petugas"],
            onDelete = ForeignKey.CASCADE
        )
    ),
    indices = arrayOf(
        Index(value = ["id_laporan"],name = "LaporanId"),
        Index(value = ["id_petugas"],name = "PetugasId")
    )
)
data class Tanggapan(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "id_laporan") var idLaporan: String,
    var tanggal: Date,
    @ColumnInfo(name = "id_petugas") var idPetugas: String,
    var tanggapan: String
)