package com.yigitkula.studentforum.profile


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
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
    private lateinit var imgCheck: ImageView

    private lateinit var circleProfileImageFragment: CircleImageView
    private lateinit var incomingUserInfo: Users
    private lateinit var databaseRef: DatabaseReference
    val CHOOSE_IMG = 100

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
        imgCheck=view.findViewById(R.id.imgCheck)

        databaseRef=FirebaseDatabase.getInstance().reference
        initImageLoader()
        setupUserInfo()
       imgClose.setOnClickListener {
            activity?.onBackPressed()
        }

        textViewChangePhoto.setOnClickListener {
            var intent = Intent()
            intent.setType("image/")
            intent.setAction(Intent.ACTION_PICK)
            startActivityForResult(intent,CHOOSE_IMG)
        }

        imgCheck.setOnClickListener {

            if(!incomingUserInfo!!.name!!.equals(editTextCreateName.text.toString())){
                databaseRef.child("users").child(incomingUserInfo!!.user_id!!).child("name")
                    .setValue(editTextCreateName.text.toString())
            }
            if(!incomingUserInfo!!.surname!!.equals(editTextCreateSurname.text.toString())) {
                databaseRef.child("users").child(incomingUserInfo!!.user_id!!).child("surname")
                    .setValue(editTextCreateSurname.text.toString())
            }
            if(!incomingUserInfo!!.user_detail!!.departmant!!.equals(editTextDepartmant.text.toString())) {
                databaseRef.child("users").child(incomingUserInfo!!.user_id!!).child("user_detail").child("departmant")
                    .setValue(editTextDepartmant.text.toString())
            }
            if(!incomingUserInfo!!.user_detail!!.school!!.equals(editTextSchool.text.toString())) {
                databaseRef.child("users").child(incomingUserInfo!!.user_id!!).child("user_detail").child("school")
                    .setValue(editTextSchool.text.toString())
            }
            if(!incomingUserInfo!!.user_name!!.equals(editTextSchool.text.toString())) {
                databaseRef.child("users").orderByChild("user_name").addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                       var usernameInUse = false
                        for(ds in snapshot!!.children){
                            var readUserName = ds!!.getValue(Users::class.java)!!.user_name
                            if(readUserName!!.equals(editTextCreateUsername.text.toString())){
                                Toast.makeText(activity,"Username is in use!",Toast.LENGTH_SHORT).show()
                                usernameInUse=true
                                break
                            }
                        }
                        if(usernameInUse==false){
                            databaseRef.child("users").child(incomingUserInfo!!.user_id!!).child("user_name")
                                .setValue(editTextCreateUsername.text.toString())
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })

            }
            Toast.makeText(activity,"User information updated!",Toast.LENGTH_SHORT).show()

        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==CHOOSE_IMG && resultCode==AppCompatActivity.RESULT_OK && data!!.data!=null){

            var profileImgURI=data!!.data

            circleProfileImageFragment.setImageURI(profileImgURI)
        }
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