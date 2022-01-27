package com.example.smarthomeforstroke

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.jetbrains.anko.toast
import java.util.*


class ReLanguage : AppCompatActivity() {

    var questionArray = arrayOf<String>("병아리","코끼리", "삐약")

    var cThis : Context? = null

    //음성 인식용
    var SttIntent: Intent? = null
    var mRecognizer: SpeechRecognizer? = null

    //음성 출력용
    var tts: TextToSpeech? = null

    // 화면 처리용
    var btnSttStart: Button? = null
    var txtInMsg: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        cThis = this
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_re_language)
        //음성인식
        SttIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        SttIntent!!.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, applicationContext.packageName)
        SttIntent!!.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR") //한국어 사용
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(cThis)
        with(mRecognizer) { this?.setRecognitionListener(listener) }

        //음성출력 생성, 리스너 초기화
        tts = TextToSpeech(cThis) { status ->
            if (status != TextToSpeech.ERROR) {
                tts!!.language = Locale.KOREAN
            }
        }
        //버튼설정
        btnSttStart = findViewById<View>(R.id.btn_stt_start) as Button
        btnSttStart!!.setOnClickListener {
            println("음성인식 시작!")
            if (ContextCompat.checkSelfPermission(
                    cThis as ReLanguage,
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@ReLanguage,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    1
                )
                //권한을 허용하지 않는 경우
            } else {
                //권한을 허용한 경우
                try {
                    with(mRecognizer) { this?.startListening(SttIntent) }
                } catch (e: SecurityException) {
                    e.printStackTrace()
                }
            }
        }
        txtInMsg = findViewById<View>(R.id.relanguage_answer) as EditText
        //어플이 실행되면 자동으로 1초뒤에 음성 인식 시작
        Handler().postDelayed({
            toast("음성인식 실행됨")
            btnSttStart!!.performClick()
        }, 1000) //바로 실행을 원하지 않으면 지워주시면 됩니다

        for (i in 1..questionArray.size){

        }

    }

    private val listener: RecognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(bundle: Bundle) {
            toast("onReadyForSpeech")

        }

        override fun onBeginningOfSpeech() {
            toast("말을 시작해 주세요")
        }

        override fun onRmsChanged(v: Float) {}
        override fun onBufferReceived(bytes: ByteArray) {
            toast("onBufferReceived")

        }

        override fun onEndOfSpeech() {
            toast("onEndOfSpeech")
        }

        override fun onError(i: Int) {
            toast("천천히 다시 말해 주세요")
        }

        override fun onResults(results: Bundle) {
            var key = ""
            key = SpeechRecognizer.RESULTS_RECOGNITION
            val mResult = results.getStringArrayList(key)
            val rs = arrayOfNulls<String>(mResult!!.size)
            mResult.toArray(rs)
            txtInMsg!!.setText(
                """
                    ${rs[0].toString()}
                    ${txtInMsg!!.text}
                    """.trimIndent()
            )
            FuncVoiceOrderCheck(rs[0])
            mRecognizer!!.startListening(SttIntent)
        }

        override fun onPartialResults(bundle: Bundle) {
            toast("onPartialResults")
        }

        override fun onEvent(i: Int, bundle: Bundle) {
            toast("onEvent")
        }
    }

    //입력된 음성 메세지 확인 후 동작 처리
    private fun FuncVoiceOrderCheck(VoiceMsg: String?) {
        var VoiceMsg = VoiceMsg
        if (VoiceMsg!!.length < 1) return
        VoiceMsg = VoiceMsg.replace(" ", "") //공백제거
        if (VoiceMsg.indexOf("카카오톡") > -1 || VoiceMsg.indexOf("카톡") > -1) {
            val launchIntent = packageManager.getLaunchIntentForPackage("com.kakao.talk")
            startActivity(launchIntent)
            onDestroy()
        } //카카오톡 어플로 이동
        if (VoiceMsg.indexOf("전동꺼") > -1 || VoiceMsg.indexOf("불꺼") > -1) {
            FuncVoiceOut("전등을 끕니다") //전등을 끕니다 라는 음성 출력
        }
    }

    //음성 메세지 출력용
    private fun FuncVoiceOut(OutMsg: String) {
        if (OutMsg.length < 1) return
        tts!!.setPitch(1.0f) //목소리 톤1.0
        tts!!.setSpeechRate(1.0f) //목소리 속도
        tts!!.speak(OutMsg, TextToSpeech.QUEUE_FLUSH, null)

        //어플이 종료할때는 완전히 제거
    }

    //카톡으로 이동을 했는데 음성인식 어플이 종료되지 않아 계속 실행되는 경우를 막기위해 어플 종료 함수
    override fun onDestroy() {
        super.onDestroy()
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
            tts = null
        }
        if (mRecognizer != null) {
            mRecognizer!!.destroy()
            mRecognizer!!.cancel()
            mRecognizer = null
        }
    }

}