package com.yigitkula.studentforum.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yigitkula.studentforum.R
import java.util.*

class CourseNameAdapter(private val clickListener: ((String) -> Unit)?) :
    ListAdapter<String, CourseNameAdapter.ViewHolder>(CourseNameDiffCallback()) {

    private val originalList: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.search_course_name_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val courseName = getItem(position)
        holder.bind(courseName)
    }

    fun filterList(text: String) {
        val filterPattern = text.lowercase(Locale.getDefault()).trim()
        val filteredList = if (filterPattern.isEmpty()) {
            ArrayList(originalList)
        } else {
            originalList.filter { it.lowercase(Locale.getDefault()).contains(filterPattern) }
        }
        submitList(filteredList)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val textView: TextView = itemView.findViewById(R.id.textViewCourseGroupName)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(courseName: String) {
            textView.text = courseName
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val courseName = getItem(position)
                clickListener?.invoke(courseName)
            }
        }
    }

    private class CourseNameDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}
