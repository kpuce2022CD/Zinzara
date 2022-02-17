package com.example.smarthomeforstroke

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.smarthomeforstroke.databinding.ActivitySmarthomeBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

data class WEATHER (val response : RESPONSE)
data class RESPONSE(val header : HEADER, val body : BODY)
data class HEADER(val resultCode : Int, val resultMsg : String)
data class BODY(val dataType : String, val items : ITEMS)
data class ITEMS(val item : List<ITEM>)
// category : 자료 구분 코드, fcstDate : 예측 날짜, fcstTime : 예측 시간, fcstValue : 예보 값
data class ITEM(val category : String, val fcstDate : String, val fcstTime : String, val fcstValue : String)


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

    lateinit private var binding : ActivitySmarthomeBinding
    private var preview : Preview? = null
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    private var camera : Camera? = null
    private var cameraController : CameraControl? = null
    private var cameraInfo: CameraInfo? = null

    private var rainRatio : String? = null
    private var rainType : String? = null
    private var humidity : String? = null
    private var sky : String? = null
    private var temp : String? = null


    var base_date = "20210703"  // 발표 일자
    var base_time = "1400"      // 발표 시각
    var nx = "55"               // 예보지점 X 좌표
    var ny = "127"              // 예보지점 Y 좌표


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySmarthomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        binding.btnWeather.setOnClickListener {
            setWeather(nx, ny)

        }




        startCamera()
        binding.cameraCaptureButton.setOnClickListener {
            takePhoto()
        }
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()

        binding.viewFinder.setOnTouchListener { v : View, event : MotionEvent ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.performClick()
                    return@setOnTouchListener true
                }
                MotionEvent.ACTION_UP -> {
// Get the MeteringPointFactory from PreviewView
                    val factory = binding.viewFinder.meteringPointFactory
// Create a MeteringPoint from the tap coordinates
                    val point = factory.createPoint(event.x, event.y)
// Create a MeteringAction from the MeteringPoint, you can configure it to specify the metering mode
                    val action = FocusMeteringAction.Builder(point).build()
// Trigger the focus and metering. The method returns a ListenableFuture since the operation
// is asynchronous. You can use it get notified when the focus is successful or if it fails.
                    cameraController?.startFocusAndMetering(action)
                    v.performClick()
                    return@setOnTouchListener true
                }
                else -> return@setOnTouchListener false
            }
        }




    }

    fun setWeather(nx : String, ny : String) {
        // 준비 단계 : base_date(발표 일자), base_time(발표 시각)
        // 현재 날짜, 시간 정보 가져오기
        val cal = Calendar.getInstance()
        base_date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time) // 현재 날짜
        val timeH = SimpleDateFormat("HH", Locale.getDefault()).format(cal.time) // 현재 시각
        val timeM = SimpleDateFormat("HH", Locale.getDefault()).format(cal.time) // 현재 분
        // API 가져오기 적당하게 변환
        base_time = getBaseTime(timeH, timeM)
        // 동네예보  API는 3시간마다 현재시간+4시간 뒤의 날씨 예보를 알려주기 때문에
        // 현재 시각이 00시가 넘었다면 어제 예보한 데이터를 가져와야함
        if (timeH == "00" && base_time == "2330") {
            cal.add(Calendar.DATE, -1).toString()
            base_date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)
        }
        // 날씨 정보 가져오기
        // (응답 자료 형식-"JSON", 한 페이지 결과 수 = 10, 페이지 번호 = 1, 발표 날싸, 발표 시각, 예보지점 좌표)
        val call = ApiObject.retrofitService.GetWeather(60, 1, "JSON", base_date, base_time, nx, ny)

        // 비동기적으로 실행하기
        call.enqueue(object : retrofit2.Callback<WEATHER> {
            // 응답 성공 시
            override fun onResponse(call: Call<WEATHER>, response: Response<WEATHER>) {
                if (response.isSuccessful) {
                    // 날씨 정보 가져오기
                    var it: List<ITEM> = response.body()!!.response.body.items.item

                    var rainRatio = ""      // 강수 확률
                    var rainType = ""       // 강수 형태
                    var humidity = ""       // 습도
                    var sky = ""            // 하능 상태
                    var temp = ""           // 기온
                    for (i in 0..9) {
                        when(it[i].category) {
                            "POP" -> rainRatio = it[i].fcstValue    // 강수 기온
                            "PTY" -> rainType = it[i].fcstValue     // 강수 형태
                            "REH" -> humidity = it[i].fcstValue     // 습도
                            "SKY" -> sky = it[i].fcstValue          // 하늘 상태
                            "T3H" -> temp = it[i].fcstValue         // 기온
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
                        "4" -> result1 = "소나기"
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


                    binding.btnWeather.text = it[1].fcstValue.toString()

                    /*
                    val dialog = AlertDialog.Builder(this@SmartHome)
                    dialog.setTitle("오늘의 날씨")
                    dialog.setMessage("강수 확률 : $rainRatio%, $result1, 습도 : $humidity%, $result2, 온도 : $temp°")
                    dialog.show()
*/
                    // 토스트 띄우기
                    Toast.makeText(applicationContext, it[0].fcstDate + ", " + it[0].fcstTime + "의 날씨 정보입니다.", Toast.LENGTH_SHORT).show()
                }
            }

            // 응답 실패 시
            override fun onFailure(call: Call<WEATHER>, t: Throwable) {
                Log.d("api fail", t.message.toString())
            }
        })
    }



    private fun getBaseTime(h : String, m : String) : String {
        var result = ""

        // 45분 전이면
        if (m.toInt() < 45) {
            // 0시면 2330
            if (h == "00") result = "2330"
            // 아니면 1시간 전 날씨 정보 부르기
            else {
                var resultH = h.toInt() - 1
                // 1자리면 0 붙여서 2자리로 만들기
                if (resultH < 10) result = "0" + resultH + "30"
                // 2자리면 그대로
                else result = resultH.toString() + "30"
            }
        }
        // 45분 이후면 바로 정보 받아오기
        else result = h + "30"

        return result
    }



    private fun takePhoto() {
// Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return
// Create time-stamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            newJpgFileName())
// Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
// Set up image capture listener, which is triggered after photo has
// been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.d("CameraX-Debug", "Photo capture failed: ${exc.message}", exc)
                }
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo capture succeeded: $savedUri"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d("CameraX-Debug", msg)
                }
            })
    }



    // viewFinder 설정 : Preview
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
// Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
// Preview
            preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }
// ImageCapture
            imageCapture = ImageCapture.Builder()
                .build()
// Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
            try {
// Unbind use cases before rebinding
                cameraProvider.unbindAll()
// Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture)

                cameraController = camera!!.cameraControl
                cameraInfo = camera!!.cameraInfo

            } catch(exc: Exception) {
                Log.d("CameraX-Debug", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }


    private fun newJpgFileName() : String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())
        return "${filename}.jpg"
    }
    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir
        else filesDir
    }
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
