package com.pedo.laporkan.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.pedo.laporkan.data.model.Tanggapan

@Dao
interface TanggapanDao {
    @Query("SELECT * from tanggapan WHERE id_laporan = :idLaporan")
    fun getTanggapanOnLaporan(idLaporan : String) : LiveData<List<Tanggapan>>

    @Insert
    fun insertTanggapan(data : Tanggapan)

    //there are no update nor delete operation on laporan
}