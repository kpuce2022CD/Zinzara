package com.example.smarthomeforstroke

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


// 결과 xml 파일에 접근해서 정보 가져오기
interface WeatherInterface {

    // getVilageFcst : 동네 예보 조회
    @GET("getUltraSrtFcst?serviceKey=c2EtQlIYpThDFFRZclkoOmGaenwXMHJsGNg%2FsF49Oidra4IQo%2Fs7moFPaGkJmTCzOZOPyfV%2FByeQEnIjVIhvBA%3D%3D")

    fun GetWeather(
        @Query("dataType") data_type : String,
        @Query("numOfRows") num_of_rows : Int,   // 한 페이지 경과 수
        @Query("pageNo") page_no : Int,          // 페이지 번호
        @Query("base_date") base_date : String,  // 발표 일자
        @Query("base_time") base_time : String,  // 발표 시각
        @Query("nx") nx : String,                // 예보지점 X 좌표
        @Query("ny") ny : String
    ): Call<WEATHER>
}