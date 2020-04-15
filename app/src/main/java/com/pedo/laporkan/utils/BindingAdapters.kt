package com.pedo.laporkan.utils

import android.graphics.Bitmap
import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.pedo.laporkan.data.model.Laporan
import com.pedo.laporkan.ui.laporan.listing.DaftarLaporanRVAdapter
import com.pedo.laporkan.utils.Helpers.laporKanDateFormat
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

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

@BindingAdapter("loadImage")
fun ImageView.loadImage(bitmap : Bitmap?) {
    if(bitmap == null){
        this.visibility = View.GONE
    }else{
        this.visibility = View.VISIBLE
        this.setImageBitmap(bitmap)
    }
}

@BindingAdapter("printDate")
fun TextView.printDate(date : LocalDate?){
    date?.let {
        text = it.format(laporKanDateFormat).toString()
    }
}
