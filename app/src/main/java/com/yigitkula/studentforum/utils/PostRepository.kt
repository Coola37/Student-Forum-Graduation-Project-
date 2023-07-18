package com.yigitkula.studentforum.utils

import com.google.firebase.database.*

class PostRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val courseRef: DatabaseReference = database.reference.child("posts")

    fun getCourseNames(callback: (List<String>) -> Unit) {
        val courseNames = mutableListOf<String>()

        val eventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val courseName = snapshot.child("course_name").getValue(String::class.java)
                    courseName?.let { courseNames.add(it) }
                }
                callback(courseNames)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        }

        courseRef.addListenerForSingleValueEvent(eventListener)
    }
}
