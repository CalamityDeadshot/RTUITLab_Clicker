package com.calamity.rtuitlabclicker.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calamity.rtuitlabclicker.common.Constants
import com.calamity.rtuitlabclicker.common.enqueue
import com.calamity.rtuitlabclicker.data.remote.GithubAuthApi
import com.calamity.rtuitlabclicker.domain.repository.UserRepository
import com.calamity.rtuitlabclicker.domain.use_cases.GetUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repo: UserRepository,
    private val getUserInfoUseCase: GetUserInfoUseCase
) : ViewModel() {

    fun getAccessToken(code: String) = viewModelScope.launch {
        repo.getUserInfoFromCode(code)
    }
}