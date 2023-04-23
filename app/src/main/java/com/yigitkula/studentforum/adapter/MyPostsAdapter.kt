package com.yigitkula.studentforum.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.model.Post
import com.yigitkula.studentforum.utils.EventbusDataEvents
import org.greenrobot.eventbus.EventBus

class MyPostsAdapter(options: FirebaseRecyclerOptions<Post>) :
    FirebaseRecyclerAdapter<Post, MyPostsAdapter.ViewHolder>(options) {

    private var listener: OnItemClickListener? = null
    private var searchPosts: List<Post> = listOf()



    interface OnItemClickListener {
        fun onItemClick(post: Post)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Post) {
        val post = getItem(position)
        // ViewHolder'a item'ı bağlıyoruz
        holder.bind(post)


    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                listener?.onItemClick(getItem(adapterPosition))

            }
        }

        fun bind(post: Post) {
            var courseName = itemView.findViewById(R.id.itemTvCourseCodeSearch) as TextView
            courseName.setText(post.course_name)
            var courseTopic = itemView.findViewById(R.id.itemTvTopicSearch) as TextView
            courseTopic.setText(post.topic)
            var postUuId = itemView.findViewById(R.id.rvItemUUId) as TextView
            postUuId.setText(post.post_id)
            val eventbusDataEvents = EventbusDataEvents.SendPostInfo(post)
            EventBus.getDefault().post(eventbusDataEvents)

        }

        fun setOnItemClickListener(listener: OnItemClickListener) {
            this@MyPostsAdapter.listener = listener
        }
        fun searchPosts(query: String): List<Post> {
            val filteredPosts: MutableList<Post> = mutableListOf()
            val firebasePosts = snapshots.toList() //
            for (post in firebasePosts) {
                if (post.course_name?.contains(query, true) == true || post.topic?.contains(query, true) == true) {
                    filteredPosts.add(post)
                }
            }
            return filteredPosts
        }



    }
}