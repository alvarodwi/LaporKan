package com.pedo.laporkan.utils

import android.graphics.Paint
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("underLineText")
fun TextView.underLineText(flag : Boolean) {
    if(flag){
        this.paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }
}