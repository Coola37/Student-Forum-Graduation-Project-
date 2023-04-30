package com.yigitkula.studentforum.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.adapter.MyPostsAdapter
import com.yigitkula.studentforum.loginAndRegister.LoginActivity
import com.yigitkula.studentforum.model.Post
import com.yigitkula.studentforum.utils.EventbusDataEvents
import com.yigitkula.studentforum.view.QuestionActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*


class DetailSearchFragment : Fragment() {
    private lateinit var fragmentSearchView: android.widget.SearchView
    private lateinit var recyclerViewFragment: RecyclerView
    private lateinit var adapter: MyPostsAdapter

    private lateinit var authListener: FirebaseAuth.AuthStateListener
    private lateinit var ref: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var coursCode: String? = null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_detail_search, container, false)
        fragmentSearchView = view.findViewById(R.id.fragmentSearchView)
        recyclerViewFragment = view.findViewById(R.id.searchRecyclerViewFragment)
        auth = Firebase.auth
        ref = FirebaseDatabase.getInstance().reference

        setupAuthListener()

        val query = FirebaseDatabase.getInstance().getReference("posts").orderByChild("course_name").equalTo(coursCode)
        Log.e("query -> ", query.toString())
        val options = FirebaseRecyclerOptions.Builder<Post>()
            .setQuery(query, Post::class.java)
            .build()

        recyclerViewFragment.layoutManager=LinearLayoutManager(this.activity)
        adapter = MyPostsAdapter(options)

        adapter.ViewHolder(view).setOnItemClickListener(object : MyPostsAdapter.OnItemClickListener{
            override fun onItemClick(post: Post) {

                val intent = Intent(context, QuestionActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                context!!.startActivity(intent)
                EventBus.getDefault().postSticky(EventbusDataEvents.SendPostInfo(post))
            }
        })
        recyclerViewFragment.adapter = adapter

        Toast.makeText(activity,coursCode,Toast.LENGTH_SHORT).show()

        return view
    }

    @Subscribe(sticky = true)
    internal fun onPostInfoEvent(postInfo: EventbusDataEvents.GetPostCourseName){
        coursCode = postInfo.courseName

    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.startListening()
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