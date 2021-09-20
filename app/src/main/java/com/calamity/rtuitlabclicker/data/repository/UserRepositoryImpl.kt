package com.calamity.rtuitlabclicker.data.repository

import android.content.SharedPreferences
import android.util.Log
import com.calamity.rtuitlabclicker.common.Constants
import com.calamity.rtuitlabclicker.common.Variables
import com.calamity.rtuitlabclicker.common.enqueue
import com.calamity.rtuitlabclicker.data.local.UsersDatabase
import com.calamity.rtuitlabclicker.data.local.dao.UserDao
import com.calamity.rtuitlabclicker.data.remote.GithubApi
import com.calamity.rtuitlabclicker.data.remote.GithubAuthApi
import com.calamity.rtuitlabclicker.data.remote.dto.AccessToken
import com.calamity.rtuitlabclicker.data.remote.dto.toUser
import com.calamity.rtuitlabclicker.domain.model.User
import com.calamity.rtuitlabclicker.domain.repository.UserRepository
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val api: GithubApi,
    private val authApi: GithubAuthApi,
    private val prefs: SharedPreferences
) : UserRepository {

    override suspend fun getUsers(): List<User> =
        userDao.getAllUsers()

    override suspend fun getUserByName(name: String): User =
        userDao.getUserByName(name)

    override suspend fun getUserInfo(token: String) =
        api.getUserInfo("token $token")
            .enqueue {
                onResponse = { response ->
                    Log.v("API", response.body()?.toString()!!)
                    val user = response.body()!!.toUser()
                    // Determine if user is already present
                    GlobalScope.launch {
                        val users = getUsers()
                        if (users.isEmpty()) {
                            addUser(user)
                            return@launch
                        }
                        users.forEach {
                            if (it.name == user.name) {
                                addUser(it)
                                return@forEach
                            }
                        }
                    }
                }
            }

    private suspend fun addUser(user: User) {
        val newUserData = user.copy(
            counter = user.counter,
            id = user.id
        )
            insert(newUserData)
        Variables.activeUser.value = newUserData
        prefs.edit()
            .putString(Constants.SHARED_PREFS_KEY, newUserData.toString()).apply()
    }

    override suspend fun getUserInfoFromCode(code: String) {
        Log.v("API", "parameters ${Constants.CLIENT_ID}, ${Constants.CLIENT_SECRETS}, $code")
        authApi.getToken(Constants.CLIENT_ID, Constants.CLIENT_SECRETS, code)
            .enqueue {
                onResponse = { response ->
                    Log.v("API", response.raw().message())
                    GlobalScope.launch {
                        getUserInfo(response.body()?.accessToken!!)
                    }
                }
                onFailure = {
                    Log.v("API", it?.message!!)
                }
            }
    }

    override suspend fun getLocalUserInfo(): User? {
        val userStr = prefs.getString(Constants.SHARED_PREFS_KEY, "")!!
        if (userStr.isEmpty()) {
            Variables.activeUser.value = null
            return null
        }
        val user = Gson().fromJson(userStr, User::class.java)
        Variables.activeUser.value = user
        return user
    }


    override suspend fun update(user: User) {
        userDao.update(user)
        prefs.edit().putString(Constants.SHARED_PREFS_KEY, user.toString()).apply()
    }

    override suspend fun updateFromApi(userFromApi: User) {
        val existingUser = getUserByName(userFromApi.name)
        update(User(
            authToken = existingUser.authToken,
            name = userFromApi.name,
            profileImageUri = userFromApi.profileImageUri,
            counter = existingUser.counter,
            id = existingUser.id
        ))
    }

    override suspend fun delete(user: User) =
        userDao.delete(user)

    override suspend fun insert(user: User) =
        userDao.insert(user)


}