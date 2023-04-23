package com.yigitkula.studentforum.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.adapter.MyPostsAdapter
import com.yigitkula.studentforum.loginAndRegister.LoginActivity
import com.yigitkula.studentforum.model.Post
import com.yigitkula.studentforum.utils.BottomNavigationViewHelper
import com.yigitkula.studentforum.utils.EventbusDataEvents
import com.yigitkula.studentforum.view.QuestionActivity
import org.greenrobot.eventbus.EventBus

class HomeActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var rcViewYourQues: RecyclerView
    private lateinit var mainRoot: ConstraintLayout
    private val ACTIVITY_NO=0
    private val TAG="HomeActivity"
    private lateinit var auth: FirebaseAuth
    private lateinit var authListener: FirebaseAuth.AuthStateListener
    private lateinit var adapter: MyPostsAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        rcViewYourQues=findViewById(R.id.rcViewYourQues)
        mainRoot = findViewById(R.id.mainRoot)
        rcViewYourQues.layoutManager=LinearLayoutManager(this)


        val query = FirebaseDatabase.getInstance().getReference("posts").orderByChild("sender_user").equalTo(auth.uid)

        val options = FirebaseRecyclerOptions.Builder<Post>()
            .setQuery(query, Post::class.java)
            .build()

        val view = View(this)

        adapter = MyPostsAdapter(options)
        adapter.ViewHolder(view).setOnItemClickListener(object : MyPostsAdapter.OnItemClickListener{
            override fun onItemClick(post: Post) {

                val intent = Intent(this@HomeActivity, QuestionActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                this@HomeActivity.startActivity(intent)
                EventBus.getDefault().postSticky(EventbusDataEvents.SendPostInfo(post))


            }
        })
        rcViewYourQues.adapter = adapter


        setupNavigationView()
        setupAuthListener()

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
    }
    fun setupNavigationView(){
        BottomNavigationViewHelper.setupNavigation(this, bottomNavigationView)
        var menu=bottomNavigationView.menu
        var menuItem=menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }
    private fun setupAuthListener() {
        authListener=object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user= FirebaseAuth.getInstance().currentUser
                if(user == null){

                    val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                    this@HomeActivity.startActivity(intent)
                    finish()

                }else{
                    return
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authListener)
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        if(authListener != null){
            auth.removeAuthStateListener(authListener)
            adapter.startListening()
        }
    }
}