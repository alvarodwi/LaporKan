package com.pedo.laporkan.utils

import android.graphics.Paint
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("underLineText")
fun TextView.underLineText(flag: Boolean) {
    if (flag) {
        this.paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }
}

@BindingAdapter("berandaItemVisibilityMasyarakat")
fun View.berandaItemVisibilityMasyarakat(userRole: String) {
    when (userRole) {
        "Masyarakat", "Admin" -> {
            this.visibility = View.VISIBLE
        }
        else -> {
            this.visibility = View.GONE
        }
    }
}


@BindingAdapter("berandaItemVisibilityPetugas")
fun View.berandaItemVisibilityPetugas(userRole: String) {
    when (userRole) {
        "Petugas" -> {
            this.visibility = View.VISIBLE
        }
        else -> {
            this.visibility = View.GONE
        }
    }
}