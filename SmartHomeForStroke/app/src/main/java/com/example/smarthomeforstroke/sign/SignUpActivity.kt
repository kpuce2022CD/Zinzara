package com.example.smarthomeforstroke.sign

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.example.smarthomeforstroke.databinding.ActivitySignUpBinding
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.smarthomeforstroke.SignUpInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignUpActivity : AppCompatActivity() {

    var userAPIS = UserAPIS.create()
    private var mBinding: ActivitySignUpBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.editPhonenum.addTextChangedListener(PhoneNumberFormattingTextWatcher())


        binding.editPwConfirm.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if (binding.editPw.getText().toString().equals(binding.editPwConfirm.getText().toString())) {
                    binding.imgPwok.visibility = View.VISIBLE
                    binding.btnSignup.isEnabled = true
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.editPw.getText().toString().equals(binding.editPwConfirm.getText().toString())) {
                    binding.imgPwok.visibility = View.VISIBLE
                    binding.btnSignup.isEnabled = true
                }
            }


        })

        binding.btnSignup.setOnClickListener {
            val id = binding.editId.text.toString()
            val pw = binding.editPw.text.toString()
            val pn = binding.editPhonenum.text.toString()

            val signUpInfo = SignUpInfo(id, pw, pn, "")
            signUpInfo.user_id = id
            signUpInfo.pw = pw
            signUpInfo.phone_number = pn

            userAPIS.requestSignUp(signUpInfo).enqueue(object : Callback<SignUpInfo>{
                override fun onResponse(call: Call<SignUpInfo>, response: Response<SignUpInfo>) {
                    val dialog = AlertDialog.Builder(this@SignUpActivity)
                    dialog.setTitle("성공")
                    dialog.setMessage("회원가입 성공")
                    dialog.show()
                    val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                override fun onFailure(call: Call<SignUpInfo>, t: Throwable) {
                    errorDialog("Server", t)
                }

            })
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