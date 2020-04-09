package com.pedo.laporkan.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pedo.laporkan.data.model.Laporan
import com.pedo.laporkan.data.model.StatusLaporan
import com.pedo.laporkan.data.model.relational.LaporanAndUser
import com.pedo.laporkan.data.model.relational.LaporanAndUserWithTanggapan
import com.pedo.laporkan.data.model.relational.LaporanWithTanggapan

@Dao
interface LaporanDao {
    @Query("SELECT * from laporan ORDER BY tanggal")
    fun getAllLaporan() : LiveData<List<Laporan>>

    @Query("SELECT * from laporan where status = :status ORDER BY tanggal")
    fun getLaporanByStatus(status : StatusLaporan) : LiveData<List<Laporan>>

    @Query("SELECT * from laporan where id_user = :idUser ORDER BY tanggal")
    fun getLaporanByUser(idUser : String) : LiveData<List<Laporan>>

    @Query("SELECT * from laporan where id_user = :idUser and status = :status ORDER BY tanggal")
    fun getLaporanWithFilter(idUser: String,status : StatusLaporan) : LiveData<List<Laporan>>

    @Transaction
    @Query("SELECT * from laporan where id = :id")
    fun getLaporan(id : String) : LiveData<LaporanAndUser>

    @Transaction
    @Query("SELECT * from laporan where id = :id")
    fun getDetailLaporan(id: String) : LiveData<LaporanAndUserWithTanggapan>

    @Insert
    fun insertLaporan(data : Laporan)

    @Update
    fun updateLaporan(data : Laporan)

    //there are no delete operation on laporan
}