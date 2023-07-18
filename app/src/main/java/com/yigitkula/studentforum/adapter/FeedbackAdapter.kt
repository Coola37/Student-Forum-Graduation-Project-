import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.model.Feedbacks

class FeedbackAdapter(context: Context, listView: ListView, path: String) : BaseAdapter(), AdapterView.OnItemClickListener {

    private val context: Context = context
    private val inflater = LayoutInflater.from(context)
    private val dataList = mutableListOf<Feedbacks>()
    private val myRef: DatabaseReference = FirebaseDatabase.getInstance().getReference(path)
    private val valueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            dataList.clear()
            for (snapshot in dataSnapshot.children) {
                val data = snapshot.getValue(Feedbacks::class.java)
                if (data is Feedbacks) {
                    dataList.add(data)
                } else {
                    Log.e("FeedbackAdapter", "Invalid data type: $data")
                }
            }
            notifyDataSetChanged()
            dataList.sortBy { it.date }
        }

        override fun onCancelled(databaseError: DatabaseError) {

        }
    }

    private var onItemClickListener: OnItemClickListener? = null

    init {
        myRef.addValueEventListener(valueEventListener)
        listView.adapter = this
        listView.onItemClickListener = this
    }

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getItem(position: Int): Any {
        return dataList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            view = inflater.inflate(R.layout.feedback_item, parent, false)
        }else{
            Log.e("viewError", "view = null")
        }
        val data = getItem(position) as Feedbacks
        val username =  view!!.findViewById<TextView>(R.id.feedbackUsername)
        username.text = data.userName
        view.findViewById<TextView>(R.id.textViewBody).text = data.feedback

        val likes = view.findViewById<TextView>(R.id.textViewLikes)
        likes.text = data.likeCount.toString()

        val date = view.findViewById<TextView>(R.id.textViewFBdate)
        date.text = data.date

        val imgLike = view.findViewById<ImageView>(R.id.imageViewLike)

        username.setOnClickListener {
            onItemClickListener?.onItemClick(data)
        }
        imgLike.setOnClickListener {

            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            val clickHistoryRef = FirebaseDatabase.getInstance().reference.child("likes")
                .child("clickHistory").child(uid)

            clickHistoryRef.child(data.feedbackID!!).addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        Toast.makeText(context, "You liked this feedback before.", Toast.LENGTH_SHORT).show()
                    } else {
                        FirebaseDatabase.getInstance().reference.child("likes").child("clickHistory").child(uid).child(data.feedbackID!!)
                           .setValue("click").addOnCompleteListener {
                                Log.e("clickHistoryAdded","succesfull")

                                val feedbackRef = FirebaseDatabase.getInstance().reference.child("feedbacks").child(data.postID!!).child(data.feedbackID!!)
                                feedbackRef.child("likeCount").addListenerForSingleValueEvent(object: ValueEventListener{
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                           var currentLike = snapshot.getValue(Int::class.java)
                                           if(currentLike != null){
                                               feedbackRef.child("likeCount").setValue(currentLike + 1 )
                                           }else{
                                               Log.e("increaseLikeCount", "currentLike = null")
                                           }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            Log.e("FeedbackAdapter", "Do not read likeCount value", error.toException())
                                        }
                                    })

                            }.addOnFailureListener{
                                Log.e("clickHistoryAdded", "Error" + it.message)
                            }
                        val userDetailRef = FirebaseDatabase.getInstance().reference.child("users").child(data.senderID!!).child("user_detail")
                        userDetailRef.child("rank").addListenerForSingleValueEvent(object: ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val currentRank = snapshot.getValue(Int::class.java)
                                if (currentRank != null) {
                                    userDetailRef.child("rank").setValue(currentRank + 1)
                                    clickHistoryRef.child(data.feedbackID!!).setValue(true)
                                } else{
                                    Log.e("increaseRankError", "currentRank = null")
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.e("FeedbackAdapter", "Do not read rank value", error.toException())
                            }
                        })
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("FeedbackAdapter", "Do not read clik history", error.toException())
                }
            })
        }
        return view
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }
    interface OnItemClickListener {
        fun onItemClick(feedback: Feedbacks)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }
}
