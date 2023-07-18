package com.yigitkula.studentforum.model

class Post {

    var post_id: String? = null
    var sender_user:String? = null
    var course_name: String? = null
    var topic: String? = null
    var problem: String? = null
    var problem_img: String? = null
    var date: String? = null
    constructor()
    constructor(
        post_id: String?,
        sender_user: String?,
        course_name: String?,
        topic: String?,
        problem: String?,
        problem_img: String?,
        date: String?
    ) {
        this.post_id = post_id
        this.sender_user = sender_user
        this.course_name = course_name
        this.topic = topic
        this.problem = problem
        this.problem_img = problem_img
        this.date=date
    }
    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Post) {
            return false
        }

        return post_id == other.post_id &&
                course_name == other.course_name &&
                topic == other.topic
    }
}