package com.androidaccademy.myapplication.network

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import java.lang.Thread.sleep

class MyAuthenticator : Authenticator {

	private val accountManagerProvider = AccountManagerProvider.instance

	override fun authenticate(route: Route?, response: Response): Request? {

		val request = response.request
		if (hasCurrentAuthorizationHeader(request)) {
			blockingRefreshToken(request)
		}

		return buildRequestWithNewHeaders(request)
	}

	private fun buildRequestWithNewHeaders(request: Request) =
		request.newBuilder()
			.removeHeader(AUTHORIZATION_HEADER)
			.addHeader(AUTHORIZATION_HEADER, "${accountManagerProvider.getAuthToken()}_new")
			.build()

	private fun hasCurrentAuthorizationHeader(request: Request) =
		request.headers
			.firstOrNull { it.first == AUTHORIZATION_HEADER }
			.let { it?.second == accountManagerProvider.getAuthToken() }

	private fun blockingRefreshToken(request: Request) {
		sleep(2000)
		Timber.e("token refreshed")
	}
}
