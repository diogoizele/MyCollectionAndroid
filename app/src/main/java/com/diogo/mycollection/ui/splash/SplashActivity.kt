package com.diogo.mycollection.ui.splash

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.diogo.mycollection.MainActivity
import com.diogo.mycollection.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is SplashUiState.LoggedIn -> {
                            startActivity(MainActivity.createIntent(this@SplashActivity))
                            finish()
                        }
                        is SplashUiState.LoggedOut -> {
                            startActivity(LoginActivity.createIntent(this@SplashActivity))
                            finish()
                        }
                        else -> {}
                    }
                }
            }
        }

        viewModel.checkLoginStatus()
    }
}