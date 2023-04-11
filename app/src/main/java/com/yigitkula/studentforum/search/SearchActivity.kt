package com.yigitkula.studentforum.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.loginAndRegister.LoginActivity
import com.yigitkula.studentforum.utils.BottomNavigationViewHelper

class SearchActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView:BottomNavigationView

    private val ACTIVITY_NO=1
    private val TAG="SearchActivity"
    private lateinit var auth: FirebaseAuth
    private lateinit var authListener: FirebaseAuth.AuthStateListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        bottomNavigationView=findViewById(R.id.bottomNavigationView)

        auth=FirebaseAuth.getInstance()

        setupNavigationView()
        setupAuthListener()
    }

    fun setupNavigationView(){
        BottomNavigationViewHelper.setupNavigation(this,bottomNavigationView)
        var menu = bottomNavigationView.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }
    private fun setupAuthListener() {
        authListener=object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user= FirebaseAuth.getInstance().currentUser
                if(user == null){

                    val intent = Intent(this@SearchActivity, LoginActivity::class.java)
                    this@SearchActivity.startActivity(intent)
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