package com.calamity.rtuitlabclicker.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calamity.rtuitlabclicker.common.Constants
import com.calamity.rtuitlabclicker.common.enqueue
import com.calamity.rtuitlabclicker.data.remote.GithubAuthApi
import com.calamity.rtuitlabclicker.domain.model.User
import com.calamity.rtuitlabclicker.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repo: UserRepository
) : ViewModel() {

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    var intentProcessed = false

    fun getUserInfo(code: String) = viewModelScope.launch(Dispatchers.IO) {
        _user.postValue(repo.getUserInfoFromCode(code))
    }

    fun onLogout() {
        _user.value = null
    }
}