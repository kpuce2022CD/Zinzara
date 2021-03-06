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
import com.example.smarthomeforstroke.UserId
import com.example.smarthomeforstroke.UserInfo
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


        binding.editPwConfirm.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if (binding.editPw.getText().toString().equals(binding.editPwConfirm.getText().toString())) {
                    binding.imgPwok.visibility = View.VISIBLE
                    binding.btnSignup.isEnabled = true
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.editPw.getText().toString()
                        .equals(binding.editPwConfirm.getText().toString())
                ) {
                    binding.imgPwok.visibility = View.VISIBLE
                    binding.btnSignup.isEnabled = true
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.editPw.getText().toString()
                        .equals(binding.editPwConfirm.getText().toString())
                ) {
                    binding.imgPwok.visibility = View.VISIBLE
                    binding.btnSignup.isEnabled = true
                }
            }
        })

        binding.editId.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(binding.editId.text.length in 5..9){
                    binding.imgIdok.isClickable = true
                    binding.imgIdok.visibility = View.VISIBLE
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(binding.editId.text.length in 5..9){
                    binding.imgIdok.isClickable = true
                    binding.imgIdok.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if(binding.editId.text.length in 5..9){
                    binding.imgIdok.isClickable = true
                    binding.imgIdok.visibility = View.VISIBLE
                }
            }

        })

        binding.imgIdok.setOnClickListener{
            val userId = UserId(binding.editId.text.toString())
            userAPIS.putDuplicate(userId).enqueue(object : Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.code() == 235) {
                        val dialog = AlertDialog.Builder(this@SignUpActivity)
                        dialog.setTitle("??????")
                        dialog.setMessage("???????????? ???????????????.")
                        dialog.show()

                    } else if (response.code() == 435) {
                        val dialog = AlertDialog.Builder(this@SignUpActivity)
                        dialog.setTitle("??????")
                        dialog.setMessage("?????? ????????? ??????????????????")
                        dialog.show()
                    }
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    errorDialog("????????????", t)
                }
            })
        }

        binding.btnSignup.setOnClickListener {
            val id = binding.editId.text.toString()
            val pw = binding.editPw.text.toString()
            val pn = binding.editPhonenum.text.toString()
            val pn_sub = binding.editSubPhonenum.text.toString()

            val signUpInfo = SignUpInfo(id, pw, pn ,pn_sub, "")
            signUpInfo.user_id = id
            signUpInfo.pw = pw
            signUpInfo.phone_number = pn
            signUpInfo.sub_phone_number=pn_sub

            userAPIS.requestSignUp(signUpInfo).enqueue(object : Callback<SignUpInfo>{
                override fun onResponse(call: Call<SignUpInfo>, response: Response<SignUpInfo>) {
                    if (response.code() == 210){
                        val dialog = AlertDialog.Builder(this@SignUpActivity)
                        dialog.setTitle("??????")
                        dialog.setMessage("???????????? ??????")
                        dialog.show()
                        val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else if(response.code() == 411 ){
                        val dialog = AlertDialog.Builder(this@SignUpActivity)
                        dialog.setTitle("????????? ?????? ??????")
                        dialog.setMessage("?????? : 5?????? ?????? 9?????? ??????")
                        dialog.show()
                    } else if (response.code() == 412){
                        val dialog = AlertDialog.Builder(this@SignUpActivity)
                        dialog.setTitle("???????????? ?????? ??????")
                        dialog.setMessage("?????? : 5?????? ?????? 9?????? ??????")
                        dialog.show()
                    }
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
        dialog.setTitle("$msg ??????")
        dialog.setMessage("????????????????????????.")
        dialog.show()
    }
}