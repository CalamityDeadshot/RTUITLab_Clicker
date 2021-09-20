package com.calamity.rtuitlabclicker.domain.use_cases

import com.calamity.rtuitlabclicker.domain.model.User
import com.calamity.rtuitlabclicker.domain.repository.UserRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateUserInfoUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(user: User, counter: Long) = flow {
        repository.update(user.copy(
            authToken = user.authToken,
            name = user.name,
            profileImageUri = user.profileImageUri,
            counter = counter,
            id = user.id
        ))
        emit(counter)
    }
}