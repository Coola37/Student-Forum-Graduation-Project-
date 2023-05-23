package com.yigitkula.studentforum.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.adapter.MyPostsAdapter
import com.yigitkula.studentforum.loginAndRegister.LoginActivity
import com.yigitkula.studentforum.model.Post
import com.yigitkula.studentforum.utils.EventbusDataEvents
import com.yigitkula.studentforum.view.QuestionActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class DetailSearchFragment : Fragment() {
    private lateinit var fragmentSearchView: android.widget.SearchView
    private lateinit var recyclerViewFragment: RecyclerView
    private lateinit var adapter: MyPostsAdapter
    private lateinit var searchFragmentBackImg: ImageView

    private lateinit var authListener: FirebaseAuth.AuthStateListener
    private lateinit var ref: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var coursCode: String? = null
    private var postList = mutableListOf<Post>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_detail_search, container, false)
        fragmentSearchView = view.findViewById(R.id.fragmentSearchView)
        recyclerViewFragment = view.findViewById(R.id.searchRecyclerViewFragment)
        searchFragmentBackImg = view.findViewById(R.id.searchFragmentBackImg)
        auth = Firebase.auth
        ref = FirebaseDatabase.getInstance().reference

        setupAuthListener()
        searchFragmentBackImg.setOnClickListener {
            requireActivity().onBackPressed()
        }


        fun filterList(text: String) {
            val filteredCourseNames = postList.filter { it.topic!!.contains(text, ignoreCase = true) }
            (recyclerViewFragment.adapter as MyPostsAdapter).filterList(text)
        }

        val layoutManager = LinearLayoutManager(this.activity)

        val postsRef = FirebaseDatabase.getInstance().getReference("posts")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                postList.clear() // Önceki verileri temizle

                for (postSnapshot in dataSnapshot.children) {
                    val post = postSnapshot.getValue(Post::class.java)

                    if (post!!.course_name == coursCode) {
                        postList.add(post)
                    }
                }

                for (post in postList) {
                    Log.d("Post", "Course Name: ${post!!.course_name}")
                }
                recyclerViewFragment.layoutManager=layoutManager

                adapter = MyPostsAdapter(postList){
                    EventBus.getDefault().postSticky(EventbusDataEvents.SendPostInfo(it))
                    val intent = Intent(context, QuestionActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    requireActivity().startActivity(intent)
                }

                recyclerViewFragment.adapter = adapter
                fragmentSearchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
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
            }
        }


        postsRef.addValueEventListener(postListener)


        return view
    }



    @Subscribe(sticky = true)
    internal fun onPostInfoEvent(postInfo: EventbusDataEvents.GetPostCourseName){
        coursCode = postInfo.courseName

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }
    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }

    private fun setupAuthListener() {
        authListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user = FirebaseAuth.getInstance().currentUser
                if (user == null) {

                    val intent = Intent(activity, LoginActivity::class.java)
                    activity!!.startActivity(intent)
                    activity!!.finish()

                } else {
                    return
                }
            }
        }
    }
}