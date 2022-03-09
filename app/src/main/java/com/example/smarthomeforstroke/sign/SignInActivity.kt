package com.example.smarthomeforstroke.sign

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.smarthomeforstroke.MainActivity
import com.example.smarthomeforstroke.SignInInfo
import com.example.smarthomeforstroke.databinding.ActivitySignInBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivity : AppCompatActivity() {

    var userAPIS = UserAPIS.create()
    private var mBinding: ActivitySignInBinding? = null
    private val binding get() = mBinding!!
    val PREFERENCE = "com.example.smarthomeforstroke"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvSignup.setOnClickListener{
            val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnLogin.setOnClickListener {
            val id = binding.editId.text.toString()
            val pw = binding.editPw.text.toString()
            if(id==""||pw==""){
                // 아이디랑 비밀번호를 입력하세요라는 안내 문구
            }
            else{
                val signInInfo =SignInInfo(id, pw)
                signInInfo.user_id = id
                signInInfo.pw = pw
                userAPIS.requestSignIn(signInInfo).enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.code() == 200){
                            // 로그인 성공
                            val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
                            val editor = pref.edit()
                            editor.putString("user_id", id.toString())
                            editor.putString("pw", pw.toString())
                            editor.commit()
                            val intent = Intent(this@SignInActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else{
                            val dialog = AlertDialog.Builder(this@SignInActivity)
                            dialog.setTitle("에러")
                            dialog.setMessage("로그인 실패했습니다.")
                            dialog.show()
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        errorDialog("Server", t)
                    }


                })
            }
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