package com.example.smarthomeforstroke

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_device_manage.*
import org.jetbrains.anko.startActivity

class DeviceManage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_manage)

        btn_device_search.setOnClickListener { startActivity<DeviceSearch>() }


    }
}