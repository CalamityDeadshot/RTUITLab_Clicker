package com.calamity.rtuitlabclicker.presentation.start

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.calamity.rtuitlabclicker.R
import com.calamity.rtuitlabclicker.common.Constants
import com.calamity.rtuitlabclicker.common.Variables
import com.calamity.rtuitlabclicker.domain.model.User
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartFragment : Fragment(R.layout.fragment_start) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireContext().getSharedPreferences(Constants.SHARED_PREFS_FILE, Context.MODE_PRIVATE)
        val userStr = prefs.getString(Constants.SHARED_PREFS_KEY, "")
        if (userStr!!.isEmpty())
            findNavController().navigate(R.id.action_startFragment_to_authenticationFragment)
        else {
            val activeUser: User = Gson().fromJson(userStr, User::class.java)
            Variables.activeUser.value = activeUser
            findNavController().navigate(R.id.action_startFragment_to_clickerFragment)
        }
    }
}