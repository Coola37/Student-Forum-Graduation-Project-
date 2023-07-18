package com.yigitkula.studentforum.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.yigitkula.studentforum.R

class ChangeEmailActivity : AppCompatActivity() {

    private lateinit var editTextTextNewEmail: EditText
    private lateinit var editTextTextNewEmail2: EditText
    private lateinit var editTextTextPassword: EditText
    private lateinit var buttonChangeEmail: MaterialButton

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_email)


        editTextTextNewEmail = findViewById(R.id.editTextTextNewEmail)
        editTextTextNewEmail2 = findViewById(R.id.editTextTextNewEmail2)
        editTextTextPassword = findViewById(R.id.editTextTextPassword)
        buttonChangeEmail = findViewById(R.id.buttonChangeEmail)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser!!
        buttonChangeEmail.setOnClickListener {
            val newEmail = editTextTextNewEmail.text.toString()
            val newEmail2 = editTextTextNewEmail2.text.toString()
            val password = editTextTextPassword.text.toString()

            if(newEmail.equals(newEmail2)){
                val credential = EmailAuthProvider.getCredential(currentUser!!.email!!, password)

                currentUser.reauthenticate(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            currentUser.updateEmail(newEmail)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(this,"Email replacement was successful!",Toast.LENGTH_SHORT).show()
                                        this.onBackPressed()
                                    } else {
                                        Toast.makeText(this,"Email replacement failed!",Toast.LENGTH_SHORT).show()

                                    }
                                }
                        } else {
                            Toast.makeText(this,"Password is wrong, please check.",Toast.LENGTH_SHORT).show()
                        }
                    }

            }else{
                Toast.makeText(this,"The emails you entered do not match.",Toast.LENGTH_SHORT).show()
            }
        }
    }
}