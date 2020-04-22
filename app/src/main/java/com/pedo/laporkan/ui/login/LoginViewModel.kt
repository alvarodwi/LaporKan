package com.pedo.laporkan.ui.login

import android.app.Application
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.pedo.laporkan.data.model.User
import com.pedo.laporkan.data.repository.MainRepository
import com.pedo.laporkan.utils.Constants.DEFAULT_TAG
import com.pedo.laporkan.utils.Constants.SharedPrefKey.LOGGED_USER_ID
import com.pedo.laporkan.utils.Constants.SharedPrefKey.LOGGED_USER_NAME
import com.pedo.laporkan.utils.Constants.SharedPrefKey.LOGGED_USER_ROLE
import com.pedo.laporkan.utils.Helpers.shortenName
import kotlinx.coroutines.*
import java.util.*

enum class AuthenticationState {
    SUKSES, USERNAME_SALAH, PASSWORD_SALAH
}

class LoginViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = MainRepository.getInstance(app.applicationContext)

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val username = MutableLiveData<String>()
    val usernameErrorText = MediatorLiveData<String>()

    val password = MutableLiveData<String>()
    val passwordErrorText = MediatorLiveData<String>()

    private val _greeting = MutableLiveData<String>()
    val greeting: LiveData<String>
        get() = _greeting

    private var _authenticationState = MutableLiveData<AuthenticationState>()
    val authenticationState: LiveData<AuthenticationState>
        get() = _authenticationState

    private var _toastMessage = MediatorLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    private var _toRegisterAction = MutableLiveData<Boolean>()
    val toRegisterAction: LiveData<Boolean>
        get() = _toRegisterAction

    init {
        usernameErrorText.addSource(username) {
            if (it == null || it.isBlank()) {
                usernameErrorText.value = "Username tidak boleh kosong"
            } else {
                usernameErrorText.value = null
            }
        }

        passwordErrorText.addSource(password) {
            if (it == null || it.isBlank()) {
                passwordErrorText.value = "Password tidak boleh kosong"
            } else {
                passwordErrorText.value = null
            }
        }

        _toastMessage.addSource(authenticationState) {
            when (it) {
                AuthenticationState.SUKSES -> {
                    _toastMessage.value = "Login Sukses!"
                }
                AuthenticationState.USERNAME_SALAH -> {
                    _toastMessage.value = "Username tidak ditemukan"
                }
                AuthenticationState.PASSWORD_SALAH -> {
                    _toastMessage.value = "Password yang Anda masukan salah"
                }
                else -> {
                    //do nothing
                }
            }
        }

        _greeting.value = getTimeOnAndroid()
    }

    private fun getTimeOnAndroid(): String {
        val c = Calendar.getInstance()
        val time = c.get(Calendar.HOUR_OF_DAY)

        return when (time) {
            in 0..10 -> "pagi"
            in 11..14 -> "siang"
            in 15..17 -> "sore"
            else -> "malam"
        }
    }

    fun onLogin() {
        val currentUsername = username.value
        val currentPassword = password.value

        if (currentUsername == null || currentUsername.isBlank()) {
            _toastMessage.value = "Username atau Password tidak boleh kosong"
            return
        }

        if (currentPassword == null || currentPassword.isBlank()) {
            _toastMessage.value = "Username atau Password tidak boleh kosong"
            return
        }

        uiScope.launch {
            checkingCredentials(currentUsername, currentPassword)
        }
    }

    fun onRegisterClick() {
        _toRegisterAction.value = true
    }

    fun doneRegisterAction() {
        _toRegisterAction.value = null
    }

    private suspend fun getUserFromDB(username:String) : User?{
        return withContext(Dispatchers.IO){
            repository.checkUsername(username)
        }
    }

    private suspend fun getUserFromDB(username: String, password: String): User? {
        return withContext(Dispatchers.IO) {
                repository.getUserData(username, password)
        }
    }

    private suspend fun checkingCredentials(username: String, password: String): Boolean {
        val isUsernameExist = getUserFromDB(username)
        if (isUsernameExist != null) {
            val dataUser = getUserFromDB(username, password)
            return if (dataUser != null) {
                Log.d(DEFAULT_TAG, shortenName(dataUser.nama))
                repository.mainSharedPreferences.edit {
                    putString(LOGGED_USER_ID,dataUser.id)
                    putString(LOGGED_USER_ROLE,dataUser.convertLevel())
                    putString(LOGGED_USER_NAME, shortenName(dataUser.nama))
                    this.apply()
                }
                _authenticationState.value = AuthenticationState.SUKSES
                true
            } else {
                _authenticationState.value = AuthenticationState.PASSWORD_SALAH
                false
            }
        } else {
            _authenticationState.value = AuthenticationState.USERNAME_SALAH
            return false
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}