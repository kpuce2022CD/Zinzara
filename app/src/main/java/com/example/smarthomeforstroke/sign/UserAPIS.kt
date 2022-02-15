package com.example.smarthomeforstroke.sign

import com.example.smarthomeforstroke.SignInInfo
import com.example.smarthomeforstroke.SignUpInfo
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

interface UserAPIS {
    @POST("/login/")
    @Headers("accept: application/json", "content-type: application/json")
    fun requestSignIn(
        @Body jsonparams: SignInInfo
    ):Call<String>

    @POST("/members/")
    @Headers("accept: application/json", "content-type: application/json")
    fun requestSignUp(
        @Body jsonparams: SignUpInfo
    ):Call<SignUpInfo>


    companion object{
        private const val ipv4 = "172.30.1.23"
        private const val port = "8000"
        fun create(): UserAPIS{
            val gson: Gson = GsonBuilder().setLenient().create()
            val BASE_URL = "http://$ipv4:$port"
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(UserAPIS::class.java)
        }

    }
}