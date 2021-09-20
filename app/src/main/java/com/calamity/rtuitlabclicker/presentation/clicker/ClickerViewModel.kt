package com.calamity.rtuitlabclicker.presentation.clicker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calamity.rtuitlabclicker.common.Resource
import com.calamity.rtuitlabclicker.common.Variables
import com.calamity.rtuitlabclicker.domain.model.User
import com.calamity.rtuitlabclicker.domain.repository.UserRepository
import com.calamity.rtuitlabclicker.domain.use_cases.GetUserInfoUseCase
import com.calamity.rtuitlabclicker.domain.use_cases.UpdateUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClickerViewModel @Inject constructor(
    private val repository: UserRepository,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val updateUserInfoUseCase: UpdateUserInfoUseCase
) : ViewModel() {

/*    private val _state = MutableStateFlow(ClickerState(user = Variables.activeUser.value, counter = Variables.activeUser.value?.counter ?: -1))
    val state: StateFlow<ClickerState> = _state*/
    private val _state = MutableLiveData<ClickerState>(ClickerState(Variables.activeUser.value, Variables.activeUser.value?.counter ?: -1))
    val state: LiveData<ClickerState> = _state

    private val _counter = MutableLiveData<Long>(Variables.activeUser.value?.counter ?: -1)
    val counter: LiveData<Long> = _counter

    init {
        getUser()
    }

    private fun getUser() = viewModelScope.launch {
        val user = repository.getLocalUserInfo()
        _state.value = ClickerState(user, user?.counter!!)

    }

    fun onClick() {
        _counter.value = counter.value?.plus(1)
        val user = Variables.activeUser.value!!
        viewModelScope.launch {
            repository.update(
                user.copy(
                    authToken = user.authToken,
                    name = user.name,
                    profileImageUri = user.profileImageUri,
                    counter = _counter.value!!,
                    id = user.id
                )
            )
        }
    }
}