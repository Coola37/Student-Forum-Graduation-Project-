package com.yigitkula.studentforum.model

class Feedbacks {
    var postID: String? = null
    var feedbackID: String? = null
    var senderID: String? = null
    var userName: String? = null
    var feedback: String? = null

    constructor()

    constructor(postID: String?, feedbackID: String?, senderID: String?, userName: String?, feedback: String?) {
        this.postID = postID
        this.feedbackID = feedbackID
        this.senderID = senderID
        this.userName = userName
        this.feedback = feedback
    }
}