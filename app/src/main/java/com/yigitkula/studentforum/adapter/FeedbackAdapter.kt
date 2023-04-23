import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import com.google.firebase.database.*
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.model.Feedbacks
import com.yigitkula.studentforum.profile.ProfileEditFragment
import com.yigitkula.studentforum.utils.EventbusDataEvents
import org.greenrobot.eventbus.EventBus

class FeedbackAdapter(context: Context, listView: ListView, path: String) : BaseAdapter(), AdapterView.OnItemClickListener {

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
        }
        val data = getItem(position) as Feedbacks
        view!!.findViewById<TextView>(R.id.feedbackUsername).text = data.userName
        view.findViewById<TextView>(R.id.textViewFeedback).text = data.feedback



        view.setOnClickListener {
            onItemClickListener?.onItemClick(data)
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
