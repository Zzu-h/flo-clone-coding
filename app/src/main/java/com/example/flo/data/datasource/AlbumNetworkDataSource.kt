package com.example.flo.data.datasource

import android.util.Log
import com.example.flo.data.api.AlbumRetrofitInterface
import com.example.flo.data.vo.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlbumNetworkDataSource {
    private val api = getRetrofit().create(AlbumRetrofitInterface::class.java)

    fun getAlbums(albumCallback: AlbumInterface){
        val getSongList = api.getAlbums()

        getSongList.enqueue(object : Callback<AlbumResponse>{
            override fun onResponse(call: Call<AlbumResponse>, response: Response<AlbumResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val albumsResponse: AlbumResponse = response.body()!!

                    when (albumsResponse.code) {
                        1000 -> {
                            albumCallback.onLoadSuccess(albumsResponse.result.songs)
                        }

                        else -> albumCallback.onLoadFailure(albumsResponse.message)
                    }
                }
            }

            override fun onFailure(call: Call<AlbumResponse>, t: Throwable) {
                albumCallback.onLoadFailure("네트워크 오류가 발생했습니다.")
            }
        })
    }

    fun getBsideTracks(albumId: Int, trackInterface: BsideTrackInterface){
        val getSongList = api.getAlbum(albumId)

        getSongList.enqueue(object : Callback<BsideTracks>{
            override fun onResponse(call: Call<BsideTracks>, response: Response<BsideTracks>) {
                if (response.isSuccessful && response.code() == 200) {
                    val albumsResponse: BsideTracks = response.body()!!
                    Log.d("AlbumTracks",albumsResponse.toString())
                    when (albumsResponse.code) {
                        1000 -> {
                            trackInterface.onLoadSuccess(albumsResponse.result)
                        }

                        else -> trackInterface.onLoadFailure(albumsResponse.message)
                    }
                }
            }

            override fun onFailure(call: Call<BsideTracks>, t: Throwable) {
                trackInterface.onLoadFailure("네트워크 오류가 발생했습니다.")
            }
        })
    }
    interface BsideTrackInterface {
        fun onLoadSuccess(albumList: List<TrackSong>)
        fun onLoadFailure(message: String?)
    }
    interface AlbumInterface {
        fun onLoadSuccess(albumList: List<Album>)
        fun onLoadFailure(message: String?)
    }
}