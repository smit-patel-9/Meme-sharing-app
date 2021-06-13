package com.example.memesharingappinkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager

class splashActivity : AppCompatActivity() {

    private val SPLASH_SCREEN_TIME_OUT = 1500L

    override fun onCreate(savedInstanceState: Bundle?) {

        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        Handler().postDelayed(Runnable {
            val intent =  Intent(this@splashActivity , MainActivity::class.java)
            startActivity(intent)
            finish()
        },SPLASH_SCREEN_TIME_OUT)
    }
}