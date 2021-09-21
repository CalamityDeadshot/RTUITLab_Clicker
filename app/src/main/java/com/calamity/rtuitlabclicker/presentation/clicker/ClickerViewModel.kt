package com.calamity.rtuitlabclicker.presentation.clicker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calamity.rtuitlabclicker.domain.model.User
import com.calamity.rtuitlabclicker.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClickerViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _user = MutableLiveData<User>()

    val user: LiveData<User> = _user

    init {
        getUser()
    }

    private fun getUser() = viewModelScope.launch(Dispatchers.IO) {
        val user = repository.getLocalUserInfo()
        _user.postValue(user!!)
    }

    fun onClick() = viewModelScope.launch {
        val user = user.value
        repository.update(
            user!!.copy(
                authToken = user.authToken,
                name = user.name,
                profileImageUri = user.profileImageUri,
                counter = user.counter + 1,
                id = user.id
            )
        )
        getUser()
    }

}