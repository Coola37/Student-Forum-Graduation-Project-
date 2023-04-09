package com.yigitkula.studentforum.loginAndRegister

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.home.HomeActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var buttonLogin: Button
    private lateinit var registerLoginActivity: TextView
    private lateinit var emailLogin: EditText
    private lateinit var passwordLogin: EditText

    private lateinit var auth: FirebaseAuth
    private lateinit var ref: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth
        ref= FirebaseDatabase.getInstance().reference

        buttonLogin=findViewById(R.id.buttonRegister)
        registerLoginActivity=findViewById(R.id.registerLoginActivity)
        emailLogin=findViewById(R.id.emailRegister)
        passwordLogin=findViewById(R.id.passwordLogin)
        setupButtonClick()

    }

    private fun setupButtonClick(){
        buttonLogin.setOnClickListener {
           authPerfomLogin()
        }

        registerLoginActivity.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            this.startActivity(intent)
            finish()
        }
    }


    private fun authPerfomLogin(){

        val email = emailLogin.text.toString()
        val password=passwordLogin.text.toString()

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Please fill all the fields",Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){ task->
                if (task.isSuccessful){
                    val user = auth.currentUser
                    Toast.makeText(this,"Is Successful!",Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, HomeActivity::class.java)
                    this.startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this,"Login failed!",Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener(this){
                Toast.makeText(this,"Authentication failed! ${it.localizedMessage}",Toast.LENGTH_SHORT).show()
            }

    }
}