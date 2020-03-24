package com.pedo.laporkan.data.model

import androidx.room.Entity
import androidx.room.Index

//class untuk user dalam aplikasi
@Entity(tableName = "user",indices = arrayOf(Index(value = ["username"],unique = true)))
data class User(
    var id : String,
    var nama : String,
    var username : String,
    var password : String,
    var telp : String,
    var level : UserLevel
)

enum class UserLevel{
    MASYARKAT,PETUGAS,ADMIN
}