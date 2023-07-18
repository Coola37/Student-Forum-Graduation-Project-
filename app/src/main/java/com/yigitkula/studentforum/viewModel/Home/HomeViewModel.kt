package com.yigitkula.studentforum.viewModel.Home

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.yigitkula.studentforum.model.Post
import com.yigitkula.studentforum.viewModel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class HomeViewModel(application: Application): BaseViewModel(application)  {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val postsRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("posts")
    private val postListData: MutableLiveData<List<Post>> = MutableLiveData()
    private var authListener: FirebaseAuth.AuthStateListener? = null

    val postList: MutableLiveData<List<Post>>
        get() = postListData

    fun loadPosts() {
       launch {
            try {
                val dataSnapshot = withContext(Dispatchers.IO) {
                    postsRef.get().await()
                }

                val postList = dataSnapshot.children.mapNotNull { postSnapshot ->
                    postSnapshot.getValue(Post::class.java)?.takeIf { post ->
                        post.sender_user == auth.uid
                    }
                }

                postListData.value = postList
                Log.e("homeRcView", "Posts loaded successfully")
            } catch (e: Exception) {
                Log.e("loadPosts", "Error loading posts: ${e.message}")
            }
        }
    }


    fun setAuthListener(listener: FirebaseAuth.AuthStateListener) {
        authListener = listener
    }

    fun addAuthStateListener() {
        auth.addAuthStateListener(authListener!!)
    }

    fun removeAuthStateListener() {
        if (authListener != null) {
            auth.removeAuthStateListener(authListener!!)
        }
    }
}