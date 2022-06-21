package com.example.smarthomeforstroke

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.smarthomeforstroke.databinding.ActivityScoreManageBinding
import com.example.smarthomeforstroke.sign.UserAPIS
import im.dacer.androidcharts.LineView
import okio.Utf8.size
import retrofit2.Call
import java.util.ArrayList
import retrofit2.Callback
import retrofit2.Response
import java.nio.file.Files.size


class ScoreManageActivity : AppCompatActivity() {

    var userAPIS = UserAPIS.create()

    private var mBinding: ActivityScoreManageBinding? = null
    private val binding get() = mBinding!!

    val PREFERENCE = "com.example.smarthomeforstroke"

    var randomint = 9
    val ex_score_list = ArrayList<Int>()
    val la_score_list = ArrayList<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityScoreManageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        var id = pref.getString("user_id", "")
        var pw = pref.getString("pw", "")
        val lineView = findViewById<View>(R.id.line_view) as LineView
        val lineViewFloat = findViewById<View>(R.id.line_view_float) as LineView

        val userInfo = UserInfo(id.toString(), pw.toString())
        userAPIS.getReExercise(userInfo).enqueue( object : Callback<ReExerciseResponse>{
            override fun onResponse(
                call: Call<ReExerciseResponse>,
                response: Response<ReExerciseResponse>
            ) {
                val exercise = response.body()
                Log.d("exercise body", exercise.toString())
                val score_num = exercise?.exerciseScores?.size
                Log.d("ex body", score_num.toString())

                if (score_num == 0){
                    binding.exNoScore.visibility = View.VISIBLE
                } else{
                    for (i in 0 until score_num!!){
                        ex_score_list.add(exercise.exerciseScores[score_num].physical_score)
                    }
                }

            }

            override fun onFailure(call: Call<ReExerciseResponse>, t: Throwable) {
                errorDialog("EXERCISE SCORE", t)
            }

        })

        userAPIS.getReLanguage(userInfo).enqueue(object : Callback<ReLanguageResponse>{
            override fun onResponse(
                call: Call<ReLanguageResponse>,
                response: Response<ReLanguageResponse>
            ) {
                val language = response.body()
                Log.d("language body", language.toString())
                val score_num = language?.languageScores?.size
                Log.d("la body", score_num.toString())

                if (score_num == 0){
                    binding.laNoScore.visibility = View.VISIBLE
                } else{
                    for(i in 0 until score_num!!){
                        la_score_list.add(language.languageScores[score_num].language_score)
                    }
                }
            }

            override fun onFailure(call: Call<ReLanguageResponse>, t: Throwable) {
                errorDialog("LANGUAGE SCORE", t)
            }

        })

        val handler = Handler()
        handler.postDelayed(
            Runnable {
                initLineViewFloat(lineViewFloat)
                initLineView(lineView)
                graphSet(lineView, lineViewFloat)
            }, 500
        )
    }

    private fun initLineViewFloat(lineView: LineView) {
        val test = ArrayList<String>()
        for (i in 0 until randomint) {
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
    private fun initLineView(lineView: LineView) {
        val test = ArrayList<String>()
        for (i in 0 until randomint) {
            test.add((i + 1).toString())
        }
        lineView.setBottomTextList(test)
        lineView.setColorArray(
            intArrayOf(
                Color.parseColor("#008062")
            )
        )
        lineView.setDrawDotLine(true)
        lineView.setShowPopup(LineView.SHOW_POPUPS_NONE)
    }
    private fun graphSet(lineViewFloat: LineView, lineView: LineView) {
        for (i in 0 until randomint) {
            la_score_list.add(i)
        }
        val dataLists: ArrayList<ArrayList<Int>> = ArrayList()
        dataLists.add(la_score_list)
        lineView.setDataList(dataLists)

        for (i in 0 until randomint) {
            ex_score_list.add(i)
        }
        val dataListFs: ArrayList<ArrayList<Int>> = ArrayList()
        dataListFs.add(ex_score_list)
        lineViewFloat.setDataList(dataListFs)
    }

    fun errorDialog(msg: String, t: Throwable){
        val dialog = AlertDialog.Builder(this)
        Log.e(msg, t.message.toString())
        dialog.setTitle("$msg 에러")
        dialog.setMessage("호출실패했습니다.")
        dialog.show()
    }
}