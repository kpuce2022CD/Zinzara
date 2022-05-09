package com.example.smarthomeforstroke

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.smarthomeforstroke.databinding.ActivityReLanguageBinding
import com.example.smarthomeforstroke.sign.UserAPIS
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class ReLanguage : AppCompatActivity() {

    var userAPIS = UserAPIS.create()

    val PREFERENCE = "com.example.smarthomeforstroke"

    private var mBinding: ActivityReLanguageBinding? = null
    private val binding get() = mBinding!!

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var recognitionListener: RecognitionListener

    val question_list : Array<String> = arrayOf("병아리", "쇠창살", "양면테이프", "경찰청", "간장","공장","공장장", "콩깍지", "강낭콩","껍질","된장")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityReLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListener()
        requestPermission()

        var pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        var id = pref.getString("user_id", "")
        var pw = pref.getString("pw", "")

        binding.btnSttStart.setOnClickListener {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
            speechRecognizer.setRecognitionListener(recognitionListener)
            speechRecognizer.startListening(intent)
        }

        binding.btnAgain.setOnClickListener {
            binding.relanguageAnswer.text = ""
        }

        val random = Random()
        var list = mutableSetOf<Int>()

        while(list.size<5){
            val anynumber = random.nextInt(question_list.size)
            list.add(anynumber)
        }
        val quest = list.toList()
        var cnt = 1
        var score = 0
        binding.relanguageQuestion.text = question_list[quest[0]]

        val intent = Intent(this, Rehabilitation::class.java)

        binding.btnSubmit.setOnClickListener {
            if (cnt<5) {
                if (binding.relanguageAnswer.text.contains(binding.relanguageQuestion.text)){
                    score+=1
                }
                binding.relanguageQuestion.text=question_list[quest[cnt]]
                cnt += 1
            } else {
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("언어재활")
                dialog.setMessage("끝")
                dialog.show()
                val reExerciseSend = ReExerciseSend(id.toString(), pw.toString(), cnt, "")
                userAPIS.postReExercise(reExerciseSend).enqueue(object : Callback<ReExerciseInfo> {
                    override fun onResponse(
                        call: Call<ReExerciseInfo>,
                        response: Response<ReExerciseInfo>
                    ) {
                        var builder = AlertDialog.Builder(this@ReLanguage)
                        builder.setTitle("점수 저장 되었습니다")
                        builder.setMessage(score.toString() + "점이에요!♡")
                        builder.show()
                        Thread.sleep(3000)
                        startActivity(intent)
                        finish()
                    }

                    override fun onFailure(call: Call<ReExerciseInfo>, t: Throwable) {
                        errorDialog("점수저장", t)
                    }

                })
            }
        }
    }

    private fun setListener() {
        recognitionListener = object: RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                Toast.makeText(applicationContext, "음성인식을 시작합니다.", Toast.LENGTH_SHORT).show()
            }

            override fun onBeginningOfSpeech() { }
            override fun onRmsChanged(rmsdB: Float) { }
            override fun onBufferReceived(buffer: ByteArray?) { }
            override fun onEndOfSpeech() { }
            override fun onError(error: Int) {
                var message: String
                when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> message = "오디오 에러"
                    SpeechRecognizer.ERROR_CLIENT -> message = "클라이언트 에러"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> message = "퍼미션 없음"
                    SpeechRecognizer.ERROR_NETWORK -> message = "네트워크 에러"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> message = "네트워크 타임아웃"
                    SpeechRecognizer.ERROR_NO_MATCH -> message = "찾을 수 없음"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> message = "RECOGNIZER가 바쁨"
                    SpeechRecognizer.ERROR_SERVER -> message = "서버가 이상함"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> message = "말하는 시간초과"
                    else -> message = "알 수 없는 오류"
                }
                Toast.makeText(applicationContext, "에러 발생 $message", Toast.LENGTH_SHORT).show()
            }

            override fun onResults(results: Bundle?) {
                var matches: ArrayList<String> = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) as ArrayList<String>
                for (i in 0 until matches.size) {
                    binding.relanguageAnswer.text = matches[i]
                }
            }
            override fun onPartialResults(partialResults: Bundle?) { }
            override fun onEvent(eventType: Int, params: Bundle?) { }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for(result in grantResults){
            if(result != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "권한 승인 부탁드립니다.", Toast.LENGTH_SHORT).show()
                finish()
                return
            }
        }
    }
    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.RECORD_AUDIO), 0)
        }
    }

    fun errorDialog(msg: String, t: Throwable){
        val dialog = AlertDialog.Builder(this)
        Log.e(msg, t.message.toString())
        dialog.setTitle("$msg 에러")
        dialog.setMessage("호출실패했습니다.")
        dialog.show()
    }

}