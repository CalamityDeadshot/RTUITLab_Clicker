package com.calamity.rtuitlabclicker.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.calamity.rtuitlabclicker.R
import com.calamity.rtuitlabclicker.common.Constants
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


        this.lifecycleScope.launchWhenStarted {
            viewModel.user.observe(this@MainActivity) {
                Log.v("API", "token from observer: ${it?.authToken}")
                if (it != null) {
                    try {
                        navController.navigate(R.id.action_authenticationFragment_to_clickerFragment)
                    } catch (e: Exception) {}
                }
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
            viewModel.getUserInfo(code!!)
        }
    }

}