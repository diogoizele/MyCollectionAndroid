package com.diogo.mycollection.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.diogo.mycollection.MainActivity
import com.diogo.mycollection.data.source.InMemoryAuthRepository
import com.diogo.mycollection.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    private val authRepository = InMemoryAuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        val nextIntent = if (authRepository.isLoggedIn()) {
            MainActivity.createIntent(this)
        } else {
            LoginActivity.createIntent(this)
        }

        startActivity(nextIntent)
        finish()
    }
}