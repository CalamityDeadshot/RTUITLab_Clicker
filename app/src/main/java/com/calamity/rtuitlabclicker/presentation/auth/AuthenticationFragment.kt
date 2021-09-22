package com.calamity.rtuitlabclicker.presentation.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.calamity.rtuitlabclicker.R
import com.calamity.rtuitlabclicker.common.Constants
import com.calamity.rtuitlabclicker.databinding.FragmentAuthenticationBinding
import com.calamity.rtuitlabclicker.presentation.MainActivity
import com.calamity.rtuitlabclicker.presentation.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationFragment : Fragment(R.layout.fragment_authentication) {

    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding(view)
    }

    private fun initBinding(view: View) {
        val binding = FragmentAuthenticationBinding.bind(view)

        binding.apply {
            githubLoginBtn.setOnClickListener {
                mainActivityViewModel.intentProcessed = false
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(
                    "${Constants.OAUTH_URL}authorize?client_id=${Constants.CLIENT_ID}&scope=user:read&redirect_uri=${Constants.REDIRECT_URL}"
                ))
                startActivity(intent)
            }
        }
    }
}