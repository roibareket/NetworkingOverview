package com.androidaccademy.myapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.androidaccademy.authentication.ui.login.LoginActivity
import com.androidaccademy.myapplication.R
import com.androidaccademy.myapplication.databinding.ActivityMainBinding
import com.androidaccademy.myapplication.network.*
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
	lateinit var binding: ActivityMainBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

		binding.apply {
			executePublicBtn.setOnClickListener {
				executeGithubCall(NetworkManager().gitHubServicePublic)
			}
			executePrivateBtn.setOnClickListener {
				executeGithubCall(NetworkManager().gitHubServicePrivate)
			}
			testMyAuthenticator.setOnClickListener {
				testMyAuthenticator()
			}
			login.setOnClickListener {
				startActivity(
					LoginActivity.newIntentAddNewAccount(
						this@MainActivity,
						getString(R.string.accountType)
					)
				)
			}
		}

	}

	// everything below should be in view model

	private fun executeGithubCall(githubApi: GitHubService) {
		binding.resultTxt.text = ""
		githubApi.listRepos("roibareket").enqueue(object :
			Callback<List<Repo>> {
			override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
				binding.resultTxt.text =
					if (response.isSuccessful) {
						response.body().orEmpty().toString()
					} else {
						response.message()
					}
			}

			override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
				binding.resultTxt.text = t.toString()
			}
		})
	}

	private fun testMyAuthenticator() {
		Thread {
			okhttp3.Response.Builder()
				.request(
					Request.Builder()
						.addHeader(AUTHORIZATION_HEADER, "my_access_token")
						.url("https://github.com")
						.build()
				)
				.protocol(Protocol.HTTP_1_1)
				.code(401)
				.message("test 401")
				.body("test 401".toResponseBody(null))
				.build().let { response ->
					MyAuthenticator().authenticate(null, response)
				}
		}.start()
	}
}
