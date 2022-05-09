package com.example.smarthomeforstroke

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

interface LightAPIS {
    @GET("lights/")
    @Headers("accept: application/json", "content-type: application/json")
    fun requestGetLights(): Call<ResponseGetLightIdList>

    @PUT("lights/{id}/state")
    @Headers("accept: application/json", "content-type: application/json")
    fun requestTurnLights(
        @Path("id") id: String,
        @Body jsonparams: PutLight
    ): Call<String>


    @PUT("lights/{id}/state")
    @Headers("accept: application/json", "content-type: application/json")
    fun requestLightsBright(
        @Path("id") id: String,
        @Body jsonparams: PutBright
    ): Call<String>


    @GET("lights/{id}/")
    @Headers("accept: application/json", "content-type: application/json")
    fun requestLightsState(
        @Path("id") id: String
    ): Call<Light>

    @DELETE("lights/{id}/")
    @Headers("accept: application/json", "content-type: application/json")
    fun deleteLight(
        @Path("id") id: String
    ):Call<Void>


    companion object {
        fun create(url:String): LightAPIS {
            val gson: Gson = GsonBuilder().setLenient().create()
            val BASE_URL = "http://$url/api/npdHhwWW9zTQdOl1Vvd49xNWcGsKxmDM224akjbE/"
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(LightAPIS::class.java)
        }
    }
}