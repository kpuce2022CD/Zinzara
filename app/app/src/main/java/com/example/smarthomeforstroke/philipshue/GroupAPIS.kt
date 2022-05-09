package com.example.smarthomeforstroke

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

interface GroupAPIS {
    @GET("groups/1/")
    @Headers("accept: application/json", "content-type: application/json")
    fun requestGetLightsId(): Call<GroupInfo>

    companion object {
        fun create(url:String): GroupAPIS {
            val gson: Gson = GsonBuilder().setLenient().create()
            val BASE_URL = "http://$url/api/npdHhwWW9zTQdOl1Vvd49xNWcGsKxmDM224akjbE/"
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(GroupAPIS::class.java)
        }
    }
}