package com.calamity.rtuitlabclicker.data.remote

import com.calamity.rtuitlabclicker.data.remote.dto.AccessToken
import com.calamity.rtuitlabclicker.data.remote.dto.UserDto
import retrofit2.Call
import retrofit2.http.*

interface GithubAuthApi {

    @Headers("Accept: application/json")
    @POST("access_token")
    @FormUrlEncoded
    suspend fun getToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String
    ) : AccessToken
}