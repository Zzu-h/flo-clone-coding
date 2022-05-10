package com.example.flo.ui.user.login

import com.example.flo.data.vo.Result

interface LoginView {
    fun onLoginSuccess(code : Int, result : Result)
    fun onLoginFailure()
}