package com.calamity.rtuitlabclicker.presentation

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.calamity.rtuitlabclicker.R
import com.calamity.rtuitlabclicker.common.Constants
import com.calamity.rtuitlabclicker.common.Variables
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.findNavController()

        setupActionBarWithNavController(navController)


        Variables.activeUser.asLiveData().observe(this) {
            Log.v("API", "token from observer: ${it?.authToken}")
            if (it != null) {
                try {
                    navController.navigate(R.id.action_authenticationFragment_to_clickerFragment)
                } catch (e: Exception) {}
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()

        val uri = intent.data
        Log.v("API", "uri: ${uri.toString()}")
        if (uri != null && uri.toString().startsWith(Constants.REDIRECT_URL)) {
            val code = uri.getQueryParameter("code")
            Log.v("API", "code: $code")
            viewModel.getAccessToken(code!!)
        }
    }

}