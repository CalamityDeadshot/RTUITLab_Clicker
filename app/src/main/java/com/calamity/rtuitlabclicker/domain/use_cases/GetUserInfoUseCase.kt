package com.calamity.rtuitlabclicker.domain.use_cases

import com.calamity.rtuitlabclicker.common.Resource
import com.calamity.rtuitlabclicker.common.Variables
import com.calamity.rtuitlabclicker.domain.model.User
import com.calamity.rtuitlabclicker.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val repository: UserRepository
) {
    fun withCode(code: String): Flow<Resource<User>> = flow<Resource<User>> {
        try {
            emit(Resource.Loading<User>())
            val user = repository.getUserInfoFromCode(code)
            emit(Resource.Success(Variables.activeUser.value!!))
        } catch (e: HttpException) {
            emit(Resource.Error<User>(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error<User>("Couldn't reach server"))
        }
    }

    fun withToken(token: String): Flow<Resource<User>> = flow<Resource<User>> {
        try {
            emit(Resource.Loading<User>())
            val user = repository.getUserInfo(token)
            emit(Resource.Success(Variables.activeUser.value!!))
        } catch (e: HttpException) {
            emit(Resource.Error<User>(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error<User>("Couldn't reach server"))
        }
    }

    fun local(): Flow<User> = flow {
        repository.getLocalUserInfo()
    }
}