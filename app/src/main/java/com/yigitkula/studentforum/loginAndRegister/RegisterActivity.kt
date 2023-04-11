package com.yigitkula.studentforum.loginAndRegister

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.home.HomeActivity
import com.yigitkula.studentforum.model.UserDetails
import com.yigitkula.studentforum.model.Users
import com.yigitkula.studentforum.utils.EventbusDataEvents
import org.greenrobot.eventbus.EventBus

class RegisterActivity : AppCompatActivity() {

    private lateinit var loginRegisterActivity: TextView
    private lateinit var buttonRegister: MaterialButton
    private lateinit var emailRegister: EditText
    private lateinit var usernameRegister: EditText
    private lateinit var nameRegister: EditText
    private lateinit var surnameRegister: EditText
    private lateinit var passwordRegister: EditText
    private lateinit var registerContainer: FrameLayout
    private lateinit var registerRoot: ConstraintLayout
    private lateinit var userRegisterLoading: ProgressBar


    private lateinit var emailText: String
    private lateinit var passText: String
    private lateinit var usernameText: String
    private lateinit var nameText: String
    private lateinit var surnameText: String

    private lateinit var auth: FirebaseAuth
    private lateinit var ref: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        loginRegisterActivity=findViewById(R.id.loginRegisterActivity)
        buttonRegister=findViewById(R.id.buttonRegister)
        emailRegister=findViewById(R.id.emailRegister)
        usernameRegister=findViewById(R.id.usernameRegister)
        nameRegister=findViewById(R.id.nameRegister)
        surnameRegister=findViewById(R.id.surnameRegister)
        passwordRegister=findViewById(R.id.passwordLogin)
        registerContainer=findViewById(R.id.registerContainer)
        registerRoot=findViewById(R.id.registerRoot)
        userRegisterLoading=findViewById(R.id.userRegisterLoading)

        auth = Firebase.auth
        ref=FirebaseDatabase.getInstance().reference




        setupButtonClick()
    }
    private fun performRegister() {
        emailText = emailRegister.text.toString()
        passText = passwordRegister.text.toString()
        nameText= nameRegister.text.toString()
        usernameText=usernameRegister.text.toString()
        surnameText=surnameRegister.text.toString()

        if(emailText.isEmpty() || passText.isEmpty() || usernameText.isEmpty() || nameText.isEmpty() || surnameText.isEmpty()){
            Toast.makeText(this,"Please fill all fields",Toast.LENGTH_SHORT).show()
            return
        }

        var emailInUse=false
        var usernameInUse=false

        ref.child("users").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot!!.getValue() != null){

                    for(user in snapshot!!.children){

                        var readUser=user.getValue(Users::class.java)
                        if(readUser!!.email.equals(emailText)){
                            Toast.makeText(this@RegisterActivity,"Email is in use!",Toast.LENGTH_SHORT).show()
                            emailInUse=true
                            userRegisterLoading.visibility=View.GONE
                            break
                        }
                        else if(readUser!!.user_name!!.equals(usernameText)){
                            Toast.makeText(this@RegisterActivity,"Username is in use!",Toast.LENGTH_SHORT).show()
                            usernameInUse=true
                            userRegisterLoading.visibility=View.GONE
                            break
                        }

                    }

                    if(emailInUse==false && usernameInUse==false ){

                        auth.createUserWithEmailAndPassword(emailText, passText)
                            .addOnCompleteListener(this@RegisterActivity) { task ->
                                if (task.isSuccessful) {
                                    // Sign in success, update UI with the signed-in user's information
                                    val user = auth.currentUser
                                    val userID = user!!.uid


                                    Toast.makeText(this@RegisterActivity, "Authentication ok. ", Toast.LENGTH_SHORT).show()

                                    var userDetailsRegistration =UserDetails("0", "0", "0",0)
                                    var userRegistration = Users(emailText,usernameText,nameText,surnameText,userID,userDetailsRegistration)


                                    //Saving the created user to the database
                                    ref.child("users").child(userID).setValue(userRegistration)
                                        .addOnCompleteListener(object : OnCompleteListener<Void>{
                                            override fun onComplete(p0: Task<Void>) {
                                                if(p0.isSuccessful){
                                                    Toast.makeText(this@RegisterActivity,"User Save!",Toast.LENGTH_SHORT).show()
                                                    userRegisterLoading.visibility=View.GONE
                                                }else{
                                                    //User deletion
                                                    auth.currentUser!!.delete()
                                                        .addOnCompleteListener(object: OnCompleteListener<Void>{
                                                            override fun onComplete(p0: Task<Void>) {
                                                                if(p0.isSuccessful){
                                                                    userRegisterLoading.visibility=View.GONE
                                                                    Toast.makeText(this@RegisterActivity,"User has been deleted!",Toast.LENGTH_SHORT).show()
                                                                }
                                                            }
                                                        })
                                                    userRegisterLoading.visibility=View.GONE
                                                    Toast.makeText(this@RegisterActivity,"User not save!",Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        })

                                     val intent = Intent(this@RegisterActivity,HomeActivity::class.java)
                                     startActivity(intent)
                                     finish()

                                    EventBus.getDefault().postSticky(EventbusDataEvents.getUserInfo(emailText,user!!.uid,passText))

                                } else {
                                    // If sign in fails, display a message to the user.
                                    userRegisterLoading.visibility=View.GONE
                                    Toast.makeText(this@RegisterActivity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                                }
                            }

                    }
                }
                else{

                    auth.createUserWithEmailAndPassword(emailText, passText)
                        .addOnCompleteListener(this@RegisterActivity) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                val user = auth.currentUser
                                val userID = user!!.uid

                                Toast.makeText(this@RegisterActivity, "Authentication ok. ", Toast.LENGTH_SHORT).show()

                                var userDetailsRegistration =UserDetails("0", "0", "0",0)
                                var userRegistration = Users(emailText,usernameText,nameText,surnameText,userID,userDetailsRegistration)


                                //Saving the created user to the database
                                ref.child("users").child(userID).setValue(userRegistration)
                                    .addOnCompleteListener(object : OnCompleteListener<Void>{
                                        override fun onComplete(p0: Task<Void>) {
                                            if(p0.isSuccessful){
                                                Toast.makeText(this@RegisterActivity,"User Save!",Toast.LENGTH_SHORT).show()
                                                userRegisterLoading.visibility=View.GONE
                                            }else{
                                                //User deletion
                                                auth.currentUser!!.delete()
                                                    .addOnCompleteListener(object: OnCompleteListener<Void>{
                                                        override fun onComplete(p0: Task<Void>) {
                                                            if(p0.isSuccessful){
                                                                userRegisterLoading.visibility=View.GONE
                                                                Toast.makeText(this@RegisterActivity,"User has been deleted!",Toast.LENGTH_SHORT).show()
                                                            }
                                                        }
                                                    })
                                                userRegisterLoading.visibility=View.GONE
                                                Toast.makeText(this@RegisterActivity,"User not save!",Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    })

                                val intent = Intent(this@RegisterActivity,HomeActivity::class.java)
                                startActivity(intent)
                                finish()

                                EventBus.getDefault().postSticky(EventbusDataEvents.getUserInfo(emailText,user!!.uid.toString(),passText))

                            } else {
                                // If sign in fails, display a message to the user.
                                userRegisterLoading.visibility=View.GONE
                                Toast.makeText(this@RegisterActivity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                            }
                        }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@RegisterActivity, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun setupButtonClick(){
        loginRegisterActivity.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            this.startActivity(intent)
            finish()
        }
        buttonRegister.setOnClickListener {
            userRegisterLoading.visibility=View.VISIBLE
            performRegister()
        }
    }
}