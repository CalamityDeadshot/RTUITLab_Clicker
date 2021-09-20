package com.calamity.rtuitlabclicker.common

import androidx.lifecycle.MutableLiveData
import com.calamity.rtuitlabclicker.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.properties.Delegates

object Variables {
    val activeUser = MutableStateFlow(null as User?)

}