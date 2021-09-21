package com.calamity.rtuitlabclicker.data.remote

import com.calamity.rtuitlabclicker.data.remote.dto.UserDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface GithubApi {
    @GET("user")
    suspend fun getUserInfo(
        @Header("Authorization") token: String
    ): UserDto
}