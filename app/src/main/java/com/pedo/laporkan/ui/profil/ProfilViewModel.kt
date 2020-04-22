package com.pedo.laporkan.ui.profil

import android.app.Application
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.*
import com.pedo.laporkan.data.repository.MainRepository
import com.pedo.laporkan.utils.Constants
import com.pedo.laporkan.utils.Constants.DEFAULT_TAG
import com.pedo.laporkan.utils.Constants.SharedPrefKey.LOGGED_USER_ID
import com.pedo.laporkan.utils.Helpers.shortenName
import kotlinx.coroutines.Job

class ProfilViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = MainRepository.getInstance(app.applicationContext)

    private val viewModelJob = Job()

    val loggedUserId = repository.mainSharedPreferences.getString(LOGGED_USER_ID, null)
    val userData = repository.getUserData(loggedUserId!!)

    private var _toastMessage = MediatorLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    private var _toLoginAfterLogout = MutableLiveData<Boolean>()
    val toLoginAfterLogout: LiveData<Boolean>
        get() = _toLoginAfterLogout

    val userShortenedName = Transformations.map(userData) {
        it?.let {
            shortenName(it.nama)
        }
    }

    init {
        Log.d(DEFAULT_TAG, loggedUserId!!)
        Log.d(DEFAULT_TAG, userData.value.toString())
    }

    fun onLogoutClicked(){
        repository.mainSharedPreferences.edit {
            putString(LOGGED_USER_ID,null)
            putString(Constants.SharedPrefKey.LOGGED_USER_ROLE,null)
            putString(Constants.SharedPrefKey.LOGGED_USER_NAME, null)
            this.apply()
        }
        _toLoginAfterLogout.value = true
        _toastMessage.value = "Menghapus sesi user..."
    }

    fun doneLogout(){
        _toLoginAfterLogout.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}