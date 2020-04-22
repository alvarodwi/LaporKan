package com.pedo.laporkan.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import com.pedo.laporkan.data.model.Tanggapan

@Dao
interface TanggapanDao {
    @Insert
    fun insertTanggapan(data : Tanggapan)

    //there are no update nor delete operation on laporan
}