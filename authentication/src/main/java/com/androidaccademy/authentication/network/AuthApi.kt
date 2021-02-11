package com.androidaccademy.authentication.network

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.androidaccademy.authentication.services.authenticator.ACCESS_TOKEN
import com.androidaccademy.authentication.services.authenticator.AccountManagerProvider
import com.androidaccademy.authentication.services.authenticator.REFRESH_TOKEN
import java.lang.Thread.sleep

class AuthApi private constructor() {

	fun signUp(name: String, userName: String, password: String) =
		Credentials("my_access_token", "my_refresh_token")
			.also {
				sleep(1000)
			}

	fun signIn(userName: String, password: String) =
		Credentials("my_access_token", "my_refresh_token")
			.also {
				sleep(1000)
			}

	fun refreshToken(refreshToken: String?, context: Context) {
		sleep(1000)
		if (refreshToken != "my_refresh_token") {
			Handler(Looper.getMainLooper()).post {
				Toast.makeText(context, "Please pass refresh token. Aborting", Toast.LENGTH_SHORT)
					.show()
			}
			return
		}

		AccountManagerProvider.getCurrentAccount(context)?.let { account ->
			AccountManagerProvider.setAuthToken(account, ACCESS_TOKEN, "new_access_token", context)
			AccountManagerProvider.setAuthToken(
				account,
				REFRESH_TOKEN,
				"new_refresh_token",
				context
			)
		}

	}

	companion object {
		val instance = AuthApi()
	}
}

data class Credentials(val accessToken: String, val refreshToken: String)
