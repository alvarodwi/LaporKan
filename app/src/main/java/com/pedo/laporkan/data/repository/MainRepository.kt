package com.pedo.laporkan.data.repository

import android.content.Context
import com.pedo.laporkan.data.database.AppDatabase
import com.pedo.laporkan.data.model.Laporan
import com.pedo.laporkan.data.model.StatusLaporan
import com.pedo.laporkan.data.model.Tanggapan
import com.pedo.laporkan.data.model.User
import com.pedo.laporkan.utils.Constants.SP_LAPORKAN
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository(context: Context) {
    companion object {
        var INSTANCE: MainRepository? = null

        fun getInstance(context: Context): MainRepository {
            var instance = INSTANCE
            if (instance == null) {
                instance = MainRepository(context.applicationContext)
            }
            return instance
        }
    }

    //init
    private val database = AppDatabase.getInstance(context)
    val mainSharedPreferences = context.getSharedPreferences(SP_LAPORKAN, Context.MODE_PRIVATE)

    //room related function
    //laporan
    fun getLaporan(id: String) = database.laporanDao.getLaporan(id)

    fun getLaporanByUser(userId: String) = database.laporanDao.getLaporanByUser(userId)

    fun getLaporanByStatus(status: StatusLaporan) = database.laporanDao.getLaporanByStatus(status)

    suspend fun insertLaporan(data: Laporan) {
        withContext(Dispatchers.IO) {
            database.laporanDao.insertLaporan(data)
        }
    }

    suspend fun updateLaporan(data: Laporan) {
        withContext(Dispatchers.IO) {
            database.laporanDao.updateLaporan(data)
        }
    }

    //tanggapan
    fun getTanggapanByLaporan(laporanId: String) =
        database.tanggapanDao.getTanggapanOnLaporan(laporanId)

    suspend fun insertTanggapan(data: Tanggapan) {
        withContext(Dispatchers.IO) {
            database.tanggapanDao.insertTanggapan(data)
        }
    }

    //user
    fun getUserData(userId: String) = database.userDao.getUserData(userId)

    fun getUserData(username: String, password: String) =
        database.userDao.getUserData(username, password)

    fun getLatestUserData() = database.userDao.getLatestUserData()

    fun checkUsername(username: String) = database.userDao.checkUsername(username)

    suspend fun createUser(data: User) {
        withContext(Dispatchers.IO) {
            database.userDao.createUser(data)
        }
    }

    suspend fun updateUser(data: User) {
        withContext(Dispatchers.IO) {
            database.userDao.updateUser(data)
        }
    }
}