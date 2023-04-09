package com.yigitkula.studentforum.notifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.utils.BottomNavigationViewHelper

class NotifcationsActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    private val ACTIVITY_NO=3
    private val TAG="NotificationsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifcations)
        bottomNavigationView=findViewById(R.id.bottomNavigationView)


        setupNavigationView()
    }

    fun setupNavigationView(){

        BottomNavigationViewHelper.setupNavigation(this,bottomNavigationView)
        var menu = bottomNavigationView.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)

    }
}