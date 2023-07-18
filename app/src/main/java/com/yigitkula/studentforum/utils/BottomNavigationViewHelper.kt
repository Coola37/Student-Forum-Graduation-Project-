package com.yigitkula.studentforum.utils



import android.content.Context
import android.content.Intent
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.home.HomeActivity
import com.yigitkula.studentforum.notifications.NotifcationsActivity
import com.yigitkula.studentforum.profile.ProfileActivity
import com.yigitkula.studentforum.search.SearchActivity
import com.yigitkula.studentforum.share.ShareActivity


class BottomNavigationViewHelper {
    companion object{
        fun setupNavigation(context: Context, bottomNavigationView: BottomNavigationView){
            bottomNavigationView.setOnItemSelectedListener {
                when (it.itemId) {

                    R.id.ic_home -> {
                        val intent = Intent(context, HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        context.startActivity(intent)
                        true
                    }
                    R.id.ic_search -> {
                        val intent = Intent(context, SearchActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        context.startActivity(intent)
                        true
                    }
                    R.id.ic_notifications -> {
                        val intent = Intent(context, NotifcationsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        context.startActivity(intent)
                        true
                    }
                    R.id.ic_profile -> {
                        val intent = Intent(context, ProfileActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        context.startActivity(intent)
                        true
                    }
                    R.id.ic_share -> {
                        val intent = Intent(context, ShareActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        context.startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
        }
    }
}