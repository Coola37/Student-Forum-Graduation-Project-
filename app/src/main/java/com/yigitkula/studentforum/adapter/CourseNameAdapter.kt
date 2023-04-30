package com.yigitkula.studentforum.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yigitkula.studentforum.R
import java.util.*

class CourseNameAdapter(private val dataList: List<String>, private val clickListener: ((String) -> Unit)?) : RecyclerView.Adapter<CourseNameAdapter.ViewHolder>() {

    private var filteredList = mutableListOf<String>()

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
                if (courseName.lowercase(Locale.getDefault()).contains(filterPattern)) {
                    filteredList.add(courseName)
                }
            }
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val textView: TextView = itemView.findViewById(R.id.textViewCourseGroupName)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            clickListener?.invoke(filteredList[adapterPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_course_name_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = filteredList[position]
    }

    override fun getItemCount() = filteredList.size
}
