package com.yigitkula.studentforum.profile


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.nostra13.universalimageloader.core.ImageLoader
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.model.Users
import com.yigitkula.studentforum.utils.EventbusDataEvents
import com.yigitkula.studentforum.utils.UniversalImageLoader
import de.hdodenhof.circleimageview.CircleImageView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.Exception

class ProfileEditFragment : Fragment() {

    private lateinit var imgClose: ImageView
    private lateinit var editTextCreateName: EditText
    private lateinit var editTextCreateSurname: EditText
    private lateinit var editTextCreateUsername: EditText
    private lateinit var editTextDepartmant: EditText
    private lateinit var editTextSchool: EditText
    private lateinit var textViewChangePhoto: TextView
    private lateinit var imgCheck: ImageView
    private lateinit var progressBarProfileImg: ProgressBar

    private lateinit var circleProfileImageFragment: CircleImageView
    private lateinit var incomingUserInfo: Users
    private lateinit var databaseRef: DatabaseReference
    private lateinit var storageRef: StorageReference

    val CHOOSE_IMG = 100
    private var profilePhotoUri: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.fragment_profile_edit, container, false)

        imgClose=view.findViewById(R.id.imgClose)

        progressBarProfileImg=view.findViewById(R.id.progressBarProfileImg)
        textViewChangePhoto=view.findViewById(R.id.textViewChangePhoto)
        circleProfileImageFragment = view.findViewById(R.id.circleProfileImgView)
        editTextCreateName =view.findViewById(R.id.editTextCourseName)
        editTextCreateSurname =view.findViewById(R.id.editTextTopic)
        editTextCreateUsername =view.findViewById(R.id.editTextProblem)
        editTextDepartmant =view.findViewById(R.id.editTextDepartmant)
        editTextSchool =view.findViewById(R.id.editTextSchool)
        imgCheck=view.findViewById(R.id.imgCheck)

        databaseRef= FirebaseDatabase.getInstance().reference
        storageRef = FirebaseStorage.getInstance().reference
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

            if(profilePhotoUri != null){

                var uploadTask = storageRef.child("users").child(incomingUserInfo!!.user_id!!).child(profilePhotoUri!!.lastPathSegment!!).putFile(profilePhotoUri!!)
                    .addOnSuccessListener { itUploadTask ->
                        itUploadTask?.storage?.downloadUrl?.addOnSuccessListener { itUri ->
                            val downloadUrl: String = itUri.toString()
                            databaseRef.child("users").child(incomingUserInfo!!.user_id!!).child("user_detail")
                                .child("profile_picture").setValue(downloadUrl).addOnCompleteListener { itTask ->
                                    if(itTask.isSuccessful){
                                        usernameUpdate(true)
                                    }else{
                                        val msg =itTask.exception?.message
                                        Log.e("ErrorUrl",msg.toString())
                                    }
                                }
                        }
                    }
                    .addOnFailureListener(object: OnFailureListener{
                        override fun onFailure(p0: Exception) {
                            Log.e("errorFailure",p0!!.message!!)
                            usernameUpdate(false)
                        }
                    })
            }else{
                usernameUpdate(null)
            }

        }
        return view
    }

    //profilImageChange
    //true -> upload Image is succesfull
    //fasle -> upload image is  failed
    //null -> user do not want change image
    private fun usernameUpdate(profileImageChange: Boolean?){

        if(!incomingUserInfo!!.user_name!!.equals(editTextCreateUsername.text.toString())) {
            databaseRef.child("users").orderByChild("user_name").addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    var usernameInUse = false

                    for(ds in snapshot!!.children){

                        var readUserName = ds!!.getValue(Users::class.java)!!.user_name

                        if(readUserName!!.equals(editTextCreateUsername.text.toString())){
                            usernameInUse=true
                            profileInformationUpdate(profileImageChange,false)
                            break
                        }
                    }
                    if(usernameInUse==false){
                        databaseRef.child("users").child(incomingUserInfo!!.user_id!!)
                            .child("user_name").setValue(editTextCreateUsername.text.toString())
                        profileInformationUpdate(profileImageChange,true)

                    }else{
                        Log.e("usernameInUse","Username is use!")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("usernameUpdate", error.message)
                }
            })

        }else{
            profileInformationUpdate(profileImageChange,null)
        }
    }
    private fun profileInformationUpdate(profileImageChange: Boolean?, usernameChange: Boolean?){
        var profileUpdate: Boolean? = null

        if(!incomingUserInfo!!.name!!.equals(editTextCreateName.text.toString())){
            databaseRef.child("users").child(incomingUserInfo!!.user_id!!).child("name")
                .setValue(editTextCreateName.text.toString())
            profileUpdate=true
        }
        if(!incomingUserInfo!!.surname!!.equals(editTextCreateSurname.text.toString())) {
            databaseRef.child("users").child(incomingUserInfo!!.user_id!!).child("surname")
                .setValue(editTextCreateSurname.text.toString())
            profileUpdate=true
        }
        if(!incomingUserInfo!!.user_detail!!.departmant!!.equals(editTextDepartmant.text.toString())) {
            databaseRef.child("users").child(incomingUserInfo!!.user_id!!).child("user_detail").child("departmant")
                .setValue(editTextDepartmant.text.toString())
            profileUpdate=true
        }
        if(!incomingUserInfo!!.user_detail!!.school!!.equals(editTextSchool.text.toString())) {
            databaseRef.child("users").child(incomingUserInfo!!.user_id!!).child("user_detail").child("school")
                .setValue(editTextSchool.text.toString())
            profileUpdate=true
        }
        if(profileImageChange == null && usernameChange == null && profileUpdate == null){
            Log.e("UserUpdateCase","no change")
        }
        else if(usernameChange == false && (profileUpdate == true || profileImageChange == true)){
            Toast.makeText(activity,"Username is in use!",Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(activity,"User information has been updated.",Toast.LENGTH_SHORT).show()
            activity?.onBackPressed()
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==CHOOSE_IMG && resultCode==AppCompatActivity.RESULT_OK && data!!.data!=null){

           profilePhotoUri=data!!.data

            circleProfileImageFragment.setImageURI(profilePhotoUri)
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
        UniversalImageLoader.setImage(imgUrl!!,circleProfileImageFragment,progressBarProfileImg,"")

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