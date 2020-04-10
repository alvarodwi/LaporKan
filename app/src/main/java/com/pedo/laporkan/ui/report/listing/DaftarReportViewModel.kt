package com.pedo.laporkan.ui.report.listing

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pedo.laporkan.data.repository.MainRepository
import com.pedo.laporkan.utils.Constants.DEFAULT_TAG
import kotlinx.coroutines.*
import java.util.*

class DaftarReportViewModel(app : Application) : AndroidViewModel(app) {
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage : LiveData<String>
        get() = _toastMessage

    val selectedMonth = MutableLiveData<Date>()
    val showMonthSelector = MutableLiveData<Boolean>()

    fun onBulanIniClicked(){
        selectedMonth.value = Date()
    }

    fun resetAction(){
        selectedMonth.value = null
        showMonthSelector.value = null
    }

    fun onBulananClicked(){
        showMonthSelector.value = true
    }
}