package com.androidaccademy.myapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.androidaccademy.authentication.ui.login.LoginActivity
import com.androidaccademy.myapplication.R
import com.androidaccademy.myapplication.databinding.ActivityMainBinding
import com.androidaccademy.myapplication.network.GitHubService
import com.androidaccademy.myapplication.network.NetwrokManager
import com.androidaccademy.myapplication.network.Repo
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
				executeGithubCall(NetwrokManager().gitHubServicePublic)
			}
			executePrivateBtn.setOnClickListener {
				executeGithubCall(NetwrokManager().gitHubServicePrivate)
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
}
