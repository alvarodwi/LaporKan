package com.pedo.laporkan.data.database.typeconversion

import androidx.room.TypeConverter
import com.pedo.laporkan.data.model.StatusLaporan
import com.pedo.laporkan.data.model.UserLevel
import org.threeten.bp.LocalDate

class Converters {
    companion object{
        //date conversion
        @TypeConverter
        @JvmStatic
        fun fromLocalDate(value: LocalDate?) : String? = value?.toString()

        @TypeConverter
        @JvmStatic
        fun toLocalDate(value: String?) : LocalDate? = LocalDate.parse(value)

        //statusLaporan conversion
        @TypeConverter
        @JvmStatic
        fun fromStatusLaporan(value : StatusLaporan): Int{
            return when(value){
                StatusLaporan.BARU -> 0
                StatusLaporan.PROSES -> 1
                StatusLaporan.GAGAL -> 2
                StatusLaporan.SELESAI -> 3
            }
        }

        @TypeConverter
        @JvmStatic
        fun toStatusLaporan(value: Int) : StatusLaporan {
            return when(value){
                0 -> StatusLaporan.BARU
                1 -> StatusLaporan.PROSES
                2 -> StatusLaporan.GAGAL
                3 -> StatusLaporan.SELESAI
                else -> StatusLaporan.BARU
            }
        }

        //userLevel conversion
        @TypeConverter
        @JvmStatic
        fun fromUserLevel(value : UserLevel) : Int{
            return when(value){
                UserLevel.MASYARKAT -> 0
                UserLevel.PETUGAS -> 1
                UserLevel.ADMIN -> 2
            }
        }

        @TypeConverter
        @JvmStatic
        fun toUserLevel(value : Int) : UserLevel {
            return when(value){
                0 -> UserLevel.MASYARKAT
                1 -> UserLevel.PETUGAS
                2 -> UserLevel.ADMIN
                else -> UserLevel.MASYARKAT //exhaustive branch, catching error...
            }
        }
    }
}