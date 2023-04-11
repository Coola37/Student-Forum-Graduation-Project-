package com.yigitkula.studentforum.profile

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.nostra13.universalimageloader.core.ImageLoader
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.model.Users
import com.yigitkula.studentforum.utils.EventbusDataEvents
import com.yigitkula.studentforum.utils.UniversalImageLoader
import de.hdodenhof.circleimageview.CircleImageView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class ProfileEditFragment : Fragment() {

    private lateinit var imgClose: ImageView
    private lateinit var editTextCreateName: EditText
    private lateinit var editTextCreateSurname: EditText
    private lateinit var editTextCreateUsername: EditText
    private lateinit var editTextDepartmant: EditText
    private lateinit var editTextSchool: EditText
    private lateinit var textViewChangePhoto: TextView

    private lateinit var circleProfileImageFragment: CircleImageView
    private lateinit var incomingUserInfo: Users

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.fragment_profile_edit, container, false)

        imgClose=view.findViewById(R.id.imgClose)

        textViewChangePhoto=view.findViewById(R.id.textViewChangePhoto)
        circleProfileImageFragment = view.findViewById(R.id.circleProfileImg)
        editTextCreateName =view.findViewById(R.id.editTextCreateName)
        editTextCreateSurname =view.findViewById(R.id.editTextCreateSurname)
        editTextCreateUsername =view.findViewById(R.id.editTextCreateUsername)
        editTextDepartmant =view.findViewById(R.id.editTextDepartmant)
        editTextSchool =view.findViewById(R.id.editTextSchool)

        initImageLoader()
        setupUserInfo()
       imgClose.setOnClickListener {
            activity?.onBackPressed()
        }



        return view
    }

    private fun initImageLoader() {

        var universalImageLoader = UniversalImageLoader(requireActivity())
        ImageLoader.getInstance().init(universalImageLoader.config)

    }
    private fun setupUserInfo(){
        editTextCreateName.setText(incomingUserInfo.name)
        editTextCreateUsername.setText(incomingUserInfo.user_name)
        editTextCreateSurname.setText(incomingUserInfo.surname)
        editTextDepartmant.setText(incomingUserInfo.user_detail!!.departmant)
        editTextSchool.setText(incomingUserInfo.user_detail!!.school)

        var imgUrl = incomingUserInfo.user_detail!!.profile_picture
        UniversalImageLoader.setImage(imgUrl!!,circleProfileImageFragment,null,"")

    }

    @Subscribe(sticky = true)
    internal fun onUserInfoEvent(usersInfo: EventbusDataEvents.SendUserInformation){
        incomingUserInfo=usersInfo.user!!
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }
}