package com.yigitkula.studentforum.loginAndRegister

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseAuth.getInstance
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceIdReceiver
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.home.HomeActivity
import java.util.Objects

class LoginActivity : AppCompatActivity() {

    private lateinit var buttonLogin: Button
    private lateinit var registerLoginActivity: TextView
    private lateinit var emailLogin: EditText
    private lateinit var passwordLogin: EditText
    private lateinit var progresBarLogin: ProgressBar

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
            progresBarLogin=findViewById(R.id.progressBarLogin)
            progresBarLogin.visibility= View.VISIBLE
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

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){ task->
                if (task.isSuccessful){
                    saveFcnToken()
                    val verification = auth.currentUser?.isEmailVerified
                    if(verification == true){
                        Toast.makeText(this,"Welcome to Student Forum!",Toast.LENGTH_SHORT).show()
                        progresBarLogin.visibility= View.GONE
                        val intent = Intent(this, HomeActivity::class.java)
                        this.startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this,"Please verify your Email!",Toast.LENGTH_SHORT).show()
                        progresBarLogin.visibility= View.GONE
                    }


                }else{
                    progresBarLogin.visibility= View.GONE
                    Toast.makeText(this,"Login failed!",Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener(this){
                progresBarLogin.visibility= View.GONE
                Toast.makeText(this,"Authentication failed: ${it.localizedMessage}",Toast.LENGTH_SHORT).show()
            }

    }

    private fun saveFcnToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            var token = it.result
            saveNewTokenInDatabase(token)
        }
    }
    private fun saveNewTokenInDatabase(newToken: String){
        if(FirebaseAuth.getInstance().currentUser != null){
            FirebaseDatabase.getInstance().reference
                .child("users")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("fcn_token").setValue(newToken)
        }
    }

}