package com.yigitkula.studentforum.share

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.utils.BottomNavigationViewHelper

class ShareActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView:BottomNavigationView

    private val ACTIVITY_NO=2
    private val TAG="ShareActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        bottomNavigationView=findViewById(R.id.bottomNavigationView)

        setupBottomNavigation()
    }

    fun setupBottomNavigation(){

        BottomNavigationViewHelper.setupNavigation(this,bottomNavigationView)
        var menu = bottomNavigationView.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }
}