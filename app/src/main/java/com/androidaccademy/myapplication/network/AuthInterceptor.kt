package com.androidaccademy.myapplication.network

import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

val AUTHORIZATION_HEADER = "AUTHORIZATION_DUMMY" // "AUTHORIZATION"

class AuthInterceptor : Interceptor {
	val accountManagerProvider = AccountManagerProvider.instance

	override fun intercept(chain: Interceptor.Chain): Response =
		accountManagerProvider.getAuthToken()?.let { accessToken ->
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
