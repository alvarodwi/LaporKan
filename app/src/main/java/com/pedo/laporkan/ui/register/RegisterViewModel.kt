package com.pedo.laporkan.ui.register

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.pedo.laporkan.data.model.User
import com.pedo.laporkan.data.model.UserLevel
import com.pedo.laporkan.data.repository.MainRepository
import com.pedo.laporkan.utils.Constants.DEFAULT_TAG
import com.pedo.laporkan.utils.Constants.KODE_ADMIN
import com.pedo.laporkan.utils.Helpers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RegisterViewModel(app: Application, private val role : String) : AndroidViewModel(app) {
    private val repository = MainRepository.getInstance(app.applicationContext)

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private lateinit var incompleteUser : User

    //live data for input
    val userId = MutableLiveData<String>()
    val userFullName = MutableLiveData<String>()
    val userTelp = MutableLiveData<String>()

    val username = MutableLiveData<String>()
    val usernameErrorText = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()
    val confirmPasswordErrorText = MediatorLiveData<String>()

    val kodeAdmin = MutableLiveData<String>()

    //live data for navigation
    // 1 untuk keRegisterAkun, 2 untuk keLogin, 3 dari kodeAdmin sesuai
    private var _nextAction = MutableLiveData<Int>()
    val nextAction: LiveData<Int>
        get() = _nextAction

    private var _toKodeAdminAction = MutableLiveData<Boolean>()
    val toKodeAdminAction: LiveData<Boolean>
        get() = _toKodeAdminAction

    private var _isAdminRegistration = MutableLiveData<Boolean>()
    val isAdminRegistration : LiveData<Boolean>
        get() = _isAdminRegistration

    private var _toastMessage = MediatorLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    init {
        confirmPasswordErrorText.addSource(
            Helpers.DoubleTuple(password,confirmPassword)
        ){
            if(it.first != it.second){
                confirmPasswordErrorText.value = "Password tidak sesuai"
            }else{
                confirmPasswordErrorText.value = null
            }
        }

        _isAdminRegistration.value = role != "NON_ADMIN"
    }

    fun onRegisterDataClicked(){
        val currentUserId = userId.value
        val currentUserFullName = userFullName.value
        val currentTelp = userTelp.value

        if (currentUserId == null || currentUserId.isBlank()) {
            _toastMessage.value = "Kolom isian tidak boleh kosong"
            return
        }

        if (currentUserFullName == null || currentUserFullName.isBlank()) {
            _toastMessage.value = "Kolom isian tidak boleh kosong"
            return
        }

        if (currentTelp == null || currentTelp.isBlank()) {
            _toastMessage.value = "Kolom isian tidak boleh kosong"
            return
        }

        incompleteUser = User(id = currentUserId.trim(),nama = currentUserFullName.trim(),telp = currentTelp.trim(),level = getUserLevel())
        Log.d(DEFAULT_TAG,incompleteUser.toString())
        _nextAction.value = 1
    }

    fun onRegisterAkunClicked(){
        val currentUsername = username.value
        val currentPassword = password.value
        val currentConfirmPassword = confirmPassword.value

        if (currentUsername == null || currentUsername.isBlank()) {
            _toastMessage.value = "Kolom isian tidak boleh kosong"
            return
        }

        if (currentPassword == null || currentPassword.isBlank()) {
            _toastMessage.value = "Kolom isian tidak boleh kosong"
            return
        }

        if (currentConfirmPassword == null || currentConfirmPassword.isBlank()) {
            _toastMessage.value = "Kolom isian tidak boleh kosong"
            return
        }

        if(currentPassword != currentConfirmPassword){
            _toastMessage.value = "Password yang Anda isikan tidak konsisten"
            return
        }

        uiScope.launch {
            updateRegisterData(currentUsername.trim(),currentPassword.trim())
        }
    }

    fun onHasKodeAdminClicked(){
        _toKodeAdminAction.value = true
    }

    fun doneKodeAdminClicked(){
        _toKodeAdminAction.value = null
    }

    fun doneNextAction(){
        _nextAction.value = null
    }

    fun onKodeAdminClicked(){
        val currentKodeAdmin = kodeAdmin.value

        if (currentKodeAdmin == null || currentKodeAdmin.isBlank()) {
            _toastMessage.value = "Kolom isian tidak boleh kosong"
            return
        }

        checkKodeAdmin(currentKodeAdmin.trim())
    }

    private fun getUserLevel() : UserLevel{
        return when(role){
            "NON_ADMIN" -> UserLevel.MASYARKAT
            else -> UserLevel.ADMIN
        }
    }

    fun getIncompleteUser() : User{
        return incompleteUser
    }

    fun setIncompleteUser(data : User){
        this.incompleteUser = data
    }

    private suspend fun updateRegisterData(username : String,password :String){
        incompleteUser.let {
            it.username = username
            it.password = password

            Log.d(DEFAULT_TAG,it.toString())
            repository.createUser(incompleteUser)
        }
        _nextAction.value = 2
    }

    private fun checkKodeAdmin(kode : String){
        if(kode == KODE_ADMIN){
            _nextAction.value = 3
        }else{
            _toastMessage.value = "Kode Admin yang Anda masukan tidak sesuai"
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(val app: Application, private val role : String  = "NON_ADMIN") : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            if(modelClass.isAssignableFrom(RegisterViewModel::class.java)){
                return RegisterViewModel(
                    app,
                    role
                ) as T
            }
            throw IllegalArgumentException("Unable To Construct ViewModel")
        }

    }
}