package com.calamity.rtuitlabclicker.presentation.start

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.calamity.rtuitlabclicker.R
import com.calamity.rtuitlabclicker.common.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartFragment : Fragment(R.layout.fragment_start) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get active user id
        val prefs = requireContext().getSharedPreferences(Constants.SHARED_PREFS_FILE, Context.MODE_PRIVATE)
        val userId = prefs.getInt(Constants.SHARED_PREFS_KEY, -1)
        if (userId == -1)
            findNavController().navigate(R.id.action_startFragment_to_authenticationFragment)
        else
            findNavController().navigate(R.id.action_startFragment_to_clickerFragment)
    }
}