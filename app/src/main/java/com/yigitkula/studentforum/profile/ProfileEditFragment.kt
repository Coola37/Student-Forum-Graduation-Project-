package com.yigitkula.studentforum.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.yigitkula.studentforum.R
import de.hdodenhof.circleimageview.CircleImageView

class ProfileEditFragment : Fragment() {

    private lateinit var imgClose: ImageView

    private lateinit var circleProfileImageFragment: CircleImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.fragment_profile_edit, container, false)

        imgClose=view.findViewById(R.id.imgClose)

        circleProfileImageFragment = view.findViewById(R.id.circleProfileImg)

       imgClose.setOnClickListener {
            activity?.onBackPressed()
        }


        return view
    }

}