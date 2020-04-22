package com.pedo.laporkan.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pedo.laporkan.data.database.dao.LaporanDao
import com.pedo.laporkan.data.database.dao.TanggapanDao
import com.pedo.laporkan.data.database.dao.UserDao
import com.pedo.laporkan.data.database.typeconversion.Converters
import com.pedo.laporkan.data.model.Laporan
import com.pedo.laporkan.data.model.Tanggapan
import com.pedo.laporkan.data.model.User

@Database(entities = [Laporan::class,Tanggapan::class, User::class],version = 1,exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(){
    abstract val laporanDao : LaporanDao
    abstract val tanggapanDao : TanggapanDao
    abstract val userDao : UserDao

    companion object{
        private var INSTANCE : AppDatabase? = null

        fun getInstance(context : Context) : AppDatabase{
            synchronized(this){
                var instance =  INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "LaporKanDB"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}