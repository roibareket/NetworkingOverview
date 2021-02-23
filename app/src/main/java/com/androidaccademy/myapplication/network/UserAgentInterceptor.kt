package com.androidaccademy.myapplication.network

import okhttp3.Interceptor
import okhttp3.Response

class UserAgentInterceptor : Interceptor {

	override fun intercept(chain: Interceptor.Chain): Response {
		val request = chain.request()
			.newBuilder()
			.addHeader("User-Agent", "Android")
			.build()

		val response = chain.proceed(request)

		return response
	}
}


