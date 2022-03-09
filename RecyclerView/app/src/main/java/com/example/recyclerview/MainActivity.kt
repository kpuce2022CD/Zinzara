package com.example.recyclerview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private var mBinding: ActivityMainBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var recycler1 = binding.Recycler1
        var floatingButton= binding.floatingActionButton

        initRecycler(recycler1)

        floatingButton.setOnClickListener{
            var newIntent = Intent(applicationContext, TodoAdd::class.java)
            startActivity(newIntent)
        }
    }

    override fun onResume() {
        var recyTodo = findViewById<RecyclerView>(R.id.Recycler1)
        recyTodo = initRecycler(recyTodo)  // 화면 다시 불러왔을때도 일정 불러오기
        super.onResume()
    }

    private fun initRecycler(rv : RecyclerView) : RecyclerView {  // 리싸이클러뷰 불러오기 함수
        rvAdapter = Recycler(this)
        todoDb = TodoDB.getInstance(this)  // DB 접근

        rvAdapter.datas = todoDb?.getTodoDao()?.getTodoAll()!!

        rvAdapter.notifyDataSetChanged()

        rv.adapter = rvAdapter  // 어댑터 적용

        return rv  // 적용된 리싸이클러뷰 리턴
    }
}
