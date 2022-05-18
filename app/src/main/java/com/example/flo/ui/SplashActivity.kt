package com.example.flo.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.flo.data.datasource.AuthNetworkDataSource
import com.example.flo.data.model.UserCode
import com.example.flo.data.model.UserCode.auth2
import com.example.flo.data.model.UserCode.jwt
import com.example.flo.data.vo.Result
import com.example.flo.databinding.ActivitySplashBinding
import com.example.flo.ui.main.MainActivity
import com.example.flo.ui.user.login.LoginActivity
import com.example.flo.ui.user.login.LoginView

//@Deprecated("not use splash activity")
class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)

        setContentView(binding.root)
        autoLogin()
    }
    fun autoLogin(){
        val spf = getSharedPreferences(auth2, MODE_PRIVATE)
        val jwt = spf.getString(jwt, null)
        if(jwt == null)
            startActivity(LoginActivity::class.java)
        else
            AuthNetworkDataSource().autoLogin(jwt, object :LoginView {
                override fun onLoginSuccess(code: Int, result: Result?) {
                    startActivity(MainActivity::class.java)
                }

                override fun onLoginFailure(message: String?) {
                    Log.d("Auto-Login", message.toString())
                    startActivity(LoginActivity::class.java)
                }
            })
    }
    private fun startActivity(cls: Class<*>){
        Handler(Looper.getMainLooper())
            .postDelayed({
                startActivity(Intent(this, cls))
                finish()
            }, 500)
    }
}