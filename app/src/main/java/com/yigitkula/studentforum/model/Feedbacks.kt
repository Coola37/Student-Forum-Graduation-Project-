package com.yigitkula.studentforum.model

class Feedbacks {
    var postID: String? = null
    var feedbackID: String? = null
    var senderID: String? = null
    var userName: String? = null
    var feedback: String? = null
    var date: String? = null

    constructor()

    constructor(postID: String?, feedbackID: String?, senderID: String?, userName: String?, feedback: String?, date: String?) {
        this.postID = postID
        this.feedbackID = feedbackID
        this.senderID = senderID
        this.userName = userName
        this.feedback = feedback
        this.date = date
    }
}