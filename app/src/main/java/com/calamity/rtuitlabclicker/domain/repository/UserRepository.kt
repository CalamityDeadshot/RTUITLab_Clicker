package com.calamity.rtuitlabclicker.domain.repository

import com.calamity.rtuitlabclicker.data.remote.dto.AccessToken
import com.calamity.rtuitlabclicker.domain.model.User

interface UserRepository {

    suspend fun getUsers(): List<User>

    suspend fun getUserByName(name: String): User

    suspend fun getUserInfo(token: String)

    suspend fun getUserInfoFromCode(code: String)

    suspend fun getLocalUserInfo(): User?

    suspend fun update(user: User)

    suspend fun updateFromApi(userFromApi: User)

    suspend fun delete(user: User)

    suspend fun insert(user: User)
}