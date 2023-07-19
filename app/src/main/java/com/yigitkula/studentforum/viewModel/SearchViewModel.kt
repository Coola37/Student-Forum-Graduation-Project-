package com.yigitkula.studentforum.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SearchViewModel : ViewModel() {

    private lateinit var auth: FirebaseAuth
    private lateinit var ref: DatabaseReference

    private val _courseNames = MutableLiveData<List<String>>()
    val courseNames: LiveData<List<String>>
        get() = _courseNames

    private val _filteredCourseNames = MutableLiveData<List<String>>()
    val filteredCourseNames: LiveData<List<String>>
        get() = _filteredCourseNames



    fun initialize() {
        auth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().reference
    }

    fun fetchData() {
        val courseRef = ref.child("posts")

        courseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val courseNames = mutableListOf<String>()
                val duplicateCourseNames = mutableListOf<String>()

                for (snapshot in dataSnapshot.children) {
                    val courseName = snapshot.child("course_name").getValue(String::class.java)
                    courseName?.let { courseNames.add(it) }
                }

                val groupedCourseNames = courseNames.groupBy { it }
                for ((name, group) in groupedCourseNames) {
                    if (group.size > 1) {
                        duplicateCourseNames.add(name)
                    }
                }

                val uniqueCourseNames = duplicateCourseNames.distinct()
                courseNames.removeAll(uniqueCourseNames)

                val finalCourseNames = mutableListOf<String>()
                finalCourseNames.addAll(uniqueCourseNames)
                finalCourseNames.addAll(courseNames)

                _courseNames.value = finalCourseNames
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Error: ${databaseError.message}")
            }
        })
    }

    fun filterList(text: String) {
        val originalList = courseNames.value
        val filteredList = originalList?.filter { it.contains(text, ignoreCase = true) } ?: emptyList()

        if (filteredList.isEmpty() && text.isNotEmpty()) {
            _filteredCourseNames.value = listOf("Not found")
        } else {
            _filteredCourseNames.value = filteredList
        }
    }






}





