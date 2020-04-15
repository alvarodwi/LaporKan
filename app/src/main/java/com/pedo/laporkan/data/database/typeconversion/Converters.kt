package com.pedo.laporkan.data.database.typeconversion

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.room.TypeConverter
import com.pedo.laporkan.data.model.StatusLaporan
import com.pedo.laporkan.data.model.UserLevel
import org.threeten.bp.LocalDate
import java.io.ByteArrayOutputStream

class Converters {
    companion object{
        //date conversion
        @TypeConverter
        @JvmStatic
        fun fromLocalDate(value: LocalDate?) : String? = value?.toString()

        @TypeConverter
        @JvmStatic
        fun toLocalDate(value: String?) : LocalDate? = LocalDate.parse(value)

        //bitmap conversion
        //date conversion
        @TypeConverter
        @JvmStatic
        fun fromBitmap(value: Bitmap?) : String?{
            val stream = ByteArrayOutputStream()
            value?.compress(Bitmap.CompressFormat.PNG,100,stream)
            val outputStream = stream.toByteArray()
            //memory wise =)
            value?.recycle()
            stream.close()

            val output = Base64.encodeToString(outputStream,Base64.DEFAULT)
            return output
        }

        @TypeConverter
        @JvmStatic
        fun toBitmap(value: String?) : Bitmap? {
            val decodedString = Base64.decode(value,Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedString,0,decodedString.size)
        }

        //statusLaporan conversion
        @TypeConverter
        @JvmStatic
        fun fromStatusLaporan(value : StatusLaporan): Int{
            return when(value){
                StatusLaporan.BARU -> 0
                StatusLaporan.GAGAL -> 1
                StatusLaporan.PROSES -> 2
                StatusLaporan.SELESAI -> 3
            }
        }

        @TypeConverter
        @JvmStatic
        fun toStatusLaporan(value: Int) : StatusLaporan {
            return when(value){
                0 -> StatusLaporan.BARU
                1 -> StatusLaporan.GAGAL
                2 -> StatusLaporan.PROSES
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