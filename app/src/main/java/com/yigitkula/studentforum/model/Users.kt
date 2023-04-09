package com.yigitkula.studentforum.model

class Users {
    var email: String? = null
   // var password: String? = null
    var user_name: String? = null
    var name: String? = null
    var surname: String? = null
    var user_id: String? = null

    constructor() {}

    constructor(
        email: String?,
   //     password: String?,
        user_name: String?,
        name: String?,
        surname: String?,
        user_id: String?
    ) {
        this.email = email
    //    this.password = password
        this.user_name = user_name
        this.name = name
        this.surname = surname
        this.user_id = user_id
    }
}