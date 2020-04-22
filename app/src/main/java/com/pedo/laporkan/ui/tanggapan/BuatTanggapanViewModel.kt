package com.pedo.laporkan.ui.tanggapan

import android.app.Application
import androidx.lifecycle.*
import com.pedo.laporkan.data.model.Tanggapan
import com.pedo.laporkan.data.repository.MainRepository
import com.pedo.laporkan.utils.Constants
import com.pedo.laporkan.utils.Helpers.shortenName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

class BuatTanggapanViewModel(private val idLaporan: String,app : Application) : AndroidViewModel(app) {
    private val repository = MainRepository.getInstance(app.applicationContext)

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val laporanItem = repository.getLaporan(idLaporan)
    val laporanUserInfo = Transformations.map(laporanItem){
        it?.let {
            "Dibuat oleh ${shortenName(it.user.nama)}"
        }
    }
    val tanggapan = MutableLiveData<String>()

    private var _nextAction = MutableLiveData<Boolean>()
    val nextAction: LiveData<Boolean>
        get() = _nextAction

    private var _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    fun onKirimTanggapanBtnClicked(){
        val currentTanggapan = tanggapan.value

        if (currentTanggapan == null || currentTanggapan.isBlank()) {
            return
        }

        val currentTanggal = LocalDate.now()
        val currentPetugasId =
            repository.mainSharedPreferences.getString(Constants.SharedPrefKey.LOGGED_USER_ID, null)

        val tanggapan = Tanggapan(
            idLaporan = idLaporan,
            tanggal = currentTanggal,
            tanggapan = currentTanggapan,
            idPetugas = currentPetugasId!!
        )

        uiScope.launch {
            saveTanggapanToDB(tanggapan)
        }
        _toastMessage.value = "Tanggapan Dikirim!"
        _nextAction.value = true
    }

    fun doneNextAction(){
        _nextAction.value = null
    }

    private suspend fun saveTanggapanToDB(data : Tanggapan){
        repository.insertTanggapan(data)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(val idLaporan: String,val app : Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            if(modelClass.isAssignableFrom(BuatTanggapanViewModel::class.java)){
                return BuatTanggapanViewModel(
                    idLaporan,
                    app
                ) as T
            }
            throw IllegalArgumentException("Cannot assign viewmodel")
        }

    }
}