package com.yigitkula.studentforum

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yigitkula.studentforum.home.HomeActivity
import com.yigitkula.studentforum.utils.BottomNavigationViewHelper
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

//@RunWith(RobolectricTestRunner::class)
class HomeActivityTest {

  /*  @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var homeActivity: HomeActivity
    private val context = Mockito.mock(Activity::class.java)

    @Before
    fun setup(){
        homeActivity = Robolectric.buildActivity(HomeActivity::class.java).create().get()
        bottomNavigationView = homeActivity.findViewById(R.id.bottomNavigationView)
    }

    @Test
    fun setupNavigationView(){
        BottomNavigationViewHelper.setupNavigation(context, bottomNavigationView)
        var menu = bottomNavigationView.menu
        var menuItem = menu.getItem(0)
        menuItem.isChecked = true
    }*/
}
