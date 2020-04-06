package com.pedo.laporkan.ui.laporan.listing.template

import android.app.Application
import androidx.lifecycle.*
import com.pedo.laporkan.data.model.Laporan
import com.pedo.laporkan.data.repository.MainRepository
import com.pedo.laporkan.utils.Constants.KriteriaDaftarLaporan
import com.pedo.laporkan.utils.Constants.SharedPrefKey.LOGGED_USER_ID
import com.pedo.laporkan.utils.Constants.SharedPrefKey.LOGGED_USER_ROLE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class DaftarLaporanViewModel(private val criteria : String, app : Application) : AndroidViewModel(app){
    private val repository = MainRepository.getInstance(app.applicationContext)

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val currentUserId = repository.mainSharedPreferences.getString(LOGGED_USER_ID,null)
    private val currentUserRole = repository.mainSharedPreferences.getString(LOGGED_USER_ROLE,null)

    private var _fragmentTitle = MutableLiveData<String>()
    val fragmentTitle : LiveData<String>
        get() = _fragmentTitle

    private var _fragmentSubtitle = MutableLiveData<String>()
    val fragmentSubtitle : LiveData<String>
        get() = _fragmentSubtitle

    private var _toDetail = MutableLiveData<Laporan>()
    val toDetail: LiveData<Laporan>
        get() = _toDetail

    private var _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    init {
        _fragmentTitle.value = when (criteria) {
            KriteriaDaftarLaporan.SEMUA_LAPORAN -> {
                "Semua Laporan"
            }
            else -> {
                "Laporan Anda"
            }
        }

        _fragmentSubtitle.value = when (criteria) {
            KriteriaDaftarLaporan.SEMUA_LAPORAN -> {
                "Berikut adalah semua laporan\nyang bisa Anda lihat"
            }
            else -> {
                when (currentUserRole) {
                    "Masyarakat" -> "Berikut adalah laporan\nyang Anda buat"
                    else -> "Berikut adalah laporan\nyang sudah Anda tanggapi"
                }
            }
        }
    }

    val listLaporan : LiveData<List<Laporan>>
        get(){
            return when(criteria){
                KriteriaDaftarLaporan.SEMUA_LAPORAN -> {
                    repository.getAllLaporan()
                }
                else -> {
                    repository.getLaporanByUser(currentUserId!!)
                }
            }
        }

    fun displayLaporanDetail(item : Laporan){
        _toDetail.value = item
    }

    fun doneToDetail(){
        _toDetail.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(val criteria : String, val app : Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            if(modelClass.isAssignableFrom(DaftarLaporanViewModel::class.java)){
                return DaftarLaporanViewModel(
                    criteria,
                    app
                ) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}