package com.calamity.rtuitlabclicker.common

import com.calamity.rtuitlabclicker.BuildConfig

object Constants {
    const val REDIRECT_URL =     "calamitydeadshot://callback"
    const val CLIENT_ID =        BuildConfig.CLIENT_ID
    const val CLIENT_SECRETS =   BuildConfig.CLIENT_SECRETS
    const val OAUTH_URL =        "https://github.com/login/oauth/"
    const val BASE_URL =         "https://api.github.com/"
    const val SHARED_PREFS_FILE = "active_user"
    const val SHARED_PREFS_KEY = "ACTIVE_USER"
}