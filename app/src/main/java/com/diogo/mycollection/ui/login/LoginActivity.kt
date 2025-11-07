package com.diogo.mycollection.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.activity.viewModels
import com.diogo.mycollection.MainActivity
import com.diogo.mycollection.data.source.InMemoryAuthRepository
import com.diogo.mycollection.databinding.ActivityLoginBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repository = InMemoryAuthRepository()
                return LoginViewModel(repository) as T
            }
        }
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupListeners()
        observeUiState()
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
