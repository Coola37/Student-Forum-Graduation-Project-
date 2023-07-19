package com.yigitkula.studentforum.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.yigitkula.studentforum.model.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(application: Application) : BaseViewModel(application) {
    private val _userData = MutableLiveData<Users>()
    val userData: LiveData<Users>
        get() = _userData

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    fun getUserData(userID: String) {
      launch {
            _loading.value = true

            try {
                val snapshot = withContext(Dispatchers.IO) {
                    FirebaseDatabase.getInstance().reference.child("users").child(userID).addListenerForSingleValueEvent(
                        object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                _userData.value = snapshot.getValue(Users::class.java)
                                _loading.value = false
                            }

                            override fun onCancelled(error: DatabaseError) {
                                _loading.value = false
                            }
                        }
                    )
                }
            } catch (e: Exception) {
                _loading.value = false
            }
        }
    }
}
