package com.example.flo.data.datasource

import android.util.Log
import androidx.core.view.isVisible
import com.example.flo.data.api.AuthRetrofitInterface
import com.example.flo.data.vo.AuthResponse
import com.example.flo.data.vo.User
import com.example.flo.ui.user.login.LoginView
import com.example.flo.ui.user.signup.SignUpView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.sign

class AuthNetworkDataSource {
    companion object TAG{
        val success = "SIGNUP/SUCCESS"
        val fail = "SIGNUP/FAILURE"
    }
    private val api = getRetrofit().create(AuthRetrofitInterface::class.java)

    fun signUp(user: User, signUpView: SignUpView){
        val signUpService = api.signUp(user)

        signUpService.enqueue(object : Callback<AuthResponse> {
            override fun onFailure(call: Call<AuthResponse>, t: Throwable)
                = signUpView.onSignUpFailure(t.message)
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>){
                val resp = response.body() as AuthResponse
                when(resp.code){
                    1000 -> signUpView.onSignUpSuccess()
                    else -> signUpView.onSignUpFailure(resp.message)
                }
            }
        })
    }

    fun login(user: User, loginView: LoginView) {
        api.login(user).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val loginResponse: AuthResponse = response.body()!!

                    when (val code = loginResponse.code) {
                        1000 -> loginView.onLoginSuccess(code,loginResponse.result!! )
                        else -> loginView.onLoginFailure(response.message())
                    }
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable)
                = loginView.onLoginFailure(t.message)
        })
    }
    fun autoLogin(jwt: String, loginView: LoginView) {
        api.autoLogin(jwt).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val loginResponse: AuthResponse = response.body()!!

                    when (val code = loginResponse.code) {
                        1000 -> loginView.onLoginSuccess(code, null )
                        else -> loginView.onLoginFailure(response.message())
                    }
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable)
                    = loginView.onLoginFailure(t.message)
        })
    }
}