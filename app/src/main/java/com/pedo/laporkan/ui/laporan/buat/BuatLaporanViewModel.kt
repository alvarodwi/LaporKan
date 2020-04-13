package com.pedo.laporkan.ui.laporan.buat

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.pedo.laporkan.data.model.Laporan
import com.pedo.laporkan.data.model.StatusLaporan
import com.pedo.laporkan.data.repository.MainRepository
import com.pedo.laporkan.utils.Constants.DEFAULT_TAG
import com.pedo.laporkan.utils.Constants.SharedPrefKey.LOGGED_USER_ID
import com.pedo.laporkan.utils.Helpers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import java.util.*

class BuatLaporanViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = MainRepository.getInstance(app.applicationContext)

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private lateinit var incompleteLaporan : Laporan

    //fragment tulisan
    val judul = MutableLiveData<String>()
    val isi = MutableLiveData<String>()

    private var _isInputFilled  = MediatorLiveData<Boolean>()
    val isInputFilled : LiveData<Boolean>
        get() = _isInputFilled

    private var _nextAction = MutableLiveData<Int>()
    val nextAction: LiveData<Int>
        get() = _nextAction

    private var _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    fun onTulisanBtnClicked() {
        val currentJudul = judul.value
        val currentIsi = isi.value

        if (currentJudul == null || currentJudul.isBlank()) {
            return
        }

        if (currentIsi == null || currentIsi.isBlank()) {
            return
        }

        val currentId = UUID.randomUUID().toString().replace("-","")
        val currentTanggal = LocalDate.now()
        val currentUserId =
            repository.mainSharedPreferences.getString(LOGGED_USER_ID, null)
        val currentStatus = StatusLaporan.BARU

        incompleteLaporan = Laporan(
            id = currentId,
            tanggal = currentTanggal,
            judul = currentJudul,
            isi = currentIsi,
            idUser = currentUserId!!,
            status = currentStatus
        )

        Log.d(DEFAULT_TAG,incompleteLaporan.toString())

        _nextAction.value = 1
    }

    //fragmentLampiran aka Foto
    private var _isPhotoAttached = MutableLiveData<Boolean>()
    val isPhotoAttached : LiveData<Boolean>
        get() = _isPhotoAttached

    fun onLampiranBtnClicked(){
       isPhotoAttached.value?.let {
           if(it){
               //assign photo to laporan
           }else{
               Log.d(DEFAULT_TAG,getIncompleteLaporan().toString())
               _nextAction.value = 2
           }
       }
    }

    //tinjau

    fun onKirimLaporanClicked(){
        uiScope.launch {
            saveLaporanToDB()
        }
        _toastMessage.value = "Laporan dikirim!"
        _nextAction.value = 3
    }

    private suspend fun saveLaporanToDB(){
        val laporan = getIncompleteLaporan()
        laporan.let{
            //update tanggal to current time when this function called
            it.tanggal = LocalDate.now()
        }
        repository.insertLaporan(laporan)
    }

    init {
        _isInputFilled.value = false
        //tulisan
        _isInputFilled.addSource(Helpers.DoubleTuple(judul, isi)){
            _isInputFilled.value = !((it.first == null || it.first.isBlank()) || (it.second == null || it.second.isBlank()))
        }

        //lampiran
        _isPhotoAttached.value = false
    }

    fun doneNextAction(){
        _nextAction.value = 0
    }

    fun setIncompleteLaporan(laporan : Laporan){
        incompleteLaporan = laporan
    }

    fun getIncompleteLaporan() : Laporan = incompleteLaporan

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}