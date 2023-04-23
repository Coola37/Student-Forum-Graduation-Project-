package com.yigitkula.studentforum.view

import FeedbackAdapter
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.nostra13.universalimageloader.core.ImageLoader
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.model.Feedbacks
import com.yigitkula.studentforum.model.Post
import com.yigitkula.studentforum.model.Users
import com.yigitkula.studentforum.profile.ProfileEditFragment
import com.yigitkula.studentforum.utils.EventbusDataEvents
import com.yigitkula.studentforum.utils.UniversalImageLoader
import com.yigitkula.studentforum.utils.UniversalImageLoaderPost
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.w3c.dom.Text
import java.util.*

class QuestionActivity : AppCompatActivity() {
    private lateinit var questionRoot: ConstraintLayout
    private var incomingPostInfo: Post? = null

    private lateinit var tvQuestionCourseName: TextView
    private lateinit var tvQuestionProblem: TextView
    private lateinit var tvQuestionTopic: TextView
    private lateinit var questionProblemImg: ImageView
    private lateinit var buttonFeedbackQuestionSend: MaterialButton
    private lateinit var editTextFeedbackQuestion: EditText
    private lateinit var listViewFeedbacksQuestion: ListView
    private lateinit var textViewSenderUsername: TextView
    private lateinit var questionContainer : FrameLayout
    private lateinit var tvSenderID: TextView

    private lateinit var ref: DatabaseReference
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        questionRoot = findViewById(R.id.questionRoot)
        tvQuestionCourseName = findViewById(R.id.tvQuestionCourseName)
        tvQuestionTopic = findViewById(R.id.tvQuestionTopic)
        tvQuestionProblem = findViewById(R.id.tvQuestionProblem)
        questionProblemImg = findViewById(R.id.questionProblemImg)
        listViewFeedbacksQuestion=findViewById(R.id.listViewFeedbackQuestion)
        buttonFeedbackQuestionSend=findViewById(R.id.buttonFeedbackQuestionSend)
        editTextFeedbackQuestion=findViewById(R.id.editTextSendFeedbackQuestion)
        textViewSenderUsername = findViewById(R.id.textViewSenderUsername)
        questionContainer=findViewById(R.id.questionContainer)

        auth=Firebase.auth
        ref = FirebaseDatabase.getInstance().reference
        EventBus.getDefault().register(this)
        initImageLoader()
        getPostData()


        val postIdAdapter = incomingPostInfo!!.post_id!!

        val view = View(this)
        val adapter = FeedbackAdapter(this, listViewFeedbacksQuestion,"feedbacks/${postIdAdapter}/")
        adapter.setOnItemClickListener(object : FeedbackAdapter.OnItemClickListener {
            override fun onItemClick(feedback: Feedbacks) {

                questionRoot.visibility=View.GONE

                var transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.questionContainer,ProfileViewFragment())
                transaction.addToBackStack(null)
                EventBus.getDefault().postSticky(EventbusDataEvents.GetFeedbackSenderID(feedback.senderID))

                transaction.commit()
            }
        })
        listViewFeedbacksQuestion.adapter=adapter
        buttonFeedbackQuestionSend.setOnClickListener {
            sendFeedback()
        }
        textViewSenderUsername.setOnClickListener {
            questionRoot.visibility=View.GONE
            var transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.questionContainer,ProfileViewFragment())
            transaction.addToBackStack(null)

            transaction.commit()

        }
    }


    fun getPostData(){
        tvQuestionProblem.setText(incomingPostInfo!!.problem)
        tvQuestionTopic.setText(incomingPostInfo!!.topic)
        tvQuestionCourseName.setText(incomingPostInfo!!.course_name)

        var imgUrl = incomingPostInfo!!.problem_img!!
        UniversalImageLoaderPost.setImage(imgUrl,questionProblemImg,null,"")

        ref.child("users").child(incomingPostInfo!!.sender_user!!).orderByChild("user_name")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val senderUser = snapshot.getValue(Users::class.java)
                    textViewSenderUsername.setText(senderUser!!.user_name!!)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }
    @Subscribe(sticky = true)
    internal fun onPostInfoEvent(postInfo: EventbusDataEvents.SendPostInfo){
        incomingPostInfo=postInfo.post!!

    }
    private fun initImageLoader() {

        var universalImageLoaderPost = UniversalImageLoader(this)
        ImageLoader.getInstance().init(universalImageLoaderPost.config)

    }

    private fun sendFeedback(){

        var feedbackText= editTextFeedbackQuestion.text.toString()
        val postID = incomingPostInfo!!.post_id.toString()
        val senderID = auth.uid!!
        val feedbackID = UUID.randomUUID().toString()
        val usernameControl = ref.child("users").child(senderID)

        usernameControl.child("user_name").get().addOnSuccessListener { dataSnapshot ->
            val userName = dataSnapshot.value as String
            var sendFeedback = Feedbacks(postID,feedbackID,senderID,userName,feedbackText)

            if (feedbackText != null){
                ref.child("feedbacks").child(postID).child(feedbackID).setValue(sendFeedback)
                    .addOnCompleteListener(object : OnCompleteListener<Void> {
                        override fun onComplete(p0: Task<Void>) {
                            if(p0.isSuccessful){
                                editTextFeedbackQuestion.text.clear()
                                incerementRank()
                                Toast.makeText(this@QuestionActivity,"Feedback sent", Toast.LENGTH_SHORT).show()
                            }else{
                                Toast.makeText(this@QuestionActivity,"Feedback not sent", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
            }else{
                Toast.makeText(this@QuestionActivity,"Feedback field is null!", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener{exception ->
            Log.e("username", exception.message.toString())
        }

    }
    private fun incerementRank(){
       ref.child("users").child(auth.uid!!).child("user_detail").child("rank")
           .addListenerForSingleValueEvent(object : ValueEventListener{
               override fun onDataChange(snapshot: DataSnapshot) {
                   var rank = snapshot.getValue(Int::class.java)
                   Log.e("rank value",rank.toString())
                   rank = rank!!.plus(1)
                   Log.e("rank value",rank.toString())
                   ref.child("users").child(auth.uid!!).child("user_detail").child("rank")
                       .setValue(rank)
                       .addOnCompleteListener {
                           Log.e("Rank","Rank Plus 1 ")
                       }
                       .addOnFailureListener{
                           Log.e("Rank","Rank didn't plus 1"+it.message.toString())
                       }
               }

               override fun onCancelled(error: DatabaseError) {
                   TODO("Not yet implemented")
               }
           })
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
       questionRoot.visibility=View.VISIBLE
        super.onBackPressed()
    }
}