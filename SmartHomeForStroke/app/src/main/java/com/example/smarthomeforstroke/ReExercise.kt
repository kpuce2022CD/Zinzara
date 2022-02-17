package com.example.smarthomeforstroke

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
import org.w3c.dom.Text
import java.util.*
import kotlin.concurrent.thread
import kotlin.concurrent.timer

class ReExercise : AppCompatActivity() {

    private var mBinding: ActivityReExerciseBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityReExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Thread(Runnable {
            val img = findViewById<ImageView>(R.id.img_finger)
            val tvNum = findViewById<TextView>(R.id.tv_num)
            val tvQuest = findViewById<TextView>(R.id.tv_quest)
            val btnSend = findViewById<Button>(R.id.btn_send)
            val score = findViewById<TextView>(R.id.tv_score)
            val etCorrect = findViewById<TextView>(R.id.et_correct)
            var cnt = 0

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

            var builder = AlertDialog.Builder(this)
            builder.setTitle("점수")
            builder.setMessage(cnt.toString())
            runOnUiThread{
                builder.show()
            }

        }).start()




    }
}
