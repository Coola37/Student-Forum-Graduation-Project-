package com.yigitkula.studentforum.notifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.utils.BottomNavigationViewHelper
import kotlinx.android.synthetic.main.activity_notifcations.*

class NotifcationsActivity : AppCompatActivity() {
    private val ACTIVITY_NO=3
    private val TAG="NotificationsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifcations)

        setupNavigationView()
    }

    fun setupNavigationView(){

        BottomNavigationViewHelper.setupNavigation(this,bottomNavigationView)
        var menu = bottomNavigationView.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)

    }
}