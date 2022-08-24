package com.example.smarthomeforstroke.sign

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.smarthomeforstroke.R
import com.example.smarthomeforstroke.SignUpInfo
import com.example.smarthomeforstroke.UserInfo
import com.example.smarthomeforstroke.databinding.ActivityUserDeleteBinding
import com.example.smarthomeforstroke.databinding.ActivityUserProfileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserProfileActivity : AppCompatActivity() {

    var userAPIS = UserAPIS.create()
    private lateinit var binding : ActivityUserProfileBinding

    val PREFERENCE = "com.example.smarthomeforstroke"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        var id = pref.getString("user_id", "")
        var pw = pref.getString("pw", "")

        val userInfo = UserInfo(id.toString(), pw.toString())

        userAPIS.postUserInfo(userInfo).enqueue( object : Callback<SignUpInfo> {
            override fun onResponse(call: Call<SignUpInfo>, response: Response<SignUpInfo>) {
                val userInfo = response.body()
                binding.tvId.text = "아이디 : " + userInfo!!.user_id
                binding.tvPhone.text = "전화번호     " + userInfo!!.phone_number
                binding.tvSubPhone.text = "보호자 연락처     " + userInfo!!.sub_phone_number
                binding.tvCreated.text = "가입 날짜    " + userInfo!!.created!!.substring(0 until 11)
            }

            override fun onFailure(call: Call<SignUpInfo>, t: Throwable) {
                errorDialog("정보 불러오기", t)
            }

        })

        binding.deleteAccount.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            val builderItem = ActivityUserDeleteBinding.inflate(layoutInflater)
            val editText = builderItem.editText
            with(builder){
                setTitle("Delete Account")
                setMessage("비밀번호를 입력하세요 입력 하세요")
                setView(builderItem.root)
                setPositiveButton("OK"){
                        dialogInterface: DialogInterface, i: Int ->
//                    if(editText.text != null)
//                        toast("입력된 이름 : ${editText.text}")
                }
                setNegativeButton("취소"){
                        dialogInterface: DialogInterface, i: Int ->
                }
                show()
            }
        }

        binding.changePassword.setOnClickListener {
            var builder = AlertDialog.Builder(this)
            builder.setTitle("비밀번호 변경")
            builder.setIcon(R.mipmap.ic_launcher)

            var v1 = layoutInflater.inflate(R.layout.dialog, null)
            builder.setView(v1)

            // p0에 해당 AlertDialog가 들어온다. findViewById를 통해 view를 가져와서 사용
            var listener = DialogInterface.OnClickListener { p0, p1 ->
                var alert = p0 as AlertDialog
                var edit1: EditText? = alert.findViewById<EditText>(R.id.editText)
                var edit2: EditText? = alert.findViewById<EditText>(R.id.editText2)

            }

            builder.setPositiveButton("확인", listener)
            builder.setNegativeButton("취소", null)

            builder.show()
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