package com.yigitkula.studentforum.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
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
import com.yigitkula.studentforum.viewModel.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

class SearchActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var searchRoot: ConstraintLayout
    private lateinit var searchContainer: FrameLayout
    private lateinit var searchView: SearchView

    private val ACTIVITY_NO = 1
    private val TAG = "SearchActivity"
    private lateinit var auth: FirebaseAuth
    private lateinit var ref: DatabaseReference
    private lateinit var adapter: CourseNameAdapter

    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        searchRecyclerView = findViewById(R.id.searchRecyclerViewFragment)
        searchRoot = findViewById(R.id.fragmentSearchRoot)
        searchContainer = findViewById(R.id.searchContainer)

        auth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().reference

        viewModel = SearchViewModel()

        searchView = findViewById(R.id.fragmentSearchView)
        searchRecyclerView.layoutManager = LinearLayoutManager(this)

        setupNavigationView()
        setupAuthListener()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { viewModel.filterList(it) }
                return true
            }
        })

        viewModel.initialize()
        viewModel.fetchData()
        observeCourseNames()
    }

    private fun observeCourseNames() {
        viewModel.courseNames.observe(this) { courseNames ->
            adapter = CourseNameAdapter(clickListener = { courseName ->
                searchRoot.visibility = View.GONE
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.searchContainer, DetailSearchFragment())
                transaction.addToBackStack("goDetailFragment")
                transaction.commit()

                EventBus.getDefault().postSticky(EventbusDataEvents.GetPostCourseName(courseName))
            })
            searchRecyclerView.adapter = adapter
            adapter.submitList(courseNames)

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let { viewModel.filterList(it) }
                    return true
                }
            })
        }

        viewModel.filteredCourseNames.observe(this) { filteredCourseNames ->
            adapter.submitList(filteredCourseNames)
        }
    }





    private fun setupNavigationView() {
        BottomNavigationViewHelper.setupNavigation(this, bottomNavigationView)
        val menu = bottomNavigationView.menu
        val menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.isChecked = true
    }

    private fun setupAuthListener() {
        val authListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                val user = FirebaseAuth.getInstance().currentUser
                if (user == null) {
                    val intent = Intent(this@SearchActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    return
                }
            }
        }
        auth.addAuthStateListener(authListener)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        searchRoot.visibility = View.VISIBLE
        super.onBackPressed()
    }
}
