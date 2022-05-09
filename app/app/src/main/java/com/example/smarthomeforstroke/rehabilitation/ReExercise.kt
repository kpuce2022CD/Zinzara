package com.example.smarthomeforstroke

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.smarthomeforstroke.databinding.ActivityReExerciseBinding
import com.example.smarthomeforstroke.databinding.ActivityReLanguageBinding
import com.example.smarthomeforstroke.sign.UserAPIS
import org.w3c.dom.Text
import retrofit2.Call
import java.util.*
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList


class ReExercise : AppCompatActivity() {

    var userAPIS = UserAPIS.create()
    private var mBinding: ActivityReExerciseBinding? = null
    private val binding get() = mBinding!!

    val PREFERENCE = "com.example.smarthomeforstroke"

    private var preview : Preview? = null
    private var imageCapture: ImageCapture? = null
    private lateinit var filepath : String

    var answer = ArrayList<String>()
    var question = ArrayList<String>()
    var num : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityReExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startCamera()

        filepath = File(getMyExternalMediaDirs(), newJpgFileName()).absolutePath

        var pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        var id = pref.getString("user_id", "")
        var pw = pref.getString("pw", "")

        Thread(Runnable {
            val img = findViewById<ImageView>(R.id.img_finger)
            val tvNum = findViewById<TextView>(R.id.tv_num)
            val intent = Intent(this, Rehabilitation::class.java)
            var cnt = 0


            for( i in 1..5) {
                val random = Random()
                num = random.nextInt(9)
                question.add(num.toString())
                runOnUiThread {
                    tvNum.text = "$i/5"
                }
                when (num) {
                    0 -> runOnUiThread {
                        img.background =
                            ContextCompat.getDrawable(this@ReExercise, R.drawable.finger0)
                    }
                    1 -> runOnUiThread {
                        img.background =
                            ContextCompat.getDrawable(this@ReExercise, R.drawable.finger1)
                    }
                    2 -> runOnUiThread {
                        img.background =
                            ContextCompat.getDrawable(this@ReExercise, R.drawable.finger2)
                    }
                    3 -> runOnUiThread {
                        img.background =
                            ContextCompat.getDrawable(this@ReExercise, R.drawable.finger3)
                    }
                    4 -> runOnUiThread {
                        img.background =
                            ContextCompat.getDrawable(this@ReExercise, R.drawable.finger4)
                    }
                    5 -> runOnUiThread {
                        img.background =
                            ContextCompat.getDrawable(this@ReExercise, R.drawable.finger5)
                    }
                    6 -> runOnUiThread {
                        img.background =
                            ContextCompat.getDrawable(this@ReExercise, R.drawable.finger6)
                    }
                    7 -> runOnUiThread {
                        img.background =
                            ContextCompat.getDrawable(this@ReExercise, R.drawable.finger7)
                    }
                    8 -> runOnUiThread {
                        img.background =
                            ContextCompat.getDrawable(this@ReExercise, R.drawable.finger8)
                    }
                }
                Thread.sleep(5000)

                takePicture(filepath!!)

            }
            var builder = AlertDialog.Builder(this@ReExercise)
            builder.setTitle("조금만 기다려주세요")
            builder.setMessage("점수를 저장합니다.")
            runOnUiThread{
                builder.show()
            }

            Thread.sleep(5000)
            for (i in 0..4){
                if (answer[i] == question[i]){
                    cnt++
                }
            }

            val reExerciseSend = ReExerciseSend(id.toString(), pw.toString(), cnt, "")
            userAPIS.postReExercise(reExerciseSend).enqueue(object : Callback<ReExerciseInfo>{
                override fun onResponse(
                    call: Call<ReExerciseInfo>,
                    response: Response<ReExerciseInfo>
                ) {
                    var builder = AlertDialog.Builder(this@ReExercise)
                    builder.setTitle("점수 저장 되었습니다")
                    builder.setMessage(cnt.toString() + "점이에요!♡")
                    builder.show()
                    Thread.sleep(3000)
                    startActivity(intent)
                    finish()
                }

                override fun onFailure(call: Call<ReExerciseInfo>, t: Throwable) {
                    errorDialog("점수저장", t)
                }

            })

        }).start()

    }
    fun errorDialog(msg: String, t: Throwable){
        val dialog = AlertDialog.Builder(this)
        Log.e(msg, t.message.toString())
        dialog.setTitle("$msg 에러")
        dialog.setMessage("호출실패했습니다.")
        dialog.show()
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
                    val bitmap = BitmapFactory.decodeFile(filepath)
                    val resized = Bitmap.createScaledBitmap(bitmap, 256, 256, true)
                    val baos = ByteArrayOutputStream()
                    resized.compress(Bitmap.CompressFormat.JPEG, 60, baos)
                    val bytes = baos.toByteArray()
                    val temp = Base64.encodeToString(bytes, Base64.NO_WRAP)

                    Log.d("temp", temp.toString())

                    //temp가 Base64

                    val ImgInfo = ImgInfo(temp)
                    ImgInfo.img = temp

                    userAPIS.postImgImfo(ImgInfo).enqueue(object : Callback<String>{
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            if (response.code() == 210){
                                toast("0")
                                answer.add("0")
                            }
                            else if (response.code() == 211){
                                toast("1")
                                answer.add("1")
                            }
                            else if (response.code() == 212){
                                toast("2")
                                answer.add("2")
                            }
                            else if (response.code() == 213){
                                toast("3")
                                answer.add("3")
                            }
                            else if (response.code() == 214){
                                toast("4")
                                answer.add("4")
                            }
                            else if (response.code() == 215){
                                toast("5")
                                answer.add("5")
                            }
                            else if (response.code() == 216){
                                toast("6")
                                answer.add("6")
                            }
                            else if (response.code() == 217){
                                toast("7")
                                answer.add("7")
                            }
                            else if (response.code() == 218){
                                toast("8")
                                answer.add("8")
                            }
                            else if (response.code() == 219){
                                toast("9")
                                answer.add("9")
                            }
                            else {
                                toast("손가락 인식 실패")
                                answer.add("error")
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


    fun toast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}