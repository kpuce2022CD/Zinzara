
package com.example.restapi

import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.restapi.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var BASE_URL:String? = null
    var lightApi: LightAPIS? = null
    var groupAPI: GroupAPIS? = null

    private var mBinding: ActivityMainBinding? = null
    private val binding get() = mBinding!!
    var light: String? = null
    var lightLight: Light? = null
    var lightState: State? = null
    var philipsHueApi = PhilipsHueAPIS.create()
    var idAndIp: List<ResponseGetIP>? = null
    var lightId: String = "1"
    var lightIds: ArrayList<String>? = null
    var lightNum = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        philipsHueApi.requestGetIPAddress().enqueue(object : Callback<List<ResponseGetIP>>{
            override fun onResponse(call: Call<List<ResponseGetIP>>, response: Response<List<ResponseGetIP>>) {
                idAndIp = response.body()
                Log.d("PHILIPS HUE", "id : " + idAndIp?.get(0)?.id)
                Log.d("PHILIPS HUE", "internalipaddress : " + idAndIp?.get(0)?.internalipaddress)
                Log.d("PHILIPS HUE", "port : " + idAndIp?.get(0)?.port)
                BASE_URL = idAndIp?.get(0)?.internalipaddress.toString()
                lightApi = LightAPIS.create(BASE_URL!!)
                groupAPI = GroupAPIS.create(BASE_URL!!)

                getLightsId()
            }

            override fun onFailure(call: Call<List<ResponseGetIP>>, t: Throwable) {
                Log.e("PHILIPS HUE", t.message.toString())
            }
        })


        //전구 선택

        binding.rgId.setOnCheckedChangeListener { radioGroup, checkedId ->
            when(checkedId){
                binding.rbId1.id -> lightId = "1"
                binding.rbId2.id -> lightId = "2"
            }
            lightButtonOnOff(lightId)
            lightBright(lightId)
        }

        // On버튼 누를 경우
        binding.btnOn.setOnClickListener {
            lightOnOff(true, lightId)
            binding.btnOn.isEnabled = false
            binding.btnOff.isEnabled = true
        }

        // Off 버튼 누를 경우
        binding.btnOff.setOnClickListener {
            lightOnOff(false, lightId)
            binding.btnOff.isEnabled = false
            binding.btnOn.isEnabled = true
        }

        binding.seekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                binding.briInfo.text = p1.toString()
                lightBrightControl(p1, lightId)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })


    }

    // 전구 상태 보고 On/Off 버튼 바꿔줌
    fun lightButtonOnOff(lightId:String){
        lightApi?.requestLightsState(lightId)?.enqueue(object : Callback<Light>{
            override fun onResponse(call: Call<Light>, response: Response<Light>) {
                lightLight = response.body()
                lightState = lightLight?.state
                var on:Boolean = lightState!!.on
                if (on){
                    binding.btnOn.isEnabled = false
                    binding.btnOff.isEnabled = true
                }
                else{binding.btnOff.isEnabled = false
                    binding.btnOn.isEnabled = true
                }
            }

            override fun onFailure(call: Call<Light>, t: Throwable) {
                errorDialog("STATE", t)
            }
        })
    }

    fun lightOnOff(setLight:Boolean, id:String){
        val data = PutLight(setLight)
        lightApi?.requestTurnLights(id, data)?.enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                light = response.body()
                Log.d("LIGHT", "state on: " + light?.toString())
                binding.tvInfo.text = light?.toString()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                errorDialog("LIGHT", t)
            }
        })
    }


    //
    fun lightBright(lightId:String){
        lightApi?.requestLightsState(lightId)?.enqueue(object : Callback<Light>{
            override fun onResponse(call: Call<Light>, response: Response<Light>) {
                lightLight = response.body()
                lightState = lightLight?.state
                var bri:Int = lightState!!.bri
                binding.seekBar.progress = bri
            }

            override fun onFailure(call: Call<Light>, t: Throwable) {
                errorDialog("BRIGHT GET", t)
            }
        })
    }

    fun lightBrightControl(setLight:Int, id:String){
        val data = PutBright(setLight)
        lightApi?.requestLightsBright(id, data)?.enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                light = response.body()

                Log.d("BRIGHT PUT", "state on: $light")
                binding.briInfo.text = light
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                errorDialog("BRIGHT PUT", t)
            }
        })

    }



    fun getLightsId() {
        groupAPI?.requestGetLightsId()?.enqueue(object : Callback<GroupInfo>{
            override fun onResponse(call: Call<GroupInfo>, response: Response<GroupInfo>) {
                val groupInfo = response.body()
                lightIds = groupInfo?.lights!!
                binding.lightId.text = lightIds!!.size.toString() + lightIds.toString()
                lightNum=lightIds!!.size
                //DisplayRadioButton()
            }

            override fun onFailure(call: Call<GroupInfo>, t: Throwable) {
                errorDialog("GROUP", t)
            }
        })
    }

    fun DisplayRadioButton() {
        for (i in 1..lightNum) {
            var radiogroup = binding.rgId
            var rdbtn = RadioButton(this)
            rdbtn.id = i;
            rdbtn.text = i.toString() + "번 조명";
            radiogroup.addView(rdbtn);
        }
    }


    fun errorDialog(msg: String, t: Throwable){
        val dialog = AlertDialog.Builder(this@MainActivity)
        Log.e(msg, t.message.toString())
        dialog.setTitle("$msg 에러")
        dialog.setMessage("호출실패했습니다.")
        dialog.show()
    }
}
