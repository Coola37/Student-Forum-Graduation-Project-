package com.yigitkula.studentforum.loginAndRegister

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.home.HomeActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupButtonClick()

    }

    private fun setupButtonClick(){
        buttonRegister.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            this.startActivity(intent)
            finish()
        }

        registerLoginActivity.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            this.startActivity(intent)
            finish()
        }
    }
}