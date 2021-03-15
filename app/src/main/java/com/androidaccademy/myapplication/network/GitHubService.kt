package com.androidaccademy.myapplication.network

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface GitHubService {
	companion object {
		val BASE_URL = "https://api.github.com/"
	}

	@GET("users/{user}/repos")
	fun listRepos(@Path("user") user: String): Call<List<Repo>>

}
