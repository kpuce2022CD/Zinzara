package com.example.smarthomeforstroke.sign

import com.example.smarthomeforstroke.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*
import retrofit2.http.Body

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

    @POST("/physical-rehabilitation/")
    @Headers("accept: application/json", "content-type: application/json")
    fun postReExercise(
        @Body jsonparams: ReExerciseSend
    ):Call<ReExerciseInfo>

    @POST("/physical-rehabilitation/")
    @Headers("accept: application/json", "content-type: application/json")
    fun getReExercise(
        @Body jsonparams: UserInfo
    ):Call<ReExerciseResponse>

    @POST("/gestures/")
    @Headers("accept: application/json", "content-type: application/json")
    fun postImgImfo(
        @Body jsonparams: ImgInfo
    ):Call<String>

    @POST("/language-rehabilitation/")
    @Headers("accept: application/json", "content-type: application/json")
    fun getReLanguage(
        @Body jsonparams: UserInfo
    ):Call<ReLanguageResponse>

    @PUT("/duplicate-check/")
    @Headers("accept: application/json", "content-type: application/json")
    fun putDuplicate(
        @Body jsonparams: UserId
    ):Call<String>

    @POST("/members-info/")
    @Headers("accept: application/json", "content-type: application/json")
    fun postUserInfo(
        @Body jsonparams: UserInfo
    ):Call<SignUpInfo>


    companion object{
        private const val ipv4 = "3.37.253.204"
        private const val port = "80"
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