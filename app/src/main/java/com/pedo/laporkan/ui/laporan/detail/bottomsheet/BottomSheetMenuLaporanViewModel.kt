package com.pedo.laporkan.ui.laporan.detail.bottomsheet

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.pedo.laporkan.data.model.Laporan
import com.pedo.laporkan.data.model.StatusLaporan
import com.pedo.laporkan.data.repository.MainRepository
import com.pedo.laporkan.utils.Constants.DEFAULT_TAG
import com.pedo.laporkan.utils.Constants.FilterDaftarLaporan.LAPORAN_BARU
import com.pedo.laporkan.utils.Constants.FilterDaftarLaporan.LAPORAN_PROSES
import com.pedo.laporkan.utils.Constants.SharedPrefKey.LOGGED_USER_ROLE
import kotlinx.coroutines.*

class BottomSheetMenuLaporanViewModel(private val itemLaporan: Laporan,app : Application) : AndroidViewModel(app){
    private val repository = MainRepository.getInstance(app.applicationContext)
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Default + viewModelJob)

    private val _nextAction = MutableLiveData<Int>()
    val nextAction : LiveData<Int>
        get() = _nextAction

    val labelFragment = MutableLiveData<String>()
    val hideBuatTanggapan = MutableLiveData<Boolean>()
    val hideUbahStatus = MutableLiveData<Boolean>()
    val showTutupLaporan = MutableLiveData<Boolean>()
    val subLabelUbahStatus = MutableLiveData<String>()

    init {
        val statusLaporan = itemLaporan.convertStatus()
        hideBuatTanggapan.value = false
        showTutupLaporan.value = false
        hideUbahStatus.value = false

        when(statusLaporan){
            LAPORAN_BARU -> subLabelUbahStatus.value = "Mengubah status laporan menjadi PROSES"
            LAPORAN_PROSES -> subLabelUbahStatus.value = "Mengubah status laporan menjadi SELESAI"
            else -> {
                hideUbahStatus.value = true
                hideBuatTanggapan.value = true
            }
        }

        val userRole = repository.mainSharedPreferences.getString(LOGGED_USER_ROLE,null)
        userRole?.let {
            when(it){
                "Petugas" -> {
                    labelFragment.value = "MENU PETUGAS"
                    if(statusLaporan == "BARU"){
                        showTutupLaporan.value = true
                    }
                }
                else -> {
                    labelFragment.value = "MENU ADMIN"
                }
            }
        }
    }

    fun ubahStatusLaporan(){
        var oldLaporan = itemLaporan
        when(oldLaporan.convertStatus()){
            LAPORAN_BARU -> {
                oldLaporan.status = StatusLaporan.PROSES
            }
            LAPORAN_PROSES -> {
                oldLaporan.status = StatusLaporan.SELESAI
            }
            else -> {
                //do nothing
            }
        }
        Log.d(DEFAULT_TAG,oldLaporan.toString())

        uiScope.launch {
            updateLaporan(oldLaporan)
        }
    }

    fun tutupLaporan(){
        var oldLaporan = itemLaporan
        oldLaporan.status = StatusLaporan.GAGAL

        Log.d(DEFAULT_TAG,oldLaporan.toString())

        uiScope.launch {
            updateLaporan(oldLaporan)
        }
    }

    private suspend fun updateLaporan(data : Laporan){
        repository.updateLaporan(data)
    }

    fun onBuatTanggapanClicked(){
        _nextAction.value = 1
    }

    fun onUbahStatusLaporanClicked(){
        _nextAction.value = 2
    }

    fun onTutupLaporanClicked(){
        _nextAction.value = 3
    }

    fun resetNextAction(){
        _nextAction.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(val itemLaporan : Laporan, val app : Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            if(modelClass.isAssignableFrom(BottomSheetMenuLaporanViewModel::class.java)){
                return BottomSheetMenuLaporanViewModel(
                    itemLaporan,
                    app
                ) as T
            }
            throw IllegalArgumentException("Cannot assign viewmodel")
        }

    }
}