package com.pedo.laporkan.data.model

import androidx.room.*
import org.threeten.bp.LocalDate

@Entity(
    tableName = "tanggapan", foreignKeys = [ForeignKey(
        entity = Laporan::class,
        parentColumns = ["id"],
        childColumns = ["id_laporan"],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["id_petugas"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["id_laporan"],name = "LaporanId"), Index(value = ["id_petugas"],name = "PetugasId")]
)
data class Tanggapan(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "id_laporan") var idLaporan: String,
    var tanggal: LocalDate,
    @ColumnInfo(name = "id_petugas") var idPetugas: String,
    var tanggapan: String
)