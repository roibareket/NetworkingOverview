package com.androidaccademy.myapplication.network

import okhttp3.Interceptor
import okhttp3.Response
import java.lang.Thread.sleep

class NetworkDelayInterceptor: Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {
		sleep(500)
		return chain.proceed(chain.request())
	}
}
