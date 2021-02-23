package com.androidaccademy.myapplication.network

import com.androidaccademy.authentication.network.AuthApi
import com.androidaccademy.authentication.services.authenticator.AccountManagerProvider
import com.androidaccademy.myapplication.android.App
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber

class MyAuthenticator : Authenticator {

	override fun authenticate(route: Route?, response: Response): Request? {

		val request = response.request
		if (hasCurrentAuthorizationHeader(request)) {
			blockingRefreshToken()
		}

		return buildRequestWithNewHeaders(request)
	}

	private fun hasCurrentAuthorizationHeader(request: Request) =
		request.headers
			.firstOrNull { it.first == AUTHORIZATION_HEADER }
			.let { it?.second == AccountManagerProvider.getAccessToken(App.instance) }

	private fun blockingRefreshToken() {
		// Exercise 2
		// Get our current Refresh Token from AccountManagerProvider
		// (we do something similar with our Access Token)

		val currentRefreshToken = AccountManagerProvider.getRefreshToken(App.instance)
		AuthApi.instance.refreshToken(currentRefreshToken, App.instance)
		Timber.e("token refreshed")
	}

	private fun buildRequestWithNewHeaders(request: Request) =
		request.newBuilder()
			.removeHeader(AUTHORIZATION_HEADER)
			.addHeader(AUTHORIZATION_HEADER, "${AccountManagerProvider.getAccessToken(App.instance)}")
			.build()
}
