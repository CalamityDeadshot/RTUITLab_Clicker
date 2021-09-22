package com.calamity.rtuitlabclicker.data.remote

import com.calamity.rtuitlabclicker.data.remote.dto.AccessToken
import com.calamity.rtuitlabclicker.data.remote.dto.AccessTokenLogout
import com.calamity.rtuitlabclicker.data.remote.dto.UserDto
import retrofit2.Response
import retrofit2.http.*

interface GithubApi {
    @GET("user")
    suspend fun getUserInfo(
        @Header("Authorization") token: String
    ): UserDto

    @HTTP(method = "DELETE", path = "applications/{clientId}/grant", hasBody = true)
    suspend fun logOut(
        @Header("Authorization") encoded: String,
        @Path("clientId") clientId: String,
        @Body accessToken: AccessTokenLogout
    ): Response<Unit>
}