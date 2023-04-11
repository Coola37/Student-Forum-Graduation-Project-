package com.yigitkula.studentforum.utils

import com.yigitkula.studentforum.model.Users

class EventbusDataEvents {
    internal class getUserInfo(var email:String?, var verificationID: String?, var pass:String?)

    internal class SendUserInformation(var user: Users)
}