package com.kirtan.shah.mycontacts.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.kirtan.shah.mycontacts.MainActivity
import com.kirtan.shah.mycontacts.R
import com.kirtan.shah.mycontacts.loginsignup.LoginSignup

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val timeout = 3000
        Handler().postDelayed({
            val intent = Intent(this@SplashScreen, LoginSignup::class.java)
            startActivity(intent)
            finish()
        }, timeout.toLong())
    }
}