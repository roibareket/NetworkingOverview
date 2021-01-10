package com.androidaccademy.authentication.network

import java.lang.Thread.sleep

class AuthApi private constructor() {

	fun signUp(name: String, userName: String, password: String) = "token from server"
		.also {
			sleep(1000)
		}

	fun signIn(userName: String, password: String) = "token from server"
		.also {
			sleep(1000)
		}

	companion object {
		val instance = AuthApi()
	}
}
