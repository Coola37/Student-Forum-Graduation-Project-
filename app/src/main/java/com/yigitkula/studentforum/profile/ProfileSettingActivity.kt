package com.yigitkula.studentforum.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.utils.BottomNavigationViewHelper

class ProfileSettingActivity : AppCompatActivity() {

    private lateinit var profileSettingsRoot: ConstraintLayout
    private lateinit var signOut: TextView
    private lateinit var textViewSettinggsEditProfile: TextView
    private lateinit var textViewChangePassword: TextView
    private lateinit var textViewChangeEmail: TextView
    private lateinit var imageView5: ImageView
    private lateinit var bottomNavigationView: BottomNavigationView



    private val ACTIVITY_NO=4
    private val TAG="ProfileActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_setting)
        profileSettingsRoot=findViewById(R.id.profileSettingsRoot)
        signOut=findViewById(R.id.signOut)
        textViewChangeEmail=findViewById(R.id.textViewChangeEmail)
        textViewChangePassword=findViewById(R.id.textViewChangePassword)
        textViewSettinggsEditProfile=findViewById(R.id.textViewSettinggsEditProfile)
        imageView5=findViewById(R.id.imageView5)
        bottomNavigationView=findViewById(R.id.bottomNavigationView)

        activityNavigation()
        setupNavigationView()
        setupToolbar()
    }

    private fun activityNavigation(){

        signOut.setOnClickListener {
           var dialog = SignOutFragment()
               .show(supportFragmentManager,"ShowSignOutFragment")
        }

        textViewSettinggsEditProfile.setOnClickListener {
            profileSettingsRoot.visibility=View.GONE
            var transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.profileSettingsContainer,ProfileEditFragment())
            transaction.addToBackStack("addedProfileFragment")

            transaction.commit()
        }

        textViewChangePassword.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            this.startActivity(intent)
            finish()
        }

        textViewChangeEmail.setOnClickListener {
            val intent = Intent(this, ChangeEmailActivity::class.java)
            this.startActivity(intent)
            finish()
        }


    }

    fun setupToolbar(){
        imageView5.setOnClickListener {
            onBackPressed()
        }
    }

    fun setupNavigationView(){
        BottomNavigationViewHelper.setupNavigation(this, bottomNavigationView)
        var menu=bottomNavigationView.menu
        var menuItem=menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)

    }


    override fun onBackPressed() {
        profileSettingsRoot.visibility=View.VISIBLE
        super.onBackPressed()
    }
}