package com.pedo.laporkan.ui.intro

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntroFragment
import com.pedo.laporkan.MainActivity
import com.pedo.laporkan.R

class LaporKanIntro : AppIntro(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addSlide(AppIntroFragment.newInstance(
            "Selamat Datang!",
            R.font.poppins,
            "LaporKan adalah aplikasi pengaduan masyarakat berbasis Android",
            R.font.poppins,
            R.drawable.ic_intro_image,
            ContextCompat.getColor(this,R.color.colorPrimaryDark),
            ContextCompat.getColor(this,android.R.color.white),
            ContextCompat.getColor(this,android.R.color.white)
        ))

        addSlide(AppIntroFragment.newInstance(
            "Fitur Foto",
            R.font.poppins,
            "LaporKan butuh izin Anda untuk memfoto dengan kamera",
            R.font.poppins,
            R.drawable.ic_intro_photo,
            ContextCompat.getColor(this,R.color.colorPrimaryDark),
            ContextCompat.getColor(this,android.R.color.white),
            ContextCompat.getColor(this,android.R.color.white)
        ))

        addSlide(AppIntroFragment.newInstance(
            "Fitur Report",
            R.font.poppins,
            "LaporKan butuh izin Anda untuk menyimpan report pada penyimpanan perangkat Anda",
            R.font.poppins,
            R.drawable.ic_reporting,
            ContextCompat.getColor(this,R.color.colorPrimaryDark),
            ContextCompat.getColor(this,android.R.color.white),
            ContextCompat.getColor(this,android.R.color.white)
        ))

        addSlide(AppIntroFragment.newInstance(
            "Mulai Melapor!",
            R.font.poppins,
            "Tunggu apa lagi, ayo buat akun dan mulai melapor dengan LaporKan!",
            R.font.poppins,
            R.drawable.ic_intro_start,
            ContextCompat.getColor(this,R.color.colorPrimaryDark),
            ContextCompat.getColor(this,android.R.color.white),
            ContextCompat.getColor(this,android.R.color.white)
        ))

        askForPermissions(arrayOf(Manifest.permission.CAMERA),2)
        askForPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),3)
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        finish()
        Intent(this,MainActivity::class.java).also {
            startActivity(it)
        }
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        finish()
        Intent(this,MainActivity::class.java).also {
            startActivity(it)
        }
    }
}