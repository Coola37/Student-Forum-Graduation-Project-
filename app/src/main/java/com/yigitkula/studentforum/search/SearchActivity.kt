package com.yigitkula.studentforum.search

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.adapter.CourseNameAdapter
import com.yigitkula.studentforum.loginAndRegister.LoginActivity
import com.yigitkula.studentforum.utils.BottomNavigationViewHelper
import com.yigitkula.studentforum.utils.EventbusDataEvents
import org.greenrobot.eventbus.EventBus
import java.util.*

class SearchActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView:BottomNavigationView
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var searchRoot: ConstraintLayout
    private lateinit var searchContainer: FrameLayout
    private lateinit var searchView: SearchView

    private val ACTIVITY_NO=1
    private val TAG="SearchActivity"
    private lateinit var auth: FirebaseAuth
    private lateinit var ref: DatabaseReference
    private lateinit var authListener: FirebaseAuth.AuthStateListener
    private lateinit var adapter: CourseNameAdapter


    private lateinit var context: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        bottomNavigationView=findViewById(R.id.bottomNavigationView)
        searchRecyclerView=findViewById(R.id.searchRecyclerViewFragment)
        searchRoot=findViewById(R.id.fragmentSearchRoot)
        searchContainer=findViewById(R.id.searchContainer)

        auth=FirebaseAuth.getInstance()
        ref= FirebaseDatabase.getInstance().reference
        context=this
        searchView = findViewById(R.id.fragmentSearchView)
        searchRecyclerView.layoutManager=LinearLayoutManager(this)
        setupNavigationView()
        setupAuthListener()

        val courseRef = ref.child("posts")
        val courseNames = mutableListOf<String>()

        fun filterList(text: String) {
            val filteredCourseNames = courseNames.filter { it.contains(text, ignoreCase = true) }
            (searchRecyclerView.adapter as CourseNameAdapter).filterList(text)
        }

        val eventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val duplicateCourseNames = mutableListOf<String>()
                for (snapshot in dataSnapshot.children) {
                    val courseName = snapshot.child("course_name").getValue(String::class.java)
                    courseName?.let { courseNames.add(it) }
                }
                val groupedCourseNames = courseNames.groupBy { it }
                for ((name, group) in groupedCourseNames) {
                    if (group.size > 1) {
                        duplicateCourseNames.add(name)
                    }
                }

                courseNames.removeAll(duplicateCourseNames)

                val uniqueCourseNames = mutableListOf<String>()
                uniqueCourseNames.addAll(duplicateCourseNames.distinct())

                courseNames.addAll(uniqueCourseNames)

                val layoutManager = LinearLayoutManager(this@SearchActivity)
                val adapter = CourseNameAdapter(courseNames) { courseName ->
                    searchRoot.visibility= View.GONE
                    var transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.searchContainer, DetailSearchFragment())
                    transaction.addToBackStack("goDetailFragment")

                    transaction.commit()

                    EventBus.getDefault().postSticky(EventbusDataEvents.GetPostCourseName(courseName))
                }
                searchRecyclerView.layoutManager = layoutManager
                searchRecyclerView.adapter = adapter

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        newText?.let { filterList(it) }

                        return true
                    }
                })
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("Error: ${databaseError.message}")
            }
        }
        courseRef.addValueEventListener(eventListener)
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
//        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        if(authListener != null){
            auth.removeAuthStateListener(authListener)
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        searchRoot.visibility=View.VISIBLE
        super.onBackPressed()
    }
}