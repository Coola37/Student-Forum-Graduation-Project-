package com.yigitkula.studentforum.share

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.search.SearchBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.nostra13.universalimageloader.core.ImageLoader
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.home.HomeActivity
import com.yigitkula.studentforum.loginAndRegister.LoginActivity
import com.yigitkula.studentforum.model.Post
import com.yigitkula.studentforum.utils.BottomNavigationViewHelper
import com.yigitkula.studentforum.utils.UniversalImageLoader
import com.yigitkula.studentforum.utils.UniversalImageLoaderPost
import org.w3c.dom.Text
import java.lang.Exception
import java.util.UUID
import kotlin.random.Random

class ShareActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView:BottomNavigationView
    private lateinit var editTextCourseName: EditText
    private lateinit var editTextTopic: EditText
    private lateinit var editTextProblem: EditText
    private lateinit var imgUploadPhoto: ImageView
    private lateinit var buttonShare: MaterialButton
    private lateinit var textViewAddImg: TextView

    private val ACTIVITY_NO=2
    private val TAG="ShareActivity"
    private lateinit var auth: FirebaseAuth
    private lateinit var authListener: FirebaseAuth.AuthStateListener
    private lateinit var ref: DatabaseReference
    private lateinit var storageRef: StorageReference


    val CHOOSE_IMG = 100
    private var postPhotoUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        textViewAddImg=findViewById(R.id.textViewAddImg)
        buttonShare=findViewById(R.id.buttonShare)
        bottomNavigationView=findViewById(R.id.bottomNavigationView)
        editTextCourseName=findViewById(R.id.editTextCourseName)
        editTextTopic=findViewById(R.id.editTextTopic)
        editTextProblem=findViewById(R.id.editTextProblem)
        imgUploadPhoto=findViewById(R.id.imgUploadPhoto)

        auth=Firebase.auth
        ref=FirebaseDatabase.getInstance().reference
        storageRef=FirebaseStorage.getInstance().reference

        buttonShare.setOnClickListener {
            shareQuestion()
            val intent = Intent(this, HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            this.startActivity(intent)
            this.finish()
            true
        }
        textViewAddImg.setOnClickListener {
            var intent = Intent()
            intent.setType("image/")
            intent.setAction(Intent.ACTION_PICK)
            startActivityForResult(intent,CHOOSE_IMG)
        }

        setupAuthListener()
        setupBottomNavigation()
    }

    fun setupBottomNavigation(){

        BottomNavigationViewHelper.setupNavigation(this,bottomNavigationView)
        var menu = bottomNavigationView.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }

    fun shareQuestion(){

        var courseNameText = editTextCourseName.text.toString()
        var topicText = editTextTopic.text.toString()
        var problemText = editTextProblem.text.toString()

        if(courseNameText.isEmpty() || topicText.isEmpty() && problemText.isEmpty()){
            Toast.makeText(this,"Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }
        else{
            val userID = auth.currentUser!!.uid
            var postID = UUID.randomUUID().toString()
            var postQuestion = Post(postID,userID,courseNameText,topicText,problemText,"")

            ref.child("posts").child(courseNameText).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                                var uploadTask = storageRef.child("posts").child(postID.toString())
                                    .child(postPhotoUri!!.lastPathSegment!!).putFile(postPhotoUri!!)
                                    .addOnSuccessListener { itUploadTask ->
                                        itUploadTask?.storage?.downloadUrl?.addOnSuccessListener { itUri ->

                                            val downloadUrl: String = itUri.toString()
                                            ref.child("posts").child(postID.toString()).child("problem_img")
                                                .setValue(downloadUrl).addOnCompleteListener { itTask ->
                                                    if(itTask.isSuccessful){
                                                        postQuestion.problem_img=downloadUrl
                                                        Log.e("ProblemImg","Upload is succesful")
                                                    }else{
                                                        Log.e("ProblemImg",itTask.exception?.message.toString())
                                                    }
                                                }
                                        }
                                    }

                                ref.child("posts").child(postID.toString()).setValue(postQuestion)
                                    .addOnCompleteListener(object: OnCompleteListener<Void>{
                                        override fun onComplete(p0: Task<Void>) {
                                            if(p0.isSuccessful){
                                                Toast.makeText(this@ShareActivity,"Question sent",Toast.LENGTH_SHORT).show()
                                            }else{
                                                Toast.makeText(this@ShareActivity,"Question not sent",Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    })
                                    .addOnFailureListener(object: OnFailureListener{
                                        override fun onFailure(p0: Exception) {
                                            Toast.makeText(this@ShareActivity,p0.message,Toast.LENGTH_SHORT).show()
                                        }
                                    })
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ShareActivity, error.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==CHOOSE_IMG && resultCode == AppCompatActivity.RESULT_OK && data!!.data!=null){
            postPhotoUri=data!!.data

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
        if(authListener != null){
            auth.removeAuthStateListener(authListener)
        }
    }
}