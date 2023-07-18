package com.yigitkula.studentforum.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.yigitkula.studentforum.R

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var editTextTextCurrentPassword: EditText
    private lateinit var editTextNewPassword: EditText
    private lateinit var editTextTextNewPassword2: EditText
    private lateinit var buttonChangePassword: MaterialButton

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        editTextTextCurrentPassword = findViewById(R.id.editTextTextCurrentEmail)
        editTextNewPassword = findViewById(R.id.editTextNewPassword)
        editTextTextNewPassword2 = findViewById(R.id.editTextTextNewEmail)
        buttonChangePassword = findViewById(R.id.buttonChangePassword)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        buttonChangePassword.setOnClickListener {

            val currentPassword = editTextTextCurrentPassword.text.toString()
            val newPassword = editTextNewPassword.text.toString()
            val newPassword2 = editTextTextNewPassword2.text.toString()
            if(newPassword.equals(newPassword2)){
                val credential = EmailAuthProvider.getCredential(currentUser!!.email!!,currentPassword)

                currentUser.reauthenticate(credential).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        currentUser.updatePassword(newPassword)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this,"Password change successful.",Toast.LENGTH_SHORT).show()
                                    this.onBackPressed()
                                } else {
                                    Toast.makeText(this,"Password change failed!",Toast.LENGTH_SHORT).show()
                                }
                            }
                    }else{
                        Log.e("CurrentUserVerification", task.exception.toString())
                    }
                }
            }else{
                Toast.makeText(this,"The passwords you entered do not match!.",Toast.LENGTH_SHORT).show()
            }
        }
    }
}