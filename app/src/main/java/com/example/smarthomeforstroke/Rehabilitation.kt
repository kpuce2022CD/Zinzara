package com.example.smarthomeforstroke

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_rehabilitation.*
import org.jetbrains.anko.startActivity

class Rehabilitation  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rehabilitation)

        btn_remedial_exercise.setOnClickListener { startActivity<ReExercise>() }
        btn_remedial_language.setOnClickListener { startActivity<ReLanguage>() }

    }
}