package com.example.mediaone

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {
    private val splashTimeOut: Long = 5000  // 5 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            // Create an Intent to open MainActivity
            val intent = Intent(this, MainActivity::class.java)

            // Start MainActivity
            startActivity(intent)

            // Finish the current activity (splash screen)
            finish()
        }, splashTimeOut)
    }
}
