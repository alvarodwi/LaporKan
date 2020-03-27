package com.pedo.laporkan.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

//class untuk user dalam aplikasi
@Entity(tableName = "user",indices = arrayOf(Index(value = ["username"],unique = true)))
@Parcelize
data class User(
    @PrimaryKey var id : String,
    var nama : String,
    var username : String = "",
    var password : String = "",
    var telp : String,
    var level : UserLevel
) : Parcelable{
    fun convertLevel() : String{
        return when(level){
            UserLevel.MASYARKAT -> "Masyarakat"
            UserLevel.ADMIN -> "Admin"
            UserLevel.PETUGAS -> "Petugas"
        }
    }
}

enum class UserLevel{
    MASYARKAT,PETUGAS,ADMIN
}