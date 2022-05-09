package com.example.smarthomeforstroke

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smarthomeforstroke.databinding.ActivityDeviceManageBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Toast

import android.content.DialogInterface




class DeviceItem(val devNum : String)

class DeviceManageActivity : AppCompatActivity() {

    var groupAPI: GroupAPIS? = null
    private var BASE_URL:String? = null

    var philipsHueApi = PhilipsHueAPIS.create()
    var idAndIp: List<ResponseGetIP>? = null
    var lightIds: ArrayList<String>? = null
    var lightNum = 0
    var lightApi: LightAPIS? = null
    var lightLight: Light? = null
    var lightState: State? = null


    private var mBinding: ActivityDeviceManageBinding? = null
    private val binding get() = mBinding!!

    var dataList = arrayListOf<DeviceItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityDeviceManageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var recyclerView = binding.rvList
        var floatingButton = binding.floatingActionButton
        val rvAdapter = RecyclerAdapter(this, dataList)
        recyclerView.adapter = rvAdapter


//        philipsHueApi.requestGetIPAddress().enqueue(object : Callback<List<ResponseGetIP>>{
//            override fun onResponse(call: Call<List<ResponseGetIP>>, response: Response<List<ResponseGetIP>>) {
//                idAndIp = response.body()
//                Log.d("PHILIPS HUE", "id : " + idAndIp?.get(0)?.id)
//                Log.d("PHILIPS HUE", "internalipaddress : " + idAndIp?.get(0)?.internalipaddress)
//                Log.d("PHILIPS HUE", "port : " + idAndIp?.get(0)?.port)
//
//                BASE_URL = idAndIp?.get(0)?.internalipaddress.toString()
//                groupAPI = GroupAPIS.create(BASE_URL!!)
//                lightApi = LightAPIS.create(BASE_URL!!)
//                getLightsId()
//                Log.d("lightId", lightIds.toString())
//
//            }
//            override fun onFailure(call: Call<List<ResponseGetIP>>, t: Throwable) {
//                Log.e("PHILIPS HUE", t.message.toString())
//            }
//        })
//
//        rvAdapter.setOnItemClickListener(object : RecyclerAdapter.OnItemClickListener{
//            override fun onItemClick(v: View, data: DeviceItem, pos: Int) {
//                val dlg = AlertDialog.Builder(this@DeviceManageActivity)
//                dlg.setTitle("조명을 삭제하시겠습니까?") //제목
//                dlg.setMessage("확인을 누르면 조명을 사용할 수 없습니다.")
//                dlg.setPositiveButton("확인") { dialog, which ->
//                    val deleteDev = data.devNum
//                    deletelight(deleteDev)
//                }
//                dlg.setNegativeButton("취소"){dialog, which ->
//
//                }
//            }
//        })
//
//        floatingButton.setOnClickListener{
//
//        }

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
        var handler = Handler()
        handler.postDelayed(
            Runnable {
                initRecycler()
            },1000)
        // 화면 다시 불러왔을때도 일정 불러오기
        super.onResume()
    }

    private fun initRecycler(){  // 리싸이클러뷰 불러오기 함수
        var recyclerView = binding.rvList
        val lm = LinearLayoutManager(this)
        recyclerView.layoutManager = lm
        recyclerView.setHasFixedSize(true)
        Log.d("lightId", lightIds.toString())

//        for(i in lightIds?.size!!-1 downTo 0){
//            dataList.add(DeviceItem(lightIds!![i]))
//            Log.d("lightId", lightIds!![i])
//        }
        dataList.add(DeviceItem(("1")))
        dataList.add(DeviceItem(("2")))
        val rvAdapter = RecyclerAdapter(this, dataList)
        recyclerView.adapter = rvAdapter
    }

    fun deletelight(lightId:String) {
        lightApi?.deleteLight(lightId)?.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                val dialog = AlertDialog.Builder(this@DeviceManageActivity)
                dialog.setTitle("삭제 완료")
                dialog.setMessage("$lightId 번 조명이 삭제되었습니다.")
                dialog.show()
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                val dialog = AlertDialog.Builder(this@DeviceManageActivity)
                Log.e("delete", t.message.toString())
                dialog.setTitle("delete 에러")
                dialog.setMessage("호출실패했습니다.")
                dialog.show()
            }

        })
    }

}

