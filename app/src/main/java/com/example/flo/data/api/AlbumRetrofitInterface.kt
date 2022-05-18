package com.example.flo.data.api

import com.example.flo.data.vo.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AlbumRetrofitInterface {
    @GET("/albums")
    fun getAlbums(): Call<AlbumResponse>

    @GET("/albums/{id}")
    fun getAlbum(@Path("id") albumId: Int): Call<BsideTracks>
}