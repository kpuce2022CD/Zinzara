package com.example.smarthomeforstroke

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.WorkerThread
import com.example.smarthomeforstroke.utils.AudioWriterPCM
import com.naver.speech.clientapi.SpeechConfig
import com.naver.speech.clientapi.SpeechRecognitionException
import com.naver.speech.clientapi.SpeechRecognitionListener
import com.naver.speech.clientapi.SpeechRecognitionResult
import com.naver.speech.clientapi.SpeechRecognizer
import java.lang.ref.WeakReference
import java.util.logging.Handler
import java.util.logging.LogRecord


// 1. Main Activity 클래스
class MainActivity : Activity() {

    private var handler: RecognitionHandler? = null
    private var naverRecognizer: NaverRecognizer? = null
    private var txtResult: TextView? = null
    private var btnStart: Button? = null
    private var mResult: String? = null
    private var writer: AudioWriterPCM? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        txtResult = findViewById<View>(R.id.txt_result) as TextView
        btnStart = findViewById<View>(R.id.btn_start) as Button
        handler = RecognitionHandler(this)
        naverRecognizer = NaverRecognizer(this, handler!!, CLIENT_ID)
        btnStart!!.setOnClickListener(View.OnClickListener {
            fun onClick(v: View?) {
                if (!naverRecognizer!!.speechRecognizer!!.isRunning()) {
                    mResult = ""
                    txtResult!!.text = "Connecting..."
                    btnStart?.setText("Stop")
                    naverRecognizer!!.recognize()
                } else {
                    Log.d(TAG, "stop and wait Final Result")
                    btnStart!!.setEnabled(false)
                    naverRecognizer!!.speechRecognizer?.stop()
                }
            }
        })
    }

    // Handle speech recognition Messages.
    private fun handleMessage(msg: Message) {
        when (msg.what) {
            R.id.clientReady -> {
                txtResult!!.text = "Connected"
                writer =
                    AudioWriterPCM(Environment.getExternalStorageDirectory().absolutePath + "/NaverSpeechTest")
                writer!!.open("Test")
            }
            R.id.audioRecording -> writer!!.write(msg.obj as ShortArray)
            R.id.partialResult -> {
                mResult = msg.obj.toString()
                txtResult!!.text = mResult
            }
            R.id.finalResult -> {
                val speechRecognitionResult: SpeechRecognitionResult =
                    msg.obj as SpeechRecognitionResult
                val results: List<String> = speechRecognitionResult.getResults()
                val strBuf = StringBuilder()
                for (result in results) {
                    strBuf.append(result)
                    strBuf.append("\n")
                }
                mResult = strBuf.toString()
                txtResult!!.text = mResult
            }
            R.id.recognitionError -> {
                if (writer != null) {
                    writer!!.close()
                }
                mResult = "Error code : " + msg.obj.toString()
                txtResult!!.text = mResult
                btnStart?.setText("Start")
                btnStart?.setEnabled(true)
            }
            R.id.clientInactive -> {
                if (writer != null) {
                    writer!!.close()
                }
                btnStart?.setText("START")
                btnStart?.setEnabled(true)
            }
        }
    }


    override fun onStart() {
        super.onStart() // 음성인식 서버 초기화는 여기서
        naverRecognizer!!.speechRecognizer?.initialize()
    }

    override fun onResume() {
        super.onResume()
        mResult = ""
        txtResult!!.text = ""
        btnStart?.setText(R.string.str_start)
        btnStart?.setEnabled(true)
    }

    override fun onStop() {
        super.onStop() // 음성인식 서버 종료
        naverRecognizer!!.speechRecognizer?.release()
    }

    // Declare handler for handling SpeechRecognizer thread's Messages.

    internal class RecognitionHandler(var activity: MainActivity) : Handler() {
        private val mActivity: WeakReference<MainActivity>
        fun handleMessage(msg: Message?) {
            val activity = mActivity.get()
            activity?.handleMessage(msg!!)
        }

        init {
            mActivity = WeakReference(activity)
        }

        override fun publish(record: LogRecord?) {
            TODO("Not yet implemented")
        }

        override fun flush() {
            TODO("Not yet implemented")
        }

        override fun close() {
            TODO("Not yet implemented")
        }
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val CLIENT_ID = "vc714b8wn0" // "내 애플리케이션"에서 Client ID를 확인해서 이곳에 적어주세요.
    }
}

// 2. SpeechRecognitionListener를 상속한 클래스
internal class NaverRecognizer(context: Context?, handler: Handler, clientId: String?) :
    SpeechRecognitionListener {
    public val mHandler: Handler
    var speechRecognizer: SpeechRecognizer? = null

    fun recognize() {
        try {
            speechRecognizer?.recognize(SpeechConfig(SpeechConfig.LanguageType.KOREAN, SpeechConfig.EndPointDetectType.AUTO))
        } catch (e: SpeechRecognitionException) {
            e.printStackTrace()
        }
    }

    /*
    @WorkerThread
    override fun onInactive() {
        mHandler.encoding
        val msg: Message = Message.obtain(mHandler, R.id.clientInactive)
        msg.sendToTarget()
    }*/
    @WorkerThread
    override fun onInactive() {
        val msg: Message = mHandler.obtainMessage(R.id.clientInactive)
        msg.sendToTarget()
    }


    @WorkerThread
    override fun onReady() {
        val msg: Message = Message.obtain(mHandler,R.id.clientReady)
        msg.sendToTarget()
    }

    @WorkerThread
    override fun onRecord(speech: ShortArray?) {
        val msg: Message = Message.obtain(mHandler, R.id.audioRecording, speech)
        msg.sendToTarget()
    }

    @WorkerThread
    override fun onPartialResult(result: String?) {
        val msg: Message = Message.obtain(mHandler, R.id.partialResult, result)
        msg.sendToTarget()
    }

    @WorkerThread
    override fun onEndPointDetected() {
        Log.d(TAG, "Event occurred : EndPointDetected")
    }

    @WorkerThread
    override fun onResult(result: SpeechRecognitionResult?) {
        val msg: Message = Message.obtain(mHandler, R.id.finalResult, result)
        msg.sendToTarget()
    }

    @WorkerThread
    override fun onError(errorCode: Int) {
        val msg: Message = Message.obtain(mHandler, R.id.recognitionError, errorCode)
        msg.sendToTarget()
    }

    @Override
    @WorkerThread
    override fun onEndPointDetectTypeSelected(epdType: SpeechConfig.EndPointDetectType?) {
        val msg: Message = Message.obtain(mHandler, R.id.endPointDetectTypeSelected, epdType)
        msg.sendToTarget()
    }

    companion object {
        private val TAG = NaverRecognizer::class.java.simpleName
    }

    init {
        mHandler = handler
        try {
            speechRecognizer = SpeechRecognizer(context, clientId)
        } catch (e: SpeechRecognitionException) {
            e.printStackTrace()
        }
        speechRecognizer?.setSpeechRecognitionListener(this)
    }

}
