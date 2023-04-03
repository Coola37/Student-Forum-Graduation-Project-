package com.yigitkula.studentforum.loginAndRegister

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.home.HomeActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setupButtonClick()

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
    private fun setupButtonClick(){
        loginRegisterActivity.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            this.startActivity(intent)
            finish()
        }
        buttonRegister.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            this.startActivity(intent)
            finish()
        }
    }
}