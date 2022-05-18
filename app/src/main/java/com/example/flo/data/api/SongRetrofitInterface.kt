package com.example.flo.data.api

import com.example.flo.data.vo.AuthResponse
import com.example.flo.data.vo.SongResponse
import com.example.flo.data.vo.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SongRetrofitInterface {
    @GET("/songs")
    fun getSongs(): Call<SongResponse>
}