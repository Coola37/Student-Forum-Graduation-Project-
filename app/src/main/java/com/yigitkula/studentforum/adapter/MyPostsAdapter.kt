package com.yigitkula.studentforum.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.databinding.RvListItemBinding
import com.yigitkula.studentforum.model.Post
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

    class ViewHolder(val binding: RvListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        // ViewHolder içindeki binding öğesine ulaşmak için bu özelliği oluşturuyoruz.
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<RvListItemBinding>(inflater, R.layout.rv_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = filteredList.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // ViewHolder'ın binding özelliğini kullanarak RvListItemBinding içinden görünümlere erişebiliriz.


        holder.binding.post = filteredList[position]
        // Diğer verileri de binding üzerinden güncelleyebilirsiniz, örneğin:
        // holder.binding.textViewSenderUser.text = "Kullanıcı Adı"

        // Tıklama işlemlerini burada gerçekleştirebilirsiniz.
    }
}
