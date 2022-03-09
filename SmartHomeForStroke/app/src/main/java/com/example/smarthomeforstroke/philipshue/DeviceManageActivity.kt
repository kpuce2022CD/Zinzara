package com.example.smarthomeforstroke

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthomeforstroke.R
import com.example.smarthomeforstroke.databinding.ActivityDeviceManageBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeviceManageActivity : AppCompatActivity() {

    var groupAPI: GroupAPIS? = null
    private var BASE_URL:String? = null

    lateinit var rvAdapter : RecyclerAdapter

    var philipsHueApi = PhilipsHueAPIS.create()
    var idAndIp: List<ResponseGetIP>? = null
    var lightIds: ArrayList<String>? = null
    var lightNum = 0

    private var mBinding: ActivityDeviceManageBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityDeviceManageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var recycler1 = binding.Recycler1
        var floatingButton = binding.floatingActionButton

        philipsHueApi.requestGetIPAddress().enqueue(object : Callback<List<ResponseGetIP>>{
            override fun onResponse(call: Call<List<ResponseGetIP>>, response: Response<List<ResponseGetIP>>) {
                idAndIp = response.body()
                Log.d("PHILIPS HUE", "id : " + idAndIp?.get(0)?.id)
                Log.d("PHILIPS HUE", "internalipaddress : " + idAndIp?.get(0)?.internalipaddress)
                Log.d("PHILIPS HUE", "port : " + idAndIp?.get(0)?.port)
                BASE_URL = idAndIp?.get(0)?.internalipaddress.toString()
                groupAPI = GroupAPIS.create(BASE_URL!!)
            }
            override fun onFailure(call: Call<List<ResponseGetIP>>, t: Throwable) {
                Log.e("PHILIPS HUE", t.message.toString())
            }
        })
        initRecycler()
        floatingButton.setOnClickListener{

        }

    }

    fun getLightsId() {
        groupAPI?.requestGetLightsId()?.enqueue(object : Callback<GroupInfo> {
            override fun onResponse(call: Call<GroupInfo>, response: Response<GroupInfo>) {
                val groupInfo = response.body()
                lightIds = groupInfo?.lights!!
                lightNum=lightIds!!.size
                Log.d("data in function", lightIds.toString())
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

    override fun onResume() {
        initRecycler()  // 화면 다시 불러왔을때도 일정 불러오기
        super.onResume()
    }

    private fun initRecycler(){  // 리싸이클러뷰 불러오기 함수
        var recyTodo = findViewById<RecyclerView>(R.id.Recycler1)
        rvAdapter = RecyclerAdapter(this)
        recyTodo.adapter = rvAdapter

        getLightsId()

        var handler = Handler()
        handler.postDelayed(
                Runnable {
                    Log.d("data in handler", lightIds.toString())

                    rvAdapter.datas = lightIds!!
                    rvAdapter.notifyDataSetChanged()
            },500
        )
    }
}

