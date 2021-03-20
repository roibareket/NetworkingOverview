package com.androidaccademy.myapplication.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class NetworkManager {

	val gitHubService: GitHubService

	init {
		gitHubService = initGithubApiService()
	}

	private fun initGithubApiService(): GitHubService =
		Retrofit.Builder()
			.baseUrl(GitHubService.BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
			.client(
				OkHttpClient.Builder().apply {
					addInterceptor(newLoggingInterceptor())
					addInterceptor(NetworkDelayInterceptor())
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
