package com.yigitkula.studentforum.utils

import com.yigitkula.studentforum.model.Feedbacks
import com.yigitkula.studentforum.model.Post
import com.yigitkula.studentforum.model.Users

class EventbusDataEvents {
    internal class getUserInfo(var email:String?, var verificationID: String?, var pass:String?)

    internal class getPostInfo(var postID:String?, var coursName:String?, var problem:String?, var problemImgString:String?,
                               var senderUser: String?, var topic:String?)
    internal class SendUserInformation(var user: Users?)
    internal class SendPostInfo(var post: Post)

    internal class GetFeedbackSenderID(var senderID: String?)
}