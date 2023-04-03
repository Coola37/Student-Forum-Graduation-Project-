package com.yigitkula.studentforum.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.utils.BottomNavigationViewHelper
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val ACTIVITY_NO=0
    private val TAG="HomeActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupNavigationView()
    }

    fun setupNavigationView(){
        BottomNavigationViewHelper.setupNavigation(this,bottomNavigationView)
        var menu=bottomNavigationView.menu
        var menuItem=menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }
}