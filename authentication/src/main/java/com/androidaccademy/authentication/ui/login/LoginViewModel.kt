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
import com.androidaccademy.authentication.network.Credentials
import com.androidaccademy.authentication.services.authenticator.ACCESS_TOKEN
import com.androidaccademy.authentication.services.authenticator.AccountManagerProvider
import com.androidaccademy.authentication.services.authenticator.REFRESH_TOKEN
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
				val credentials = AuthApi.instance.signIn(userName, password)
				addAccount(
					appContext,
					userName,
					password,
					accountType,
					credentials
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
			val credentials = AuthApi.instance.signUp(name, accountName, password)
			addAccount(
				appContext,
				accountName,
				password,
				accountType,
				credentials
			)
		}
	}

	private fun addAccount(
		context: Context,
		userName: String,
		password: String,
		accountType: String,
		credentials: Credentials
	) {
		val appContext = context.applicationContext
		asyncJob {
			val account = Account(userName, accountType)
			AccountManagerProvider.addAccount(account, password, context)
			AccountManagerProvider.setAuthToken(account,ACCESS_TOKEN, credentials.accessToken , context)
			AccountManagerProvider.setAuthToken(account,REFRESH_TOKEN, credentials.refreshToken, context)


			AccountManager.get(appContext).let { accountManager ->

				accountManager.setAuthToken(account, ACCESS_TOKEN, credentials.accessToken)
				accountManager.setAuthToken(account, REFRESH_TOKEN, credentials.refreshToken)
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
