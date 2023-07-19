package com.yigitkula.studentforum.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel(application: Application) : BaseViewModel(application) {

    private lateinit var auth: FirebaseAuth
    private lateinit var ref: DatabaseReference

    init {
        auth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().reference
    }

    fun performLogin(email: String, password: String, onLoginSuccess: () -> Unit, onLoginFailure: () -> Unit, onEmailNotVerified: () -> Unit) {
        if (email.isEmpty() || password.isEmpty()) {
            onLoginFailure()
            return
        }

       launch(Dispatchers.IO) {
            try {
                val authResult = auth.signInWithEmailAndPassword(email, password).await()
                val verification = authResult?.user?.isEmailVerified

              launch(Dispatchers.Main) {
                    if (verification == true) {
                        onLoginSuccess()
                    } else {
                        onEmailNotVerified()
                    }
                }
            } catch (e: Exception) {
               launch(Dispatchers.Main) {
                    onLoginFailure()
                }
            }
        }
    }

    fun saveNewTokenInDatabase(newToken: String) {
        if (auth.currentUser != null) {
            val currentUser: FirebaseUser = auth.currentUser!!
            ref.child("users")
                .child(currentUser.uid)
                .child("fcn_token").setValue(newToken)
        }
    }
}
