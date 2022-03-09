package com.example.smarthomeforstroke

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

interface PhilipsHueAPIS {
    @GET("/")
    @Headers("accept: application/json", "content-type: application/json")
    fun requestGetIPAddress(): Call<List<ResponseGetIP>>

    companion object {
        private const val BASE_URL = "https://discovery.meethue.com"
        fun create(): PhilipsHueAPIS {

            var gson: Gson = GsonBuilder().setLenient().create()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(PhilipsHueAPIS::class.java)
        }
    }
}