package com.yigitkula.studentforum.share

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.home.HomeActivity
import com.yigitkula.studentforum.loginAndRegister.LoginActivity
import com.yigitkula.studentforum.model.Post
import com.yigitkula.studentforum.utils.BottomNavigationViewHelper
import com.yigitkula.studentforum.viewModel.Share.ShareViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class ShareActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var editTextCourseName: EditText
    private lateinit var editTextTopic: EditText
    private lateinit var editTextProblem: EditText
    private lateinit var imgUploadPhoto: ImageView
    private lateinit var buttonShare: MaterialButton
    private lateinit var textViewAddImg: TextView

    private val ACTIVITY_NO = 2
    private val TAG = "ShareActivity"
    private lateinit var auth: FirebaseAuth
    private lateinit var authListener: FirebaseAuth.AuthStateListener
    private lateinit var shareViewModel: ShareViewModel


    private var postPhotoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        textViewAddImg = findViewById(R.id.textViewAddImg)
        buttonShare = findViewById(R.id.buttonShare)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        editTextCourseName = findViewById(R.id.editTextCourseName)
        editTextTopic = findViewById(R.id.editTextTopic)
        editTextProblem = findViewById(R.id.editTextProblem)
        imgUploadPhoto = findViewById(R.id.imgUploadPhoto)

        auth = Firebase.auth
        shareViewModel = ViewModelProvider(this).get(ShareViewModel::class.java)

        setupAuthListener()
        setupBottomNavigation()

        buttonShare.setOnClickListener {
            shareQuestion()
        }

        textViewAddImg.setOnClickListener {
           selectImage()
        }
    }

    private fun setupBottomNavigation() {
        BottomNavigationViewHelper.setupNavigation(this, bottomNavigationView)
        val menu = bottomNavigationView.menu
        val menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.isChecked = true
    }

    private fun shareQuestion() {
        val courseNameText = editTextCourseName.text.toString()
        val topicText = editTextTopic.text.toString()
        val problemText = editTextProblem.text.toString()

        if (courseNameText.isEmpty() || topicText.isEmpty() || problemText.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        shareViewModel.shareQuestion(courseNameText, topicText, problemText, {
            Toast.makeText(this, "Question sent", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }, {
            Toast.makeText(this, "Question not sent", Toast.LENGTH_SHORT).show()
        })
    }

    private fun selectImage() {
        shareViewModel.selectImage(this)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        shareViewModel.handleActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == AppCompatActivity.RESULT_OK && data?.data != null) {
            postPhotoUri = data.data
            imgUploadPhoto.setImageURI(postPhotoUri)
        }
    }
    private fun setupAuthListener() {
        authListener=object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user= FirebaseAuth.getInstance().currentUser
                if(user == null){

                    val intent = Intent(this@ShareActivity, LoginActivity::class.java)
                    this@ShareActivity.startActivity(intent)
                    finish()

                }else{
                    return
                }
            }
        }
    }
    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authListener)
    }
}
