package com.yigitkula.studentforum.notifications

import NotificationAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.home.HomeActivity
import com.yigitkula.studentforum.loginAndRegister.LoginActivity
import com.yigitkula.studentforum.model.NotificationFeedback
import com.yigitkula.studentforum.model.Post
import com.yigitkula.studentforum.utils.BottomNavigationViewHelper
import com.yigitkula.studentforum.view.QuestionActivity

class NotifcationsActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var notificationRecyler: RecyclerView

    private val ACTIVITY_NO=3
    private val TAG="NotificationsActivity"

    private lateinit var auth: FirebaseAuth
    private lateinit var ref: DatabaseReference
    private lateinit var authListener: FirebaseAuth.AuthStateListener
    private lateinit var adapter: NotificationAdapter
    private  var listAdapter = ArrayList<NotificationFeedback>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifcations)
        bottomNavigationView=findViewById(R.id.bottomNavigationView)
        notificationRecyler=findViewById(R.id.notificationRecyler)

        auth = Firebase.auth
        ref = FirebaseDatabase.getInstance().reference

        val query = ref.child("notifications").child(auth.uid!!)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (childSnapshot in snapshot.children) {
                        val value = childSnapshot.getValue(NotificationFeedback::class.java)
                        listAdapter.add(value!!)
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        adapter = NotificationAdapter(this, listAdapter) { feedback ->
            val intent = Intent(this, QuestionActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            this.startActivity(intent)
        }

        val layoutManager = LinearLayoutManager(this)
        notificationRecyler.layoutManager = layoutManager
        notificationRecyler.adapter=adapter
        setupNavigationView()
        setupAuthListener()
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

                    val intent = Intent(this@NotifcationsActivity, LoginActivity::class.java)
                    this@NotifcationsActivity.startActivity(intent)
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
    }

    override fun onStop() {
        super.onStop()
        if(authListener != null){
            auth.removeAuthStateListener(authListener)
        }
    }
}