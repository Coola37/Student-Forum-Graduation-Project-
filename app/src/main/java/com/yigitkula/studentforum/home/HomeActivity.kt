package com.yigitkula.studentforum.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.adapter.MyPostsAdapter
import com.yigitkula.studentforum.loginAndRegister.LoginActivity
import com.yigitkula.studentforum.model.Post
import com.yigitkula.studentforum.utils.BottomNavigationViewHelper
import com.yigitkula.studentforum.utils.EventbusDataEvents
import com.yigitkula.studentforum.view.QuestionActivity
import com.yigitkula.studentforum.viewModel.HomeViewModel
import org.greenrobot.eventbus.EventBus

class HomeActivity : AppCompatActivity() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var rcViewYourQues: RecyclerView
    private lateinit var mainRoot: ConstraintLayout
    private val ACTIVITY_NO = 0
    private val TAG = "HomeActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        setupViews()
        setupAuthListener()
        observePostList()

        viewModel.loadPosts()

        setupNavigationView()
    }

    private fun setupViews() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        rcViewYourQues = findViewById(R.id.rcViewYourQues)
        mainRoot = findViewById(R.id.mainRoot)
    }

    private fun observePostList() {
        viewModel.postList.observe(this) { postList ->
            postList?.let {
                displayPosts(it)
            }
        }
    }


    private fun displayPosts(postList: List<Post>) {
        rcViewYourQues.layoutManager = LinearLayoutManager(this@HomeActivity)
        val adapter = MyPostsAdapter(postList) { post ->
            EventBus.getDefault().postSticky(EventbusDataEvents.SendPostInfo(post))
            val intent = Intent(this@HomeActivity, QuestionActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
        rcViewYourQues.adapter = adapter
    }

    private fun setupNavigationView() {
        BottomNavigationViewHelper.setupNavigation(this, bottomNavigationView)
        val menu = bottomNavigationView.menu
        val menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.isChecked = true
    }

    private fun setupAuthListener() {
        val authListener = FirebaseAuth.AuthStateListener { auth: FirebaseAuth ->
            val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // User is logged in, do nothing
            }
        }
        viewModel.setAuthListener(authListener)
    }

    override fun onStart() {
        super.onStart()
        viewModel.addAuthStateListener()
    }

    override fun onStop() {
        super.onStop()
        viewModel.removeAuthStateListener()
    }
}