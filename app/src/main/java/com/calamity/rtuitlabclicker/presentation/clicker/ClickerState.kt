package com.calamity.rtuitlabclicker.presentation.clicker

import com.calamity.rtuitlabclicker.domain.model.User

data class ClickerState(
    val user: User? = null,
    val counter: Long = -1,
    val isLoading: Boolean = false,
    val error: String = ""
)