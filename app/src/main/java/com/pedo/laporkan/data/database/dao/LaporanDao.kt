package com.pedo.laporkan.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pedo.laporkan.data.model.Laporan
import com.pedo.laporkan.data.model.StatusLaporan
import com.pedo.laporkan.data.model.relational.LaporanAndUser
import com.pedo.laporkan.data.model.relational.LaporanAndUserWithTanggapan
import com.pedo.laporkan.data.model.report.LaporanReportResponse
import com.pedo.laporkan.data.model.report.UserReportResponse

@Dao
interface LaporanDao {
    @Query("SELECT * from laporan ORDER BY tanggal")
    fun getAllLaporan(): LiveData<List<Laporan>>

    @Query("SELECT * from laporan ORDER BY tanggal DESC LIMIT 3")
    fun getLatestLaporan(): LiveData<List<Laporan>>

    @Transaction
    @Query("SELECT * from laporan where status = :status ORDER BY tanggal")
    fun getLaporanByStatus(status: StatusLaporan): LiveData<List<LaporanAndUser>>

    @Transaction
    @Query("SELECT * from laporan where id = :id")
    fun getLaporan(id: String): LiveData<LaporanAndUser>

    @Transaction
    @Query("SELECT * from laporan where id = :id")
    fun getDetailLaporan(id: String): LiveData<LaporanAndUserWithTanggapan>

    @Query("SELECT COUNT(status) as laporanCount,status from laporan where laporan.tanggal between Date(:dateFrom) and Date(:dateTo) GROUP BY laporan.status")
    fun getLaporanReportByDates(
        dateFrom: String,
        dateTo: String
    ): LiveData<List<LaporanReportResponse>>

    @Query("SELECT COUNT(id_petugas) as userCount,id_petugas as userId from laporan inner join tanggapan ON tanggapan.id_laporan = laporan.id where laporan.tanggal between Date(:dateFrom) and Date(:dateTo) GROUP BY tanggapan.id_petugas")
    fun getPetugasReportByDates(
        dateFrom: String,
        dateTo: String
    ) : LiveData<List<UserReportResponse>>

    @Query("SELECT COUNT(id_user) as userCount,id_user as userId from laporan where laporan.tanggal between Date(:dateFrom) and Date(:dateTo) GROUP BY laporan.id_user")
    fun getUserReportByDates(
        dateFrom: String,
        dateTo: String
    ) : LiveData<List<UserReportResponse>>

    @Insert
    fun insertLaporan(data: Laporan)

    @Update
    fun updateLaporan(data: Laporan)

    //there are no delete operation on laporan
}