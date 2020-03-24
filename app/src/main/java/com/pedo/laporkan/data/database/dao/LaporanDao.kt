package com.pedo.laporkan.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pedo.laporkan.data.model.Laporan
import com.pedo.laporkan.data.model.StatusLaporan

@Dao
interface LaporanDao {
    @Query("SELECT * from laporan ORDER BY tanggal")
    fun getAllLaporan() : LiveData<List<Laporan>>

    @Query("SELECT * from laporan where status = :status ORDER BY tanggal")
    fun getLaporanByStatus(status : StatusLaporan) : LiveData<List<Laporan>>

    @Query("SELECT * from laporan where id_user = :idUser ORDER BY tanggal")
    fun getLaporanByUser(idUser : String) : LiveData<List<Laporan>>

    @Query("SELECT * from laporan where id = :id")
    fun getLaporan(id : String) : LiveData<Laporan>

    @Insert
    fun insertLaporan(data : Laporan)

    @Update
    fun updateLaporan(data : Laporan)

    //there are no delete operation on laporan
}