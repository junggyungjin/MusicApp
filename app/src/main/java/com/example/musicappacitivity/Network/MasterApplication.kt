package com.example.musicappacitivity.Network

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MasterApplication : Application() {
    lateinit var service: RetrofitService

    override fun onCreate() {
        super.onCreate()

        createRetrofit()
    }

    fun createRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://mellowcode.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(RetrofitService::class.java)
    }
}