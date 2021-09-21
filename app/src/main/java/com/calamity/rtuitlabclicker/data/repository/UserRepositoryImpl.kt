package com.calamity.rtuitlabclicker.data.repository

import android.content.SharedPreferences
import android.util.Log
import com.calamity.rtuitlabclicker.common.Constants
import com.calamity.rtuitlabclicker.data.local.dao.UserDao
import com.calamity.rtuitlabclicker.data.remote.GithubApi
import com.calamity.rtuitlabclicker.data.remote.GithubAuthApi
import com.calamity.rtuitlabclicker.data.remote.dto.toUser
import com.calamity.rtuitlabclicker.domain.model.User
import com.calamity.rtuitlabclicker.domain.repository.UserRepository
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
        Log.v("API", "got user id ${user.id} from api")
        val users = getUsers()
        if (users.isEmpty()) {
            addUser(user)
            return user
        }
        getUsers().forEach {
            if (it.name == user.name) {
                update(user)
                return@forEach
            }
        }
        return user
    }

    private suspend fun addUser(user: User) {
        insert(user)
        val userId = getUserByName(user.name).id
        Log.v("LOCAL", "put $userId as user id to prefs")
        prefs.edit()
            .putInt(Constants.SHARED_PREFS_KEY, userId).apply()
    }

    override suspend fun getUserInfoFromCode(code: String): User? {
        Log.v("API", "parameters ${Constants.CLIENT_ID}, ${Constants.CLIENT_SECRETS}, $code")
        return getUserInfo(authApi.getToken(Constants.CLIENT_ID, Constants.CLIENT_SECRETS, code).accessToken)
    }

    override suspend fun getLocalUserInfo(): User? {
        val userId = prefs.getInt(Constants.SHARED_PREFS_KEY, -1)
        Log.v("LOCAL", "got active id of $userId")
        if (userId == -1) {
            return null
        }
        return getUserById(userId)
    }

    override fun getUserById(id: Int): User? = userDao.getUserById(id)


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