package com.yigitkula.studentforum.model

class UserDetails {
    var profile_picture: String? = null
    var school: String? =null
    var departmant: String? =null
    var rank: Int? = 0

    constructor()

    constructor(profilePicture: String?, school: String?, departmant: String?) {
        this.profile_picture = profilePicture
        this.school = school
        this.departmant = departmant
    }

    constructor(profilePicture: String?, school: String?, departmant: String?, rank: Int?) {
        this.profile_picture = profilePicture
        this.school = school
        this.departmant = departmant
        this.rank = rank
    }
}