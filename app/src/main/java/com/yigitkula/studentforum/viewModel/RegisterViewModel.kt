package com.yigitkula.studentforum.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.yigitkula.studentforum.model.UserDetails
import com.yigitkula.studentforum.model.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RegisterViewModel(application: Application) : BaseViewModel(application) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val ref: DatabaseReference = FirebaseDatabase.getInstance().reference

    suspend fun performRegister(
        emailText: String,
        passText: String,
        nameText: String,
        usernameText: String,
        surnameText: String
    ): Boolean {
        if (emailText.isEmpty() || passText.isEmpty() || usernameText.isEmpty() || nameText.isEmpty() || surnameText.isEmpty()) {
            return false
        }

        var emailInUse = false
        var usernameInUse = false

        // Suspending the lambda inside withContext to perform the asynchronous call
        withContext(Dispatchers.IO) {
            ref.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (user in snapshot.children) {
                        val readUser = user.getValue(Users::class.java)
                        if (readUser?.email.equals(emailText)) {
                            emailInUse = true
                            break
                        } else if (readUser?.user_name.equals(usernameText)) {
                            usernameInUse = true
                            break
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                }
            })
        }

        if (emailInUse || usernameInUse) {
            return false
        }

        return withContext(Dispatchers.IO) {
            try {
                auth.createUserWithEmailAndPassword(emailText, passText).await()
                auth.currentUser?.sendEmailVerification()?.await()
                saveData(emailText, usernameText, nameText, surnameText)
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    private suspend fun saveData(emailText: String, usernameText: String, nameText: String, surnameText: String): Boolean {
        val user = auth.currentUser
        val userID = user?.uid

        if (user == null || userID == null) {
            return false
        }

        val userDetailsRegistration = UserDetails(" ", " ", " ", 0)
        val userRegistration = Users(emailText, usernameText, nameText, surnameText, userID, userDetailsRegistration)

        return withContext(Dispatchers.IO) {
            try {
                ref.child("users").child(userID).setValue(userRegistration).await()
                true
            } catch (e: Exception) {
                auth.currentUser?.delete()?.await()
                false
            }
        }
    }
}
