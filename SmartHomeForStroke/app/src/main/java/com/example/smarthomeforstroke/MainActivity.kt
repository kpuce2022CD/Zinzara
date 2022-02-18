package com.example.smarthomeforstroke

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.smarthomeforstroke.databinding.ActivityMainBinding
import com.example.smarthomeforstroke.sign.SignInActivity
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private var mBinding: ActivityMainBinding? = null
    private val binding get() = mBinding!!
    lateinit var navigationView: NavigationView
    lateinit var drawerLayout: DrawerLayout

    val PREFERENCE = "com.example.smarthomeforstroke"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        var id = pref.getString("user_id", "")
        var pw = pref.getString("pw", "")
        binding.sessionInfo.text = "환영합니다" + id + "님" + pw


        binding.btnCamera.setOnClickListener {
            val intent = Intent(this, SmartHome::class.java)
            startActivity(intent)
        }
        binding.btnRehabilitation.setOnClickListener {
            val intent = Intent(this, Rehabilitation::class.java)
            startActivity(intent)
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar) // toolBar를 통해 App Bar 생성
        setSupportActionBar(toolbar) // 툴바 적용

        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 드로어를 꺼낼 홈 버튼 활성화
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24) // 홈버튼 이미지 변경
        supportActionBar?.setDisplayShowTitleEnabled(false) // 툴바에 타이틀 안보이게

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_manage_device -> {
                    //startActivity<DeviceManage>()
                    true
                }
                R.id.logout -> {
                    val intent = Intent(this, SignInActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }R.id.menu_tutorial_home -> {

                    true
                }R.id.menu_tutorial_re -> {

                    true
                }else -> false
            }
        }

        //헤더 정보 변경
        val header = navigationView.getHeaderView(0)
        val profileId = header.findViewById<View>(R.id.profile_id) as TextView
        profileId.text = id.toString()
        val profileImage = header.findViewById<View>(R.id.profile_image) as ImageView
        profileImage.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }

    }

    // 툴바 메뉴 버튼이 클릭 됐을 때 실행하는 함수
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 클릭한 툴바 메뉴 아이템 id 마다 다르게 실행하도록 설정
        when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() { // onDestroy 에서 binding class 인스턴스 참조를 정리해주어야 한다.
        mBinding = null
        super.onDestroy()
    }


}