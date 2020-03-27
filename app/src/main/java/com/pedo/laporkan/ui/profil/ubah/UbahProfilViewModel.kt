package com.pedo.laporkan.ui.profil.ubah

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.pedo.laporkan.data.repository.MainRepository
import com.pedo.laporkan.utils.Constants
import com.pedo.laporkan.utils.Constants.DEFAULT_TAG
import kotlinx.coroutines.*

class UbahProfilViewModel(app : Application) : AndroidViewModel(app) {
    private val repository = MainRepository.getInstance(app.applicationContext)

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _toastMessage = MediatorLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    private var _toProfil = MutableLiveData<Boolean>()
    val toProfil: LiveData<Boolean>
        get() = _toProfil

    private var _toUbahPassword = MutableLiveData<Boolean>()
    val toUbahPassword: LiveData<Boolean>
        get() = _toUbahPassword

    val idUser = MutableLiveData<String>()
    val namaLengkap = MutableLiveData<String>()
    val username = MutableLiveData<String>()
    val usernameErrorText = MutableLiveData<String>()
    val telp = MutableLiveData<String>()

    val loggedUserId = repository.mainSharedPreferences.getString(Constants.SharedPrefKey.LOGGED_USER_ID, null)
    val existingUserData = repository.getUserData(loggedUserId!!)

    fun onSimpanPerubahanClicked(){
        val currentNamaLengkap = namaLengkap.value
        val currentUsername = username.value
        val currentTelp = telp.value

        if (currentNamaLengkap == null || currentNamaLengkap.isBlank()) {
            _toastMessage.value = "Kolom isian tidak boleh kosong"
            return
        }

        if (currentUsername == null || currentUsername.isBlank()) {
            _toastMessage.value = "Kolom isian tidak boleh kosong"
            return
        }

        if (currentTelp == null || currentTelp.isBlank()) {
            _toastMessage.value = "Kolom isian tidak boleh kosong"
            return
        }

        uiScope.launch {
          updateUserData(currentNamaLengkap,currentUsername,currentTelp)
        }
        _toastMessage.value = "Profil berhasil diubah!"
        _toProfil.value = true
    }

    fun doneUbahProfil(){
        _toProfil.value = false
    }

    private suspend fun updateUserData(nama : String,username : String, telp : String){
        val user = existingUserData.value
        user?.let{
            it.nama = nama
            it.username = username
            it.telp = telp
            repository.updateUser(it)
            Log.d(DEFAULT_TAG,it.toString())
        }
    }

    fun showExistingUserData(){
        existingUserData.value?.let {
            idUser.value = it.id
            namaLengkap.value = it.nama
            username.value = it.username
            telp.value = it.telp
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}