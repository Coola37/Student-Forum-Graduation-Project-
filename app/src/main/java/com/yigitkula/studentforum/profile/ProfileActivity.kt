package com.yigitkula.studentforum.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.utils.BottomNavigationViewHelper

class ProfileActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var buttonProfileEdit: MaterialButton
    private lateinit var buttonSettings: MaterialButton
    private lateinit var profileRoot: ConstraintLayout

    private val ACTIVITY_NO=4
    private val TAG="ProfileActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        bottomNavigationView=findViewById(R.id.bottomNavigationView)
        buttonProfileEdit=findViewById(R.id.buttonProfileEdit)
        buttonSettings=findViewById(R.id.buttonSettings)
        profileRoot=findViewById(R.id.profileRoot)

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

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        profileRoot.visibility=View.VISIBLE
        super.onBackPressed()
    }
}