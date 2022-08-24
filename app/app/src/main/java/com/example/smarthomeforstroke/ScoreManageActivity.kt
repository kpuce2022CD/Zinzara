package com.example.smarthomeforstroke

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.smarthomeforstroke.databinding.ActivityScoreManageBinding
import com.example.smarthomeforstroke.rehabilitation.ReLanguage
import com.example.smarthomeforstroke.sign.UserAPIS
import im.dacer.androidcharts.LineView
import retrofit2.Call
import java.util.ArrayList
import retrofit2.Callback
import retrofit2.Response

class ScoreManageActivity : AppCompatActivity() {

    var userAPIS = UserAPIS.create()

    private var mBinding: ActivityScoreManageBinding? = null
    private val binding get() = mBinding!!

    val ex_score_list = ArrayList<Int>()
    val la_score_list = ArrayList<Int>()

    val PREFERENCE = "com.example.smarthomeforstroke"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityScoreManageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        var id = pref.getString("user_id", "")
        var pw = pref.getString("pw", "")


        val lineViewEx = findViewById<View>(R.id.line_view_ex) as LineView
        val lineViewLa = findViewById<View>(R.id.line_view_la) as LineView

        val handler = Handler()
        handler.postDelayed(
            Runnable {
                initLineViewEx(lineViewEx)
                initLineViewLa(lineViewLa)
                graphSet(lineViewEx, lineViewLa)
            }, 500
        )

        val userInfo = UserInfo(id.toString(), pw.toString())
        userAPIS.getReExercise(userInfo).enqueue(object : Callback<List<ReExerciseInfo>> {
            override fun onResponse(call: Call<List<ReExerciseInfo>>, response: Response<List<ReExerciseInfo>>
            ) {
                val reExerciseInfo = response.body()
                Log.d("exercise", reExerciseInfo.toString())
                if (reExerciseInfo!!.isEmpty()){
                    binding.exNoScore.visibility = View.VISIBLE
                } else{
                    for (i in reExerciseInfo.indices){
                        ex_score_list.add(reExerciseInfo[i].physical_score)
                    }
                }
                Log.d("exercise", ex_score_list.toString())
            }
            override fun onFailure(call: Call<List<ReExerciseInfo>>, t: Throwable) {
                errorDialog("EXERCISE SCORE", t)
            }

        })

        userAPIS.getReLanguage(userInfo).enqueue(object : Callback<List<ReLanguageInfo>> {
            override fun onResponse(call: Call<List<ReLanguageInfo>>, response: Response<List<ReLanguageInfo>>
            ) {
                val reLanguageInfo = response.body()
                Log.d("language", reLanguageInfo.toString())
                if (reLanguageInfo!!.isEmpty()){
                    binding.exNoScore.visibility = View.VISIBLE
                } else{
                    for (i in reLanguageInfo.indices){
                        la_score_list.add(reLanguageInfo[i].language_score)
                    }
                }
                Log.d("language", la_score_list.toString())
            }
            override fun onFailure(call: Call<List<ReLanguageInfo>>, t: Throwable) {
                errorDialog("EXERCISE SCORE", t)
            }

        })
    }

    private fun initLineViewEx(lineView: LineView) {
        val test = ArrayList<String>()
        for (i in 0 until ex_score_list.size) {
            test.add((i + 1).toString())
        }
        lineView.setBottomTextList(test)
        lineView.setColorArray(
            intArrayOf(
                Color.parseColor("#008000")
            )
        )
        lineView.setDrawDotLine(true)
        lineView.setShowPopup(LineView.SHOW_POPUPS_NONE)
    }

    private fun initLineViewLa(lineView: LineView) {
        val test = ArrayList<String>()
        for (i in 0 until la_score_list.size) {
            test.add((i + 1).toString())
        }
        lineView.setBottomTextList(test)
        lineView.setColorArray(
            intArrayOf(
                Color.parseColor("#008080")
            )
        )
        lineView.setDrawDotLine(true)
        lineView.setShowPopup(LineView.SHOW_POPUPS_NONE)
    }

    private fun graphSet(lineViewEx: LineView, lineViewLa: LineView) {
        val dataListEx: ArrayList<ArrayList<Int>> = ArrayList()
        dataListEx.add(ex_score_list)
        lineViewEx.setDataList(dataListEx)

        val dataListLa: ArrayList<ArrayList<Int>> = ArrayList()
        dataListLa.add(la_score_list)
        lineViewLa.setDataList(dataListLa)
    }

    fun errorDialog(msg: String, t: Throwable){
        val dialog = AlertDialog.Builder(this)
        Log.e(msg, t.message.toString())
        dialog.setTitle("$msg 에러")
        dialog.setMessage("호출실패했습니다.")
        dialog.show()
    }
}