package com.yigitkula.studentforum.model

class NotificationFeedback {
    var targetUser: String? = null
    var title: String? = null
    var body:String? = null
    var postId:String? = null
    var feedbackId:String? = null

    constructor(){

    }

    constructor(targetUser: String?, title: String?, body: String?, postID:String?, feedbackID:String? ) {
        this.targetUser = targetUser
        this.title = title
        this.body = body
        this.feedbackId = feedbackID
        this.postId = postID
    }


}