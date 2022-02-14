package com.example.smarthomeforstroke

data class SignInInfo(
    var user_id : String,
    var pw : String
)

data class SignUpInfo(
    var user_id : String,
    var pw : String,
    var phone_number : String,
    var created : String?
)

data class UserInfo(
    var user_id : String,
    var pw : String
)

