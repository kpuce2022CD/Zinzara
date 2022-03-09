package com.example.restapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.restapi.databinding.ActivityDeviceManageBinding
import com.example.restapi.databinding.ActivityMainBinding

class DeviceManageActivity : AppCompatActivity() {

    private var mBinding: ActivityDeviceManageBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityDeviceManageBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}

