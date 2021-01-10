package com.androidaccademy.authentication.ui.login

import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.androidaccademy.authentication.R
import com.androidaccademy.authentication.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

	private val viewModel by viewModels<LoginViewModel>()
	private lateinit var binding: ActivityLoginBinding
	val isSignUp by lazy { viewModel.getIsSignUp() }

	private fun getAccountName(intent: Intent) = intent.getStringExtra(EXT_ACCOUNT_NAME).orEmpty()
	private fun getAccountType(intent: Intent) = intent.getStringExtra(EXT_ACCOUNT_TYPE).orEmpty()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
		initViews()
		observeEvents()
	}

	private fun observeEvents() {
		viewModel.getOnLoginCompleteEvent().observe(this, { event ->
			when (event.getContentIfNotHandled()) {
				LoginEvents.UserLoggedIn -> {
					setResult(RESULT_OK)
					finish()
				}
				LoginEvents.UserLoginFailed -> Toast.makeText(
					this,
					getString(R.string.login_failed),
					Toast.LENGTH_SHORT
				).show()
			}
		})

		isSignUp.observe(this, { isSignUp ->
			if (isSignUp) {
				binding.name.visibility = View.VISIBLE
				binding.signUp.setText(getString(R.string.new_user))
			} else {
				binding.name.visibility = View.GONE
				binding.signUp.setText(getString(R.string.already_have_account))
			}
		})
	}

	private fun initViews() {
		binding.apply {
			accountName.setText(getAccountName(intent))
			submit.setOnClickListener {
				if (isSignUp.value == true) {
					signIn()
				} else {
					signUp()
				}
			}
			signUp.setOnClickListener {
				viewModel.toggleSignInUp()
			}
		}
	}

	private fun signIn() {
		val userName = binding.accountName.text.toString()
		val password = binding.accountPassword.text.toString()
		viewModel.signIn(
			this,
			userName,
			password,
			getAccountType(intent)
		)
	}

	private fun signUp() {
		val name = binding.name.text.toString()
		val userName = binding.accountName.text.toString()
		val password = binding.accountPassword.text.toString()
		viewModel.signUp(
			this,
			name,
			userName,
			password,
			getAccountType(intent)
		)
	}

	companion object {
		internal const val EXT_ACCOUNT_NAME = "EXT_ACCOUNT_NAME"
		internal const val EXT_ACCOUNT_TYPE = "EXT_ACCOUNT_TYPE"

		fun newIntentAddNewAccount(
			context: Context,
			accountType: String?) =
			Intent(context, LoginActivity::class.java)
				.putExtra(EXT_ACCOUNT_TYPE, accountType)
	}
}
