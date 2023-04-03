package com.yigitkula.studentforum.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.home.HomeActivity
import com.yigitkula.studentforum.utils.BottomNavigationViewHelper
import kotlinx.android.synthetic.main.activity_profile_setting.*
import kotlinx.android.synthetic.main.activity_profile_setting.bottomNavigationView

class ProfileSettingActivity : AppCompatActivity() {
    private val ACTIVITY_NO=4
    private val TAG="ProfileActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_setting)

        activityNavigation()
        setupNavigationView()
        setupToolbar()
    }

    private fun activityNavigation(){

        signOut.setOnClickListener {
            profileSettingsRoot.visibility= View.GONE
            var transaction=supportFragmentManager.beginTransaction()
            transaction.replace(R.id.profileSettingsContainer,SignOutFragment())
            transaction.addToBackStack("editProfileFragmentEklendi")

            transaction.commit()
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

    override fun onBackPressed() {
        profileSettingsRoot.visibility=View.VISIBLE
        super.onBackPressed()
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
}