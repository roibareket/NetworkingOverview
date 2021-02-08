package com.androidaccademy.myapplication.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetwrokManager {

	val gitHubServicePublic: GitHubService
	val gitHubServicePrivate: GitHubService

	init {
		gitHubServicePublic = initGithubApiService(isAuthNeeded = false)
		gitHubServicePrivate = initGithubApiService(isAuthNeeded = true)
	}

	private fun initGithubApiService(isAuthNeeded: Boolean): GitHubService =
		Retrofit.Builder()
			.baseUrl(GitHubService.BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())

			.client(
				OkHttpClient.Builder().apply {
					if(isAuthNeeded) {
						addInterceptor(AuthInterceptor())
					}
					// Exercise 1
					// make any request inject header: "USER-AGENT": "Android"
					// addInterceptor(UserAgentInterceptor())
					addInterceptor(newLoggingInterceptor())
					authenticator(MyAuthenticator())
				}
					.build()
			)
			.build()
			.create(GitHubService::class.java)

	private fun newLoggingInterceptor() =
		HttpLoggingInterceptor().apply {
			level = HttpLoggingInterceptor.Level.HEADERS
		}
}
