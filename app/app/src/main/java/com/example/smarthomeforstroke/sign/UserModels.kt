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

data class ReExerciseSend(
    var user_id : String,
    var pw : String,
    var physical_score : Int,
    var rehabilitation_time : String
)

data class ReExerciseInfo(
    var user_id : String,
    var physical_score : Int,
    var rehabilitation_time : String
)

data class ReExerciseResponse(
    var exerciseScores : List<ReExerciseInfo>
)

data class ImgInfo(
    var img : String
)

data class ReLanguageInfo(
    var user_id : String,
    var language_score : Int,
    var rehabilitation_time: String
)

data class ReLanguageResponse(
    var languageScores : List<ReLanguageInfo>
)

data class UserId(
    var user_id: String
)


