package com.yigitkula.studentforum.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.model.Post
import com.yigitkula.studentforum.utils.EventbusDataEvents
import org.greenrobot.eventbus.EventBus
import java.util.*

class MyPostsAdapter(private val dataList: List<Post>, private val clickListener: ((Post) -> Unit)?) : RecyclerView.Adapter<MyPostsAdapter.ViewHolder>() {

    private var filteredList = mutableListOf<Post>()

    init {
        filteredList.addAll(dataList)
    }

    fun filterList(text: String) {
        filteredList.clear()
        if (text.isEmpty()) {
            filteredList.addAll(dataList)
        } else {
            val filterPattern = text.lowercase(Locale.getDefault()).trim()
            for (courseName in dataList) {
                if (courseName.topic!!.lowercase(Locale.getDefault()).contains(filterPattern)) {
                    filteredList.add(courseName)
                }
            }
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val itemTvTopicSearch: TextView = itemView.findViewById(R.id.itemTvTopicSearch)
        val textViewSenderUser: TextView = itemView.findViewById(R.id.textViewSenderUser)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val clickedItem = filteredList[position]
                val eventbusDataEvents = EventbusDataEvents.SendPostInfo(clickedItem)
                EventBus.getDefault().post(eventbusDataEvents)
                clickListener?.invoke(clickedItem) // Örneğin, tıklanan öğenin konusunu gönderiyoruz
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val senderUser = filteredList[position].sender_user
        FirebaseDatabase.getInstance().reference.child("users").child(senderUser!!).child("user_name")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val username = snapshot.getValue(String::class.java)
                    holder.textViewSenderUser.text=username
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Post adapter username:", "error")
                }
            })

        holder.itemTvTopicSearch.text = filteredList[position].topic
    }

    override fun getItemCount() = filteredList.size
}
