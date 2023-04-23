package com.yigitkula.studentforum.search

import android.content.ClipData
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.View
import android.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.adapter.MyPostsAdapter
import com.yigitkula.studentforum.loginAndRegister.LoginActivity
import com.yigitkula.studentforum.model.Post
import com.yigitkula.studentforum.utils.BottomNavigationViewHelper
import com.yigitkula.studentforum.utils.EventbusDataEvents
import com.yigitkula.studentforum.view.QuestionActivity
import org.greenrobot.eventbus.EventBus
import java.util.*

class SearchActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView:BottomNavigationView
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var searchRoot: ConstraintLayout
    private lateinit var searchView: SearchView

    private val ACTIVITY_NO=1
    private val TAG="SearchActivity"
    private lateinit var auth: FirebaseAuth
    private lateinit var ref: DatabaseReference
    private lateinit var authListener: FirebaseAuth.AuthStateListener
    private lateinit var adapter: MyPostsAdapter


    private lateinit var context: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        bottomNavigationView=findViewById(R.id.bottomNavigationView)
        searchRecyclerView=findViewById(R.id.searchRecyclerView)
        searchRoot=findViewById(R.id.searchRoot)

        auth=FirebaseAuth.getInstance()
        ref= FirebaseDatabase.getInstance().reference
        context=this
        searchView = findViewById(R.id.searchView)
        searchRecyclerView.layoutManager=LinearLayoutManager(this)
        setupNavigationView()
        setupAuthListener()
        val query = FirebaseDatabase.getInstance().getReference("posts")
        val options = FirebaseRecyclerOptions.Builder<Post>()
            .setQuery(query, Post::class.java)
            .build()

        val view = View(this)

        adapter = MyPostsAdapter(options)

        adapter.ViewHolder(view).setOnItemClickListener(object : MyPostsAdapter.OnItemClickListener{
            override fun onItemClick(post: Post) {

                val intent = Intent(context, QuestionActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                context.startActivity(intent)
                EventBus.getDefault().postSticky(EventbusDataEvents.SendPostInfo(post))
            }
        })
        searchRecyclerView.adapter=adapter
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {

                    val query = FirebaseDatabase.getInstance().getReference("posts")
                        .orderByChild("course_name")
                        .startAt(newText.uppercase())
                        .endAt(newText.uppercase() + "\uf8ff")


                    val options = FirebaseRecyclerOptions.Builder<Post>()
                        .setQuery(query, Post::class.java)
                        .build()

                    adapter.updateOptions(options)
                }
                return true
            }

        })
    }

    fun setupNavigationView(){
        BottomNavigationViewHelper.setupNavigation(this,bottomNavigationView)
        var menu = bottomNavigationView.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }
    private fun setupAuthListener() {
        authListener=object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user= FirebaseAuth.getInstance().currentUser
                if(user == null){

                    val intent = Intent(this@SearchActivity, LoginActivity::class.java)
                    this@SearchActivity.startActivity(intent)
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
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {

        super.onBackPressed()
    }
}