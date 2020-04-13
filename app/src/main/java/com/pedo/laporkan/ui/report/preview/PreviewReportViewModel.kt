package com.pedo.laporkan.ui.report.preview

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pedo.laporkan.data.model.relational.LaporanAndUserWithTanggapan
import com.pedo.laporkan.data.repository.MainRepository
import com.pedo.laporkan.utils.Constants.DEFAULT_TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.time.LocalDate
import org.threeten.bp.YearMonth
import java.util.*
import kotlin.collections.ArrayList

class PreviewReportViewModel(val date : Date? = null,app : Application) : AndroidViewModel(app) {
    private val repository = MainRepository.getInstance(app.applicationContext)

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Default + viewModelJob)

    private val listDate = getFirstAndLastDayOfDateMonth(date!!)
//    val listLaporan = repository.getLaporanBetweenDate(
//        listDate[0],listDate[1]
//    )

    init {
        Log.d(DEFAULT_TAG,date.toString())
        Log.d(DEFAULT_TAG,listDate.toString())
    }

    private fun getFirstAndLastDayOfDateMonth(date : Date) : List<String>{
        val dateArr = ArrayList<String>()

        val cal = Calendar.getInstance()
        cal.time = date

        Log.d(DEFAULT_TAG,"Bulan : ${cal.get(Calendar.MONTH)}")

        val yearMonth = YearMonth.of(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1)
        val startOfMonth = yearMonth.atDay(1)
        val endOfMonth = yearMonth.atEndOfMonth()

        dateArr.add(startOfMonth.toString())
        dateArr.add(endOfMonth.toString())
        return dateArr
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(val date : Date,val app : Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            if(modelClass.isAssignableFrom(PreviewReportViewModel::class.java)){
                return PreviewReportViewModel(
                    date,
                    app
                ) as T
            }
            throw IllegalArgumentException("Cannot construct viewmodel")
        }

    }
}