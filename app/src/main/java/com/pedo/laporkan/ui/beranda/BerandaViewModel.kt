package com.pedo.laporkan.ui.beranda

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pedo.laporkan.data.repository.MainRepository
import com.pedo.laporkan.utils.Constants.SharedPrefKey.LOGGED_USER_NAME
import com.pedo.laporkan.utils.Constants.SharedPrefKey.LOGGED_USER_ROLE
import kotlinx.coroutines.Job

class BerandaViewModel(app : Application) : AndroidViewModel(app){
    private val repository = MainRepository.getInstance(app.applicationContext)

    private val viewModelJob = Job()

    val berandaRole = MutableLiveData<String>()
    val berandaGreeting = MutableLiveData<String>()

    val latestLaporan = repository.getLatestLaporan()

    private var _toProfil = MutableLiveData<Boolean>()
    val toProfil : LiveData<Boolean>
        get() = _toProfil

    private var _toListLaporan = MutableLiveData<Boolean>()
    val toListLaporan : LiveData<Boolean>
        get() = _toListLaporan

    private var _toReport = MutableLiveData<Boolean>()
    val toReport : LiveData<Boolean>
        get() = _toReport

    private var _toBuatLaporan = MutableLiveData<Boolean>()
    val toBuatLaporan : LiveData<Boolean>
        get() = _toBuatLaporan

    init {
        berandaRole.value = repository.mainSharedPreferences.getString(LOGGED_USER_ROLE,"Masyarakat")
        berandaGreeting.value = greetUser()
    }

    fun onLeftCardClick(){
        val userRole = berandaRole.value
        if(userRole=="Masyarakat"){
            _toBuatLaporan.value = true
        }else{
            _toReport.value = true
        }
    }

    fun onRightCardClick(){
        _toListLaporan.value = true
    }

    fun onMiddleCardClick(){
        _toListLaporan.value = true
    }

    fun doneNavigating(){
        _toListLaporan.value = false
        _toBuatLaporan.value = false
        _toReport.value = false
    }

    fun onBtnProfilClick(){
        _toProfil.value = true
    }

    fun doneToProfil(){
        _toProfil.value = null
    }

    private fun greetUser() : String{
        return "Hai, ${repository.mainSharedPreferences.getString(LOGGED_USER_NAME, "Budi")}"
    }

    override fun onCleared() {
        super.onCleared()

        viewModelJob.cancel()
    }
}