package com.yigitkula.studentforum.viewModel.Share

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.integrity.e
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.yigitkula.studentforum.model.Post
import com.yigitkula.studentforum.viewModel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class ShareViewModel(application: Application) : BaseViewModel(application) {

    private val auth: FirebaseAuth = Firebase.auth
    private val ref: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val storageRef: StorageReference = FirebaseStorage.getInstance().reference

    private val CHOOSE_IMG = 100
    private var postPhotoUri: Uri? = null

    fun shareQuestion(
        courseName: String,
        topic: String,
        problem: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        if (courseName.isEmpty() || topic.isEmpty() || problem.isEmpty()) {
            onFailure()
            return
        }

        val userID = auth.currentUser?.uid
        if (userID == null) {
            onFailure()
            return
        }

        val postID = UUID.randomUUID().toString()
        val dateFormat = SimpleDateFormat("dd/M/yyyy hh:mm")
        val date = dateFormat.format(Date())
        val postQuestion = Post(postID, userID, courseName, topic, problem, "", date)

        launch {
            try {
                val postRef = ref.child("posts").child(postID)

                if (postPhotoUri != null) {
                    val uploadTask = storageRef.child("posts").child(postID)
                        .child(postPhotoUri!!.lastPathSegment!!).putFile(postPhotoUri!!).await()
                    val downloadUrl = uploadTask.storage.downloadUrl.await().toString()
                    postQuestion.problem_img = downloadUrl
                }

                postRef.setValue(postQuestion).await()

                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onFailure()
                }
            }
        }
    }


    fun selectImage(activity: Activity) {
        val intent = Intent()
        intent.type = "image/"
        intent.action = Intent.ACTION_PICK
        activity.startActivityForResult(intent, CHOOSE_IMG)
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CHOOSE_IMG && resultCode == AppCompatActivity.RESULT_OK && data?.data != null) {
            postPhotoUri = data.data
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}
