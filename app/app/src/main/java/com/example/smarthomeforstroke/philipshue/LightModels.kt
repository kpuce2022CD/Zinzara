package com.example.smarthomeforstroke

// 모든 조명 상태 받아오기
data class ResponseGetLightIdList(
    val id:Light
)

data class Light(
    val state:State,
    val swupdate:Swupdate,
    val type: String,
    val name: String,
    val modelid: String,
    val manufacturername: String,
    val productname: String,
    val capabilities:Capabilities,
    val config: Config,
    val uniqueid: String,
    val swversion: String,
    val swconfigid: String,
    val productid: String,

    )

data class State(
    val on:Boolean,
    val bri:Int,
    val alert: String,
    val mode:String,
    val reachable: Boolean
)

data class Swupdate(
    val state: String,
    val lastinstall: String
)

data class Capabilities(
    val certified: Boolean,
    val control: Control,
    val streaming: Streaming
)

data class Control(
    val mindimlevel: Int,
    val maxlumen: Int
)

data class Streaming(
    val renderer: Boolean,
    val proxy: Boolean
)

data class Config(
    val archetype: String,
    val function: String,
    val direction: String,
    val startup: Startup
)

data class Startup(
    val mode: String,
    val configured: Boolean
)

// 조명 켜고 끄기
data class PutLight(
    val on: Boolean
)

data class PutBright(
    val bri : Int
)