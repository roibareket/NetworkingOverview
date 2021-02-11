package com.androidaccademy.myapplication.network

import com.androidaccademy.authentication.services.authenticator.AccountManagerProvider
import com.androidaccademy.myapplication.android.App
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

val AUTHORIZATION_HEADER = "AUTHORIZATION"

class AuthInterceptor : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response =
		AccountManagerProvider.getAccessToken(App.instance)?.let { accessToken ->
			chain.request()
				.newBuilder()
				.addHeader(AUTHORIZATION_HEADER, accessToken)
				.build().let { newRequest ->
					chain.proceed(newRequest)
				}
		}
			?: run {
				generateCustomResponse(chain, 400, "missing token. failing call")
			}


	private fun generateCustomResponse(
		chain: Interceptor.Chain,
		code: Int,
		message: String
	): Response {
		return Response.Builder()
			.request(chain.request())
			.protocol(Protocol.HTTP_1_1)
			.code(code)
			.message(message)
			.body(message.toResponseBody(null))
			.build()
	}
}
