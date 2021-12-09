package com.example.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.login.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth //firebase
    private lateinit var binding:ActivityMainBinding //viewbinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.goSignup.setOnClickListener { //회원가입으로 이동 버튼
            val intent = Intent(this,Signup::class.java)
            startActivity(intent)
        }

        binding.login.setOnClickListener { //로그인 버튼
           Log.d("btn","success")
            signin(binding.usremail.text.toString(),binding.usrpw.text.toString())//edittext값 String 변환후 매서드에 전달
        }
    }

    private fun signin(email:String,pw:String){
        if(email.isNotEmpty()&&pw.isNotEmpty()){
            auth.signInWithEmailAndPassword(email,pw)
                .addOnCompleteListener(this){ task ->
                    if(task.isSuccessful){
                        Toast.makeText(this,"로그인 성공 $email 님",Toast.LENGTH_SHORT).show()
                        mvMainpg(auth.currentUser)
                    }
                    else{
                        Toast.makeText(this,task.exception?.message,Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    //유저 정보를 넘겨줌
    private fun mvMainpg(user: FirebaseUser?) {
        if(user != null)
        {
            startActivity(Intent(this,LogoutActivity::class.java))
            finish()
        }
    }


}