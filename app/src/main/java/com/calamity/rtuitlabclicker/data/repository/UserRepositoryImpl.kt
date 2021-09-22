package com.calamity.rtuitlabclicker.data.repository

import android.content.SharedPreferences
import android.util.Log
import com.calamity.rtuitlabclicker.common.Constants
import com.calamity.rtuitlabclicker.common.Utils
import com.calamity.rtuitlabclicker.data.local.dao.UserDao
import com.calamity.rtuitlabclicker.data.remote.GithubApi
import com.calamity.rtuitlabclicker.data.remote.GithubAuthApi
import com.calamity.rtuitlabclicker.data.remote.dto.AccessTokenLogout
import com.calamity.rtuitlabclicker.data.remote.dto.toUser
import com.calamity.rtuitlabclicker.domain.model.User
import com.calamity.rtuitlabclicker.domain.repository.UserRepository
import java.lang.Exception
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

    override suspend fun getUserInfo(token: String): User? {
        val user = api.getUserInfo("token $token").toUser()
        Log.v("API", "got user id ${user.id} from api with token $token")
        val users = getUsers()
        if (users.isEmpty()) {
            addUser(user.copy(authToken = token))
            return user
        }
        var hasCollision = false
        getUsers().forEach {
            if (it.name == user.name) {
                update(user.copy(authToken = token, counter = it.counter))
                hasCollision = true
                return@forEach
            }
        }
        if (!hasCollision) addUser(user.copy(authToken = token))
        return user
    }

    private suspend fun addUser(user: User) {
        insert(user)
        Log.v("LOCAL", "put ${user.id} as user id to prefs")
        prefs.edit()
            .putInt(Constants.SHARED_PREFS_KEY, user.id).apply()
    }

    override suspend fun getUserInfoFromCode(code: String): User? {
        Log.v("API", "parameters ${Constants.CLIENT_ID}, ${Constants.CLIENT_SECRETS}, $code")
        return try {
            getUserInfo(
                authApi.getToken(
                    Constants.CLIENT_ID,
                    Constants.CLIENT_SECRETS,
                    code
                ).accessToken
            )
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getLocalUserInfo(): User? {
        val userId = prefs.getInt(Constants.SHARED_PREFS_KEY, -1)
        Log.v("LOCAL", "got active id of $userId")
        return if (userId == -1) null else getUserById(userId)
    }

    override fun getUserById(id: Int): User? = userDao.getUserById(id)

    override suspend fun logOut() {
        Log.v("API", "Logging out with token ${getLocalUserInfo()!!.authToken}")
        api.logOut(
            "Basic ${Utils.encode64(Constants.CLIENT_ID, Constants.CLIENT_SECRETS)}",
            Constants.CLIENT_ID,
            AccessTokenLogout(getLocalUserInfo()!!.authToken)
        )
        prefs.edit().remove(Constants.SHARED_PREFS_KEY).apply()
    }

    override suspend fun update(user: User) {
        userDao.update(user)
        prefs.edit().putInt(Constants.SHARED_PREFS_KEY, user.id).apply()
    }

    override suspend fun updateFromApi(userFromApi: User) {
        update(userFromApi)
    }

    override suspend fun delete(user: User) =
        userDao.delete(user)

    override suspend fun insert(user: User) =
        userDao.insert(user)


}