package com.example.smarthomeforstroke

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.smarthomeforstroke.databinding.ActivityReExerciseBinding
import com.example.smarthomeforstroke.databinding.ActivityReLanguageBinding
import com.example.smarthomeforstroke.sign.UserAPIS
import org.w3c.dom.Text
import retrofit2.Call
import java.util.*
import retrofit2.Callback
import retrofit2.Response


class ReExercise : AppCompatActivity() {

    var userAPIS = UserAPIS.create()
    private var mBinding: ActivityReExerciseBinding? = null
    private val binding get() = mBinding!!

    val PREFERENCE = "com.example.smarthomeforstroke"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityReExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        var id = pref.getString("user_id", "")
        var pw = pref.getString("pw", "")

        Thread(Runnable {
            val img = findViewById<ImageView>(R.id.img_finger)
            val tvNum = findViewById<TextView>(R.id.tv_num)
            val tvQuest = findViewById<TextView>(R.id.tv_quest)
            val btnSend = findViewById<Button>(R.id.btn_send)
            val score = findViewById<TextView>(R.id.tv_score)
            val etCorrect = findViewById<TextView>(R.id.et_correct)
            var cnt = 0
            val intent = Intent(this, Rehabilitation::class.java)

            for( i in 1..5) {
                val random = Random()
                val num = random.nextInt(9)
                runOnUiThread {
                    btnSend.isEnabled = true
                    tvNum.text = "$i/5"
                    tvQuest.text = num.toString()
                }
                when (num) {
                    0 -> runOnUiThread {
                        img.background =
                            ContextCompat.getDrawable(this@ReExercise, R.drawable.finger0)
                    }
                    1 -> runOnUiThread {
                        img.background =
                            ContextCompat.getDrawable(this@ReExercise, R.drawable.finger1)
                    }
                    2 -> runOnUiThread {
                        img.background =
                            ContextCompat.getDrawable(this@ReExercise, R.drawable.finger2)
                    }
                    3 -> runOnUiThread {
                        img.background =
                            ContextCompat.getDrawable(this@ReExercise, R.drawable.finger3)
                    }
                    4 -> runOnUiThread {
                        img.background =
                            ContextCompat.getDrawable(this@ReExercise, R.drawable.finger4)
                    }
                    5 -> runOnUiThread {
                        img.background =
                            ContextCompat.getDrawable(this@ReExercise, R.drawable.finger5)
                    }
                    6 -> runOnUiThread {
                        img.background =
                            ContextCompat.getDrawable(this@ReExercise, R.drawable.finger6)
                    }
                    7 -> runOnUiThread {
                        img.background =
                            ContextCompat.getDrawable(this@ReExercise, R.drawable.finger7)
                    }
                    8 -> runOnUiThread {
                        img.background =
                            ContextCompat.getDrawable(this@ReExercise, R.drawable.finger8)
                    }
                }
                Thread.sleep(5000)

                if (etCorrect.text.toString().equals(num.toString())){
                    cnt += 1
                    runOnUiThread{
                        score.text = cnt.toString()
                        btnSend.isEnabled = false
                    }
                }
            }


            val reExerciseSend = ReExerciseSend(id.toString(), pw.toString(), cnt, "")
            userAPIS.postReExercise(reExerciseSend).enqueue(object : Callback<ReExerciseInfo>{
                override fun onResponse(
                    call: Call<ReExerciseInfo>,
                    response: Response<ReExerciseInfo>
                ) {
                    var builder = AlertDialog.Builder(this@ReExercise)
                    builder.setTitle("점수 저장 되었습니다")
                    builder.setMessage(cnt.toString() + "점")
                    runOnUiThread{
                        builder.show()
                    }
                    startActivity(intent)
                    finish()
                }

                override fun onFailure(call: Call<ReExerciseInfo>, t: Throwable) {
                    errorDialog("점수저장", t)
                }

            })


        }).start()




    }
    fun errorDialog(msg: String, t: Throwable){
        val dialog = AlertDialog.Builder(this)
        Log.e(msg, t.message.toString())
        dialog.setTitle("$msg 에러")
        dialog.setMessage("호출실패했습니다.")
        dialog.show()
    }
}
