package com.pedo.laporkan.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pedo.laporkan.data.model.User

@Dao
interface UserDao {
    @Query("SELECT * from user where id = :id")
    fun getUserData(id : String) : LiveData<User>

    @Query("SELECT * from user ORDER BY id DESC LIMIT 1")
    fun getLatestUserData() : User?

    @Query("SELECT * from user where username = :username and password = :password")
    fun getUserData(username : String,password: String) : User?

    @Query("SELECT * from user where username = :username")
    fun checkUsername(username : String) : User?

    @Insert
    fun createUser(data : User)

    @Update
    fun updateUser(data : User)
}