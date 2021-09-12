package com.example.musicappacitivity.Network

import com.example.musicappacitivity.Data.Song
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitService {

    @GET("melon/list")
    fun getSongList():Call<ArrayList<Song>>
}