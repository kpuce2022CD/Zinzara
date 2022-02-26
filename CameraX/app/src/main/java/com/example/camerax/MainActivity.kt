package com.example.camerax

import android.Manifest
import android.R.attr
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.camerax.databinding.ActivityMainBinding
import java.io.File
import java.text.SimpleDateFormat
import android.util.Base64
import java.io.ByteArrayOutputStream
import android.R.attr.bitmap





class MainActivity : AppCompatActivity() {
    // ViewBinding
    private lateinit var binding : ActivityMainBinding
    // Permissions
    private val PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private val PERMISSIONS_REQ = 100

//    private lateinit var filepath : String
    private val CAMERAX_REQUEST = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.button.setOnClickListener {
//            if(checkPermissions(PERMISSIONS, PERMISSIONS_REQ)){
//                takePictureAndGetImage()
//            }
            if(checkPermissions(PERMISSIONS, PERMISSIONS_REQ)){
                val nextIntent = Intent(this, CameraX::class.java)
                startActivityForResult(nextIntent, CAMERAX_REQUEST)
            }
        }
    }
    /* Main Code Start */
//    private fun takePictureAndGetImage() {
//        val intent = Intent(this, CameraX::class.java)
//        filepath = File(getMyExternalMediaDirs(), newJpgFileName()).absolutePath
//        intent.putExtra("filePath", filepath)
//        startActivityForResult(intent, CAMERAX_REQUEST)
//    }
    /* Main Code End */
/* Permission Code Start */

    private fun checkPermissions(permissions: Array<String>, permissionsRequest: Int): Boolean {
        val permissionList : MutableList<String> = mutableListOf()
        for(permission in permissions){
            val result = ContextCompat.checkSelfPermission(this, permission)
            if(result != PackageManager.PERMISSION_GRANTED){
                permissionList.add(permission)
            }
        }
        if(permissionList.isNotEmpty()){
            ActivityCompat.requestPermissions(this, permissionList.toTypedArray(), permissionsRequest)
            return false
        }
        return true
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for(result in grantResults){
            if(result != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "권한 승인 부탁드립니다.", Toast.LENGTH_SHORT).show()
                finish()
                return
            }
        }
    }
    /* Permission Code End */
/* On Activity Result Start */

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(resultCode == Activity.RESULT_OK){
//            when(requestCode){
//                CAMERAX_REQUEST -> {
////                    binding.imageView.setImageBitmap(BitmapFactory.decodeFile(filepath))
//                    val bitmap = BitmapFactory.decodeFile(filepath)
////                    binding.imageView.setImageBitmap(rotatedBitmap(bitmap))
//
//                    val baos = ByteArrayOutputStream()
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos)
//                    val bytes = baos.toByteArray()
//                    val temp = Base64.encodeToString(bytes, Base64.DEFAULT)
//
//                    binding.imageView.text = temp
//                }
//            }
//        }
//    }
    /* On Activity Result End */
/* MainActivity Private Method Start */
//    private fun newJpgFileName(): String {
//        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
//        val filename = sdf.format(System.currentTimeMillis())
//        return "${filename}.jpg"
//    }
//
//    private fun getMyExternalMediaDirs(): File {
//        val mediaDir = externalMediaDirs.firstOrNull()?.let {
//            File(it, resources.getString(R.string.app_name)).apply {
//                mkdirs()
//            }
//        }
//        return if (mediaDir != null && mediaDir.exists()) mediaDir
//        else filesDir
//    }

//    private fun rotatedBitmap(bitmap: Bitmap?): Bitmap? {
//        val matrix = Matrix()
//        var resultBitmap : Bitmap? = null
//        when(getOrientationOfImage(filepath)){
//            0 -> matrix.setRotate(0F)
//            90 -> matrix.setRotate(90F)
//            180 -> matrix.setRotate(180F)
//            270 -> matrix.setRotate(270F)
//        }
//        resultBitmap = try{
//            bitmap?.let { Bitmap.createBitmap(it, 0, 0, bitmap.width, bitmap.height, matrix, true) }
//        }catch (e: Exception){
//            e.printStackTrace()
//            null
//        }
//        return resultBitmap
//    }

//    private fun getOrientationOfImage(filepath : String): Int? {
//        var exif :ExifInterface? = null
//        var result: Int? = null
//        try{
//            exif = ExifInterface(filepath)
//        }catch (e: Exception){
//            e.printStackTrace()
//            return -1
//        }
//        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)
//        if(orientation != -1){
//            result = when(orientation){
//                ExifInterface.ORIENTATION_ROTATE_90 -> 90
//                ExifInterface.ORIENTATION_ROTATE_180 -> 180
//                ExifInterface.ORIENTATION_ROTATE_270 -> 270
//                else -> 0
//            }
//        }
//        return result
//    }
}
