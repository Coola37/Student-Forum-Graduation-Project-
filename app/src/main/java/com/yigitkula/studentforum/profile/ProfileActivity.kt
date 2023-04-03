package com.yigitkula.studentforum.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.utils.BottomNavigationViewHelper
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile_setting.bottomNavigationView

class ProfileActivity : AppCompatActivity() {

    private val ACTIVITY_NO=4
    private val TAG="ProfileActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        setupNavigationView()
        setupButtons()
    }

    fun setupNavigationView(){
        BottomNavigationViewHelper.setupNavigation(this,bottomNavigationView)
        var menu = bottomNavigationView.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }

    fun setupButtons(){
        buttonProfileEdit.setOnClickListener {
            profileRoot.visibility=View.GONE
            var transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.profileContainer,ProfileEditFragment())
            transaction.addToBackStack("addedProfileFragment")

            transaction.commit()
        }

        buttonSettings.setOnClickListener {
            var intent = Intent(this,ProfileSettingActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        profileRoot.visibility=View.VISIBLE
        super.onBackPressed()
    }
}