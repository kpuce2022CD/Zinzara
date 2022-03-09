package com.example.smarthomeforstroke

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.smarthomeforstroke.databinding.ActivitySmarthomeBinding
import com.example.smarthomeforstroke.sign.UserAPIS
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


val gson : Gson = GsonBuilder()
    .setLenient()
    .create()

// retrofit을 사용하기 위한 빌더 생성
private val retrofit = Retrofit.Builder()
    .baseUrl("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/")
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()

object ApiObject {
    val retrofitService: WeatherInterface by lazy {
        retrofit.create(WeatherInterface::class.java)
    }
}


class SmartHome : AppCompatActivity() {

    var userAPIS = UserAPIS.create()
    private lateinit var binding : ActivitySmarthomeBinding

    private var base_date = "20210703"  // 발표 일자
    private var base_time = "1400"      // 발표 시각
    var nx = "55"               // 예보지점 X 좌표
    var ny = "127"              // 예보지점 Y 좌표

    private var preview : Preview? = null
    private var imageCapture: ImageCapture? = null
    private lateinit var filepath : String

    private var BASE_URL:String? = null
    var lightApi: LightAPIS? = null
    var groupAPI: GroupAPIS? = null

    var light: String? = null
    var lightLight: Light? = null
    var lightState: State? = null
    var philipsHueApi = PhilipsHueAPIS.create()
    var idAndIp: List<ResponseGetIP>? = null
    var lightId: String = "1"
    var lightIds: ArrayList<String>? = null
    var lightNum = 0

    var lightOnOff : String? = null

    var orderNum = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySmarthomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        startCamera()

        filepath = File(getMyExternalMediaDirs(), newJpgFileName()).absolutePath

        binding.imageViewPhoto.setOnClickListener {
            takePicture(filepath!!)
        }

        philipsHueApi.requestGetIPAddress().enqueue(object : Callback<List<ResponseGetIP>>{
            override fun onResponse(call: Call<List<ResponseGetIP>>, response: Response<List<ResponseGetIP>>) {
                idAndIp = response.body()
                Log.d("PHILIPS HUE", "id : " + idAndIp?.get(0)?.id)
                Log.d("PHILIPS HUE", "internalipaddress : " + idAndIp?.get(0)?.internalipaddress)
                Log.d("PHILIPS HUE", "port : " + idAndIp?.get(0)?.port)
                BASE_URL = idAndIp?.get(0)?.internalipaddress.toString()
                lightApi = LightAPIS.create(BASE_URL!!)
                groupAPI = GroupAPIS.create(BASE_URL!!)

                getLightsId()
            }

            override fun onFailure(call: Call<List<ResponseGetIP>>, t: Throwable) {
                Log.e("PHILIPS HUE", t.message.toString())
            }
        })

    }

    fun setWeather(nx : String, ny : String) {
        // 준비 단계 : base_date(발표 일자), base_time(발표 시각)
        // 현재 날짜, 시간 정보 가져오기
        setTime()

        // 날씨 정보 가져오기
        // (응답 자료 형식-"JSON", 한 페이지 결과 수 = 10, 페이지 번호 = 1, 발표 날싸, 발표 시각, 예보지점 좌표)
        val call = ApiObject.retrofitService.GetWeather("JSON",60, 1, base_date, base_time, nx, ny)

        // 비동기적으로 실행하기
        call.enqueue(object : retrofit2.Callback<WEATHER> {
            // 응답 성공 시
            override fun onResponse(call: Call<WEATHER>, response: Response<WEATHER>) {
                if (response.body()!!.response.header.resultCode == 0){
                    Log.d("에러", response.toString())
                    Log.d("에러", response.body().toString())

                    // 날씨 정보 가져오기
                    var it: List<ITEM> = response.body()!!.response.body.items.item

                    var rainRatio = ""      // 강수 확률
                    var rainType = ""       // 강수 형태
                    var humidity = ""       // 습도
                    var sky = ""            // 하능 상태
                    var temp = ""           // 기온
                    for (i in 0 until response.body()!!.response.body.totalCount) {
                        when(it[i].category) {
                            "T1H" -> temp = it[i].fcstValue         // 기온
                            "RN1" -> rainRatio = it[i].fcstValue
                            "SKY" -> sky = it[i].fcstValue          // 하늘 상태
                            "REH" -> humidity = it[i].fcstValue     // 습도
                            "PTY" -> rainType = it[i].fcstValue     // 강수 형태
                            else -> continue
                        }
                    }
                    // 강수 형태
                    var result1 = ""
                    when(rainType) {
                        "0" -> result1 = "없음"
                        "1" -> result1 = "비"
                        "2" -> result1 = "비/눈"
                        "3" -> result1 = "눈"
                        "5" -> result1 = "빗방울"
                        "6" -> result1 = "빗방울/눈날림"
                        "7" -> result1 = "눈날림"
                        else -> "오류"
                    }
                    // 하늘 상태
                    var result2 = ""
                    when(sky) {
                        "1" -> result2 = "맑음"
                        "3" -> result2 = "구름 많음"
                        "4" -> result2 = "흐림"
                        else -> "오류"
                    }
                    binding.btnWeather.text = it[1].fcstValue

                    val dialog = AlertDialog.Builder(this@SmartHome)
                    dialog.setTitle("오늘의 날씨")
                    dialog.setMessage("강수 확률 : $rainRatio%, $result1, 습도 : $humidity%, $result2, 온도 : $temp°")
                    dialog.show()

                    Toast.makeText(applicationContext, base_date + ", " + base_time + "의 날씨 정보입니다.", Toast.LENGTH_SHORT).show()
                }
            }

            // 응답 실패 시
            override fun onFailure(call: Call<WEATHER>, t: Throwable) {
                Log.d("api fail", t.message.toString())
            }
        })
    }

    private fun setTime(){
        val cal = Calendar.getInstance()
        base_date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time) // 현재 날짜
        val time = SimpleDateFormat("HH", Locale.getDefault()).format(cal.time) // 현재 시각
        // API 가져오기 적당하게 변환

        if (time == "00"){
            cal.add(Calendar.DATE, -1).toString()
            base_time = "2300"
            base_date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)
        }
        else{
            base_time = time + "00"
        }
    }

    private fun takePicture(filepath : String) {
        val photoFile = File(filepath)
// Create output options object which contains file + metadata
        val outputOptions = ImageCapture
            .OutputFileOptions
            .Builder(photoFile)
            .build()
// Set up image capture listener, which is triggered after photo has
// been taken
        imageCapture?.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e("CameraX_Debug", "Photo capture failed: ${exc.message}", exc)
                }
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
//                    val resultIntent = Intent()
//                    setResult(Activity.RESULT_OK, resultIntent)
//                    finish()
                    val bitmap = BitmapFactory.decodeFile(filepath)
//                    binding.imageView.setImageBitmap(rotatedBitmap(bitmap))

                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos)
                    val bytes = baos.toByteArray()
                    val temp = Base64.encodeToString(bytes, Base64.DEFAULT)

                    Log.d("temp", temp.toString())

                    //temp가 Base64

                    val ImgInfo = ImgInfo(temp)
                    ImgInfo.img = temp

                    userAPIS.postImgImfo(ImgInfo).enqueue(object : Callback<String>{
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            var deviceNum = 210 + lightNum
                            if(response.code() in 211..deviceNum){
                                toast((response.code()-210).toString()+"번 기기")
                                orderNum = response.code()-210
                            }
                            else if (response.code() == 210){
                                toast("0")
                                lightOnOffState(orderNum.toString())
                                var handler = Handler()
                                handler.postDelayed(
                                    Runnable {
                                        if (orderNum in 1..deviceNum-210 && lightOnOff == "1" ){
                                            setlightOnOff(false, orderNum.toString())
                                        }
                                    }, 500
                                )
                            }
                            else if (response.code() == 211){
                                toast("1")
                            }
                            else if (response.code() == 212){
                                toast("2")
                            }
                            else if (response.code() == 213){
                                toast("3")
                            }
                            else if (response.code() == 214){
                                toast("4")
                            }
                            else if (response.code() == 215){
                                toast("5")
                                lightOnOffState(orderNum.toString())
                                var handler = Handler()
                                handler.postDelayed(
                                    Runnable {
                                        if (orderNum in 1..deviceNum-210 && lightOnOff == "0"){
                                            setlightOnOff(true, orderNum.toString())
                                        }
                                    }, 500
                                )
                            }
                            else if (response.code() == 216){
                                toast("6")
                            }
                            else if (response.code() == 217){
                                toast("7")
                            }
                            else if (response.code() == 218){
                                toast("8")
                                setWeather(nx, ny)
                            }
                            else if (response.code() == 219){
                                toast("9")
                            }
                            else {
                                toast("손가락 모양을 인식할 수 없습니다.")
                            }
                        }
                        override fun onFailure(call: Call<String>, t: Throwable) {
                            errorDialog("ImgPost", t)
                        }

                    })
                }
            })
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
// Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
// Preview
            preview = Preview.Builder()
                .setTargetRotation(windowManager.defaultDisplay.rotation)
                .build()
                .also {
                    it.setSurfaceProvider(binding.preView.surfaceProvider)
                }
// ImageCapture
            imageCapture = ImageCapture.Builder()
                .build()
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build()
            try {
// Unbind use cases before rebinding
                cameraProvider.unbindAll()
// Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture)
            } catch(exc: Exception) {
                Log.e("CameraX_Debug", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun getOrientationOfImage(filepath : String): Int? {
        var exif : ExifInterface? = null
        var result: Int? = null
        try{
            exif = ExifInterface(filepath)
        }catch (e: Exception){
            e.printStackTrace()
            return -1
        }
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)
        if(orientation != -1){
            result = when(orientation){
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
        }
        return result
    }

    private fun rotatedBitmap(bitmap: Bitmap?): Bitmap? {
        val matrix = Matrix()
        var resultBitmap : Bitmap? = null
        when(getOrientationOfImage(filepath)){
            0 -> matrix.setRotate(0F)
            90 -> matrix.setRotate(90F)
            180 -> matrix.setRotate(180F)
            270 -> matrix.setRotate(270F)
        }
        resultBitmap = try{
            bitmap?.let { Bitmap.createBitmap(it, 0, 0, bitmap.width, bitmap.height, matrix, true) }
        }catch (e: Exception){
            e.printStackTrace()
            null
        }
        return resultBitmap
    }

    private fun newJpgFileName(): String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())
        return "${filename}.jpg"
    }

    private fun getMyExternalMediaDirs(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir
        else filesDir
    }


    fun lightOnOffState(lightId:String) {
        lightApi?.requestLightsState(lightId)?.enqueue(object : Callback<Light>{
            override fun onResponse(call: Call<Light>, response: Response<Light>) {
                lightLight = response.body()
                lightState = lightLight?.state
                var on:Boolean = lightState!!.on
                if (on){
                    lightOnOff = "1"
                    Log.d("light state", lightOnOff.toString())
                }
                else{
                    lightOnOff = "0"
                    Log.d("light state", lightOnOff.toString())
                }

            }
            override fun onFailure(call: Call<Light>, t: Throwable) {
                errorDialog("STATE", t)
            }
        })
    }

    fun setlightOnOff(setLight:Boolean, id: String){
        val data = PutLight(setLight)
        lightApi?.requestTurnLights(id, data)?.enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                light = response.body()
                Log.d("LIGHT", "state on: " + light?.toString())
//                binding.tvInfo.text = light?.toString()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                errorDialog("LIGHT", t)
            }
        })
    }

    fun lightBright(lightId:String){
        lightApi?.requestLightsState(lightId)?.enqueue(object : Callback<Light>{
            override fun onResponse(call: Call<Light>, response: Response<Light>) {
                lightLight = response.body()
                lightState = lightLight?.state
//                var bri:Int = lightState!!.bri
//                binding.seekBar.progress = bri
            }

            override fun onFailure(call: Call<Light>, t: Throwable) {
                errorDialog("BRIGHT GET", t)
            }
        })
    }

    fun lightBrightControl(setLight:Int, id:String){
        val data = PutBright(setLight)
        lightApi?.requestLightsBright(id, data)?.enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                light = response.body()

                Log.d("BRIGHT PUT", "state on: $light")
//                binding.briInfo.text = light
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                errorDialog("BRIGHT PUT", t)
            }
        })

    }

    fun getLightsId() {
        groupAPI?.requestGetLightsId()?.enqueue(object : Callback<GroupInfo>{
            override fun onResponse(call: Call<GroupInfo>, response: Response<GroupInfo>) {
                val groupInfo = response.body()
                lightIds = groupInfo?.lights!!
//                binding.lightId.text = lightIds!!.size.toString() + lightIds.toString()
                lightNum=lightIds!!.size
            }
            override fun onFailure(call: Call<GroupInfo>, t: Throwable) {
                errorDialog("GROUP", t)
            }
        })
    }

    fun errorDialog(msg: String, t: Throwable){
        val dialog = AlertDialog.Builder(this)
        Log.e(msg, t.message.toString())
        dialog.setTitle("$msg 에러")
        dialog.setMessage("호출실패했습니다.")
        dialog.show()
    }
    fun toast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
