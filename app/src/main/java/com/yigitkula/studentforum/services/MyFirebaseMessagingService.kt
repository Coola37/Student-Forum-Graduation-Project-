package com.yigitkula.studentforum.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.model.NotificationFeedback
import java.util.*

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private lateinit var ref: DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onMessageReceived(message: RemoteMessage) {

        auth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().reference


        val notificationID = UUID.randomUUID().toString()
        var user =  auth.uid
        var data = message.data
        var notiTitle:String? = message.notification!!.title
        var notiBody:String? = message.notification!!.body
        //var notification = NotificationFeedback(user, notiTitle, notiBody, data)


       /* ref.child("notification").child("feedback").child(user!!).child(notificationID).setValue(notification)
            .addOnCompleteListener(object: OnCompleteListener<Void>{
                override fun onComplete(p0: Task<Void>) {
                    if (p0.isSuccessful){
                        Log.e("Notification", "Added to database")
                    }else{
                        Log.e("Notification", "Did not add to database")
                    }
                }
            })*/
    }



    override fun onNewToken(token: String) {
        var newToken = token
        saveNewTokenInDatabase(newToken)
    }

    private fun saveNewTokenInDatabase(newToken: String){
        if(FirebaseAuth.getInstance().currentUser != null){
            FirebaseDatabase.getInstance().reference
                .child("users")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("fcn_token").setValue(newToken)
        }
    }
}