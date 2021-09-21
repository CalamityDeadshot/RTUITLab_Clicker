package com.calamity.rtuitlabclicker.presentation.clicker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.calamity.rtuitlabclicker.R
import com.calamity.rtuitlabclicker.common.Constants
import com.calamity.rtuitlabclicker.databinding.FragmentAuthenticationBinding
import com.calamity.rtuitlabclicker.databinding.FragmentClickerBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_clicker.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClickerFragment : Fragment(R.layout.fragment_clicker) {

    private val viewModel: ClickerViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding(view)
    }

    private fun initBinding(view: View) {
        val binding = FragmentClickerBinding.bind(view)

        lifecycleScope.launch {
            viewModel.user.observe(viewLifecycleOwner) { user ->
                Log.v("observer", "tick, null? $user")
                if (user != null) {
                    Picasso.get().load(user.profileImageUri).into(avatar)
                    user_name.text = user.name
                    clicks.text = "Clicks: ${user.counter}"
                }
            }

            /*viewModel.counter.observe(viewLifecycleOwner) {
                clicks.text = "Clicks: $it"
            }*/
        }

        binding.apply {
            buttonClick.setOnClickListener {
                viewModel.onClick()
            }
        }
    }
}