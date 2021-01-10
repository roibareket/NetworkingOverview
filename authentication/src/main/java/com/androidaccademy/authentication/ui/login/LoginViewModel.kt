package com.androidaccademy.authentication.ui.login

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidaccademy.authentication.livedata.Event
import com.androidaccademy.authentication.network.AuthApi
import com.androidaccademy.authentication.services.authenticator.AUTH_TOKEN_TYPE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

	private val isSignUp = MutableLiveData<Boolean>(false)
	private val onLoginComplete = MutableLiveData<Event<LoginEvents>>()

	fun getOnLoginCompleteEvent(): LiveData<Event<LoginEvents>> = onLoginComplete
	fun getIsSignUp(): LiveData<Boolean> = isSignUp

	private fun asyncJob(block: suspend CoroutineScope.() -> Unit): Job {
		return viewModelScope.launch(Dispatchers.IO) {
			block()
		}
	}

	fun signIn(
		context: Context,
		userName: String,
		password: String,
		accountType: String
	) {
		val appContext = context.applicationContext
		asyncJob {
			try {
				val token = AuthApi.instance.signIn(userName, password)
				addAccount(
					appContext,
					userName,
					password,
					accountType,
					token
				)
			} catch (ex: Exception) {
				onLoginComplete.postValue(Event(LoginEvents.UserLoginFailed))
			}
		}
	}

	fun signUp(
		context: Context,
		name: String,
		accountName: String,
		password: String,
		accountType: String
	) {
		val appContext = context.applicationContext
		asyncJob {
			val token = AuthApi.instance.signUp(name, accountName, password)
			addAccount(
				appContext,
				accountName,
				password,
				accountType,
				token
			)
		}
	}

	fun addAccount(
		context: Context,
		userName: String,
		password: String,
		accountType: String,
		token: String
	) {
		val appContext = context.applicationContext
		asyncJob {
			val account = Account(userName, accountType)
			AccountManager.get(appContext).let { accountManager ->
				accountManager.addAccountExplicitly(account, password, null)
				accountManager.setAuthToken(account, AUTH_TOKEN_TYPE, token)
			}

			onLoginComplete.postValue(Event(LoginEvents.UserLoggedIn))
		}
	}

	fun toggleSignInUp() = isSignUp.postValue(isSignUp.value?.not())
}

sealed class LoginEvents {
	object UserLoggedIn : LoginEvents()
	object UserLoginFailed : LoginEvents()
}
