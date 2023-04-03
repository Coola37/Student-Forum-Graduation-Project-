package com.yigitkula.studentforum.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.utils.BottomNavigationViewHelper
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {
    private val ACTIVITY_NO=1
    private val TAG="SearchActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setupNavigationView()
    }

    fun setupNavigationView(){
        BottomNavigationViewHelper.setupNavigation(this,bottomNavigationView)
        var menu = bottomNavigationView.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }
}