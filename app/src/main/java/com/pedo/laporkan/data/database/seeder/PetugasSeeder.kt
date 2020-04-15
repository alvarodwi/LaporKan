package com.pedo.laporkan.data.database.seeder

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pedo.laporkan.data.model.User
import com.pedo.laporkan.data.model.UserLevel
import com.pedo.laporkan.data.repository.MainRepository
import com.pedo.laporkan.utils.Constants.DEFAULT_TAG
import java.io.BufferedReader
import java.io.InputStreamReader

class PetugasSeeder(private val repo : MainRepository){
    companion object{
        val PETUGAS_JSON = "/PetugasJSON.json"
    }

    private lateinit var petugasList : ArrayList<User>

    init {
        try {
            val inputStream = PetugasSeeder::class.java.getResourceAsStream(PETUGAS_JSON)
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)

            this.readJSONFile(bufferedReader)
        }catch (e : Exception){
            Log.d(DEFAULT_TAG,"Error Reading JSON",e)
        }
    }

    suspend fun seedPetugasData(){
        for(petugas in petugasList){
            petugas.let {
                it.level = UserLevel.PETUGAS
                Log.d(DEFAULT_TAG,it.toString())
                repo.createUser(it)
            }
        }
    }

    private fun readJSONFile(br : BufferedReader){
        val inputString = br.use { it.readText() }

        petugasList = Gson().fromJson(inputString,object : TypeToken<List<User>>(){}.type)
    }
}