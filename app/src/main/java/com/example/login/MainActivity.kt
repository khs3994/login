package com.example.login

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.login.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserInfo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth //firebase
    private lateinit var binding:ActivityMainBinding //viewbinding
    private val LOGIN_CODE = 101

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
                        setUserInfo(email)
                    }
                    else{
                        Toast.makeText(this,task.exception?.message,Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    private fun setUserInfo(email: String){
        val database = Firebase.database
        val myRef = database.getReference("message")
        myRef.setValue(email)
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.value
                val d = Log.d("pr value", "$value")
                intent.putExtra("useremail","$value")
            }
            override fun onCancelled(error: DatabaseError) {
                println("Failed to read value.")
            }
        })
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