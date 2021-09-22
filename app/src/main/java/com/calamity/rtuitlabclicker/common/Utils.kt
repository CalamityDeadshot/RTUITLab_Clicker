package com.calamity.rtuitlabclicker.common

import android.util.Base64

class Utils {
    companion object {
        fun encode64(username: String, password: String): String {
            return Base64.encodeToString("$username:$password".toByteArray(), Base64.NO_WRAP)
        }
    }
}