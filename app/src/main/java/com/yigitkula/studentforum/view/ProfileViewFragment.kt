package com.yigitkula.studentforum.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.nostra13.universalimageloader.core.ImageLoader
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.model.Feedbacks
import com.yigitkula.studentforum.model.Users
import com.yigitkula.studentforum.utils.EventbusDataEvents
import com.yigitkula.studentforum.utils.UniversalImageLoader
import de.hdodenhof.circleimageview.CircleImageView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class ProfileViewFragment : Fragment() {

    private lateinit var tvUsernameView: TextView
    private lateinit var tvNameView: TextView
    private lateinit var tvSurnameView: TextView
    private lateinit var tvRankView: TextView
    private lateinit var tvDeptView: TextView
    private lateinit var tvSchoolView: TextView
    private lateinit var circleProfileImgView: CircleImageView
    private lateinit var progressBarProfilePictureView: ProgressBar
    private lateinit var imageViewBack: ImageView

    private var incFeedbackId:String? = ""
    private var incPostId:String? = ""
    private lateinit var ref: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile_view, container, false)

        tvNameView=view.findViewById(R.id.tvNameView)
        tvSurnameView=view.findViewById(R.id.tvSurnameView)
        tvUsernameView=view.findViewById(R.id.tvUsernameView)
        tvRankView=view.findViewById(R.id.tvRankView)
        tvSchoolView=view.findViewById(R.id.tvSchoolView)
        tvDeptView=view.findViewById(R.id.tvDeptView)
        circleProfileImgView=view.findViewById(R.id.circleProfileImgView)
        progressBarProfilePictureView=view.findViewById(R.id.progressBarProfilePictureView)
        imageViewBack=view.findViewById(R.id.imageViewBack)
        imageViewBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        auth=Firebase.auth
        ref=FirebaseDatabase.getInstance().reference

        getData()
        initImageLoader()
        return view
    }
    private fun getData(){
       if(incFeedbackId != ""){
           ref.child("users").child(incFeedbackId!!)
               .addValueEventListener(object : ValueEventListener{
                   override fun onDataChange(snapshot: DataSnapshot) {
                       val user = snapshot.getValue(Users::class.java)
                       tvUsernameView.setText(user!!.user_name)
                       tvRankView.setText(user!!.user_detail!!.rank.toString())
                       tvNameView.setText(user!!.name)
                       tvSurnameView.setText(user!!.surname)
                       tvDeptView.setText(user!!.user_detail!!.departmant)
                       tvSchoolView.setText(user!!.user_detail!!.school)
                       val url = user!!.user_detail!!.profile_picture
                       UniversalImageLoader.setImage(url!!,circleProfileImgView,progressBarProfilePictureView,"")
                   }

                   override fun onCancelled(error: DatabaseError) {
                       TODO("Not yet implemented")
                   }
               })
       }else{
           ref.child("users").child(incPostId!!)
               .addValueEventListener(object : ValueEventListener{
                   override fun onDataChange(snapshot: DataSnapshot) {
                       val user = snapshot.getValue(Users::class.java)
                       tvUsernameView.setText(user!!.user_name)
                       tvRankView.setText(user!!.user_detail!!.rank.toString())
                       tvNameView.setText(user!!.name)
                       tvSurnameView.setText(user!!.surname)
                       tvDeptView.setText(user!!.user_detail!!.departmant)
                       tvSchoolView.setText(user!!.user_detail!!.school)
                       val url = user!!.user_detail!!.profile_picture
                       UniversalImageLoader.setImage(url!!,circleProfileImgView,progressBarProfilePictureView,"")
                   }

                   override fun onCancelled(error: DatabaseError) {
                       TODO("Not yet implemented")
                   }
               })
       }
    }

    @Subscribe(sticky = true)
    internal fun onPostInfoEvent(feedbackInf: EventbusDataEvents.GetFeedbackSenderID){
        incFeedbackId=feedbackInf.senderID!!

    }
    @Subscribe(sticky = true)
    internal fun onPostInfoEvent2(postInf: EventbusDataEvents.GetPostSenderID){
        incPostId=postInf!!.senderID!!

    }

    private fun initImageLoader() {

        var universalImageLoader = UniversalImageLoader(requireActivity())
        ImageLoader.getInstance().init(universalImageLoader.config)

    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

}