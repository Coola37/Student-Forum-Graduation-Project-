package com.yigitkula.studentforum.loginAndRegister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.messaging.FirebaseMessaging
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.home.HomeActivity
import com.yigitkula.studentforum.viewModel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var buttonLogin: Button
    private lateinit var registerLoginActivity: TextView
    private lateinit var emailLogin: EditText
    private lateinit var passwordLogin: EditText
    private lateinit var progressBarLogin: ProgressBar

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        buttonLogin = findViewById(R.id.buttonRegister)
        registerLoginActivity = findViewById(R.id.registerLoginActivity)
        emailLogin = findViewById(R.id.emailRegister)
        passwordLogin = findViewById(R.id.passwordLogin)
        progressBarLogin = findViewById(R.id.progressBarLogin)

        setupButtonClick()
    }

    private fun setupButtonClick() {
        buttonLogin.setOnClickListener {
            progressBarLogin.visibility = View.VISIBLE
            val email = emailLogin.text.toString()
            val password = passwordLogin.text.toString()

            viewModel.performLogin(
                email,
                password,
                { // On Login Success
                    saveFcnToken()
                    Toast.makeText(this@LoginActivity, "Welcome to Student Forum!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                },
                { // On Login Failure
                    Toast.makeText(this@LoginActivity, "Login failed!", Toast.LENGTH_SHORT).show()
                    progressBarLogin.visibility = View.GONE
                },
                { // On Email Not Verified
                    Toast.makeText(this@LoginActivity, "Please verify your Email!", Toast.LENGTH_SHORT).show()
                    progressBarLogin.visibility = View.GONE
                }
            )
        }

        registerLoginActivity.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun saveFcnToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            var token = it.result
            viewModel.saveNewTokenInDatabase(token)
        }
    }

    override fun onResume() {
        super.onResume()

    }
}
