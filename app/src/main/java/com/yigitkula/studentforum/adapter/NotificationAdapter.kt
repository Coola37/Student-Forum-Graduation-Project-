import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.model.NotificationFeedback
import com.yigitkula.studentforum.utils.EventbusDataEvents
import org.greenrobot.eventbus.EventBus


class NotificationAdapter(
    private val context: Context,
    private val feedbackList: List<NotificationFeedback>,
    private val onItemClick: (NotificationFeedback) -> Unit // Item click listener
) : RecyclerView.Adapter<NotificationAdapter.FeedbackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.notification_item, parent, false)
        return FeedbackViewHolder(view)
    }


    override fun onBindViewHolder(holder: FeedbackViewHolder, position: Int) {
        val feedback = feedbackList[position]
        holder.bind(feedback)
    }

    override fun getItemCount(): Int {
        return feedbackList.size
    }

    inner class FeedbackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(feedback: NotificationFeedback) {
            // Verileri görsel öğelerle ilişkilendirme işlemleri
            var textViewTitle = itemView.findViewById<TextView>(R.id.textViewTitle)
            textViewTitle.text = feedback.title
            var textViewBody = itemView.findViewById<TextView>(R.id.textViewBody)
            textViewBody.text = feedback.body
            var targetUser:String = "targetUser"
            // Diğer görsel öğeleri güncelleme işlemleri
            var notificationFeedback = NotificationFeedback(feedback.targetUser,feedback.title, feedback.body, feedback.postId, feedback.feedbackId)
            EventBus.getDefault().postSticky(EventbusDataEvents.GetNotificationInfo(notificationFeedback))

            itemView.setOnClickListener {
                onItemClick(feedback)
            }
        }
    }
}
