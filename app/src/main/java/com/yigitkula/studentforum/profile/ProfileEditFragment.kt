package com.yigitkula.studentforum.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yigitkula.studentforum.R
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.fragment_profile_edit.view.*


class ProfileEditFragment : Fragment() {

    private lateinit var circleProfileImageFragment: CircleImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.fragment_profile_edit, container, false)

        circleProfileImageFragment = view.findViewById(R.id.circleProfileImg)

        view.imgClose.setOnClickListener {
            activity?.onBackPressed()
        }


        return view
    }

}