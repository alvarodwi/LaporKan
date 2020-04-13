package com.pedo.laporkan.data.model.relational

import androidx.room.Embedded
import androidx.room.Relation
import com.pedo.laporkan.data.model.Laporan
import com.pedo.laporkan.data.model.Tanggapan
import com.pedo.laporkan.data.model.User

class LaporanWithTanggapan(
    @Embedded
    val laporan : Laporan,

    @Relation(parentColumn = "id",entityColumn = "id_laporan",entity = Tanggapan::class)
    val listTanggapan : List<Tanggapan>
)

data class TanggapanAndUser(
    @Embedded
    val tanggapan : Tanggapan,
    @Relation(parentColumn = "id_petugas",entityColumn = "id",entity = User::class)
    val user : User
)

data class LaporanAndUser(
    @Embedded
    val laporan : Laporan,

    @Relation(parentColumn = "id_user",entityColumn = "id")
    val user : User
)

data class LaporanAndUserWithTanggapan(
    @Embedded
    val laporanAndUser : LaporanAndUser,

    @Relation(parentColumn = "id",entityColumn = "id_laporan",entity = Tanggapan::class)
    val listTanggapanAndUser : List<TanggapanAndUser>
)

data class ReportResponse(
    val laporanCount : Int,
    val masyarakatCount : Int,
    val petugasCount : Int
)