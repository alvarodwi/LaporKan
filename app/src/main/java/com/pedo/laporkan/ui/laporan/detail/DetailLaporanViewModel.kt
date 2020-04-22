package com.pedo.laporkan.ui.laporan.detail

import android.app.Application
import androidx.lifecycle.*
import com.pedo.laporkan.data.model.Laporan
import com.pedo.laporkan.data.repository.MainRepository
import com.pedo.laporkan.utils.Constants.SharedPrefKey.LOGGED_USER_ROLE
import kotlinx.coroutines.Job

class DetailLaporanViewModel(idLaporan: String, app: Application) : AndroidViewModel(app) {
    private val repository = MainRepository.getInstance(app.applicationContext)

    private val viewModelJob = Job()

    private val bundleLaporan = repository.getDetailLaporan(idLaporan)

    val menuLaporanArgs = MutableLiveData<Laporan>()
    val showMenuDetail = MutableLiveData<Boolean>()
    val labelMenuDetail = MutableLiveData<String>()

    val itemLaporan = Transformations.map(bundleLaporan) {
        it?.laporanAndUser?.laporan
    }

    val itemUser = Transformations.map(bundleLaporan) {
        it?.laporanAndUser?.user
    }

    val listTanggapan = Transformations.map(bundleLaporan) {
        it?.listTanggapanAndUser
    }

    init {
        showMenuDetail.value = false
        val userRole = repository.mainSharedPreferences.getString(LOGGED_USER_ROLE, null)
        userRole?.let {
            when (it) {
                "Admin" -> {
                    labelMenuDetail.value = "Menu Admin"
                    showMenuDetail.value = true
                }
                "Petugas" -> {
                    labelMenuDetail.value = "Menu Petugas"
                    showMenuDetail.value = true
                }
            }
        }
    }

    fun disableMenuDetail(){
        showMenuDetail.value = false
    }

    fun onMenuBtnClicked() {
        menuLaporanArgs.value = itemLaporan.value
    }

    fun doneMenuBtnClicked() {
        menuLaporanArgs.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(val idLaporan: String, val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            if (modelClass.isAssignableFrom(DetailLaporanViewModel::class.java)) {
                return DetailLaporanViewModel(
                    idLaporan,
                    app
                ) as T
            }
            throw IllegalArgumentException("Unable To Construct ViewModel")
        }

    }
}