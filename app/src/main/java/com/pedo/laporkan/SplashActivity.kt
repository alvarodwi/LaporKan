package com.pedo.laporkan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {
    private val mWaitHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mWaitHandler.postDelayed({
            try{
                intent = Intent(applicationContext,MainActivity::class.java)
                startActivity(intent)
            }catch(ignored : Exception){
                ignored.printStackTrace()
            }
        },1500) // 1.5 seconds
        mWaitHandler.postDelayed({
            finish()
        },3000) // 3 seconds
    }
}
