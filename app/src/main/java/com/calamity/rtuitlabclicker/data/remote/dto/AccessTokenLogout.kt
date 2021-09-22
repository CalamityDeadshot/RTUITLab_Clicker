package com.calamity.rtuitlabclicker.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AccessTokenLogout(
    @SerializedName("access_token") val token: String
)