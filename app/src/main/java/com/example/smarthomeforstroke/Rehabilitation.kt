package com.example.smarthomeforstroke

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smarthomeforstroke.databinding.ActivityRehabilitationBinding
import com.example.smarthomeforstroke.databinding.ActivitySmarthomeBinding


class Rehabilitation  : AppCompatActivity() {

    private var mBinding: ActivityRehabilitationBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRehabilitationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRemedialExercise.setOnClickListener {
            val intent = Intent(this, ReLanguage::class.java)
            startActivity(intent)
        }
        binding.btnRemedialLanguage.setOnClickListener {
            val intent = Intent(this, ReExercise::class.java)
            startActivity(intent)
        }




    }

    override fun onDestroy() { // onDestroy 에서 binding class 인스턴스 참조를 정리해주어야 한다.
        mBinding = null
        super.onDestroy()
    }
}