package com.pedo.laporkan.ui.splash

import android.app.Application
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pedo.laporkan.data.repository.MainRepository
import com.pedo.laporkan.utils.Constants
import com.pedo.laporkan.utils.Constants.SharedPrefKey.IS_FIRST_OPEN

class SplashViewModel(app : Application) : AndroidViewModel(app) {
    private val repository = MainRepository.getInstance(app.applicationContext)

    //1 to intro, 2 to login, 3 to beranda
    private val _nextAction = MutableLiveData<Int>()
    val nextAction : LiveData<Int>
        get() = _nextAction

    init {
        if(isFirstTimeAppOpened()){
            _nextAction.value = 1
            this.resetFirstTimeAppOpened()
        }else{
            if (!checkIfUserHasnotLogin()){
                _nextAction.value = 3
            }else{
                _nextAction.value = 2
            }
        }
    }

    //return true if user hasn't logged in or had logged out
    private fun checkIfUserHasnotLogin() : Boolean{
        return repository.mainSharedPreferences.getString(Constants.SharedPrefKey.LOGGED_USER_ID,null) == null
    }

    //return true if yes, false if not
    private fun isFirstTimeAppOpened() : Boolean{
        return repository.mainSharedPreferences.getBoolean(IS_FIRST_OPEN,true)
    }

    private fun resetFirstTimeAppOpened(){
        repository.mainSharedPreferences.edit {
            putBoolean(IS_FIRST_OPEN,false)
            this.apply()
        }
    }

    fun resetNextAction(){
        _nextAction.value = null
    }
}