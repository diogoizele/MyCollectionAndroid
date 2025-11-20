package com.diogo.mycollection.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.core.view.WindowCompat
import com.diogo.mycollection.MainActivity
import com.diogo.mycollection.core.extensions.applyKeyboardInsets
import com.diogo.mycollection.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.applyKeyboardInsets(binding.root)

        supportActionBar?.hide()

        setupListeners()
        observeUiState()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            currentFocus!!.clearFocus()
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun setupListeners() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            viewModel.login(email, password)
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is LoginUiState.Idle -> {
                        binding.loginButton.isEnabled = true
                        binding.progressBar.visibility = View.GONE
                    }
                    is LoginUiState.Loading -> {
                        binding.loginButton.isEnabled = false
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is LoginUiState.Success -> {
                        startActivity(MainActivity.createIntent(this@LoginActivity))
                        finish()
                    }
                    is LoginUiState.Error -> {
                        binding.loginButton.isEnabled = true
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@LoginActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
