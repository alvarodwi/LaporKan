package com.pedo.laporkan.utils

import android.content.Context
import android.widget.Toast

object Extensions {
    fun Context.showToast(message : String, duration : Int){
        Toast.makeText(this,message,duration).show()
    }
}