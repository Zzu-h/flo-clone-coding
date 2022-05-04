package com.example.flo.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.flo.databinding.ActivitySplashBinding
import com.example.flo.ui.main.MainActivity

@Deprecated("not use splash activity")
class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)

        setContentView(binding.root)

        Handler(Looper.getMainLooper())
            .postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
            }, 1000)
    }
}