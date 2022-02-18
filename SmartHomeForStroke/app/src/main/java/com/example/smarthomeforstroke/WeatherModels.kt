package com.example.smarthomeforstroke

data class WEATHER(
    val response: RESPONSE
)

data class RESPONSE(
    val header : HEADER,
    val body: BODY
)

data class HEADER(
    val resultCode : Int,
    val resultMsg : String
)

data class BODY(
    val dataType : String,
    val items : ITEMS,
    val totalCount : Int
)

data class ITEMS(
    val item : List<ITEM>
)

data class ITEM(
    val category : String,
    val fcstData : String,
    val fcstTime : String,
    val fcstValue : String
)

class ModelWeather {
    var rainType = ""       // 강수 형태
    var humidity = ""       // 습도
    var sky = ""            // 하능 상태
    var temp = ""           // 기온
    var fcstTime = ""       // 예보시각
}