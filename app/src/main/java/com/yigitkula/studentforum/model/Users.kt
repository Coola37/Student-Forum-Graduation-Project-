package com.yigitkula.studentforum.model
class Users {
    var email: String? = null
    var user_name: String? = null
    var name: String? = null
    var surname: String? = null
    var user_id: String? = null
    var user_detail: UserDetails? = null
    constructor() {}
    constructor(
        email: String?,
        user_name: String?,
        name: String?,
        surname: String?,
        user_id: String?
    ) {
        this.email = email
        this.user_name = user_name
        this.name = name
        this.surname = surname
        this.user_id = user_id
    }
    constructor(
        email: String?,
        user_name: String?,
        name: String?,
        surname: String?,
        user_id: String?,
        user_detail: UserDetails?
    ) {
        this.email = email
        this.user_name = user_name
        this.name = name
        this.surname = surname
        this.user_id = user_id
        this.user_detail = user_detail
    }
    override fun toString(): String {
        return super.toString()
    }
}