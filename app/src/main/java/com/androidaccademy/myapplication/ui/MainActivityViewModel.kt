package com.androidaccademy.myapplication.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.androidaccademy.myapplication.network.NetworkManager
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class MainActivityViewModel : ViewModel() {
	private val networkManager = NetworkManager()

	val compositeDisposable = CompositeDisposable()
	val networkCallsProgressLiveData = MutableLiveData(NetworkCallProgress.IDLE)
	val networkCallsCompletedLiveEvent = networkCallsProgressLiveData


	fun getDataFromMultipleRepos() {
		//  networkManager.gitHubService.listRepos("parahall")
			networkManager.gitHubService.listRepos("roibareket")
			.subscribe({
				Timber.i(it.toString())
			}, {
				Timber.i(it, "network failed")
			})
	}
}

enum class NetworkCallProgress {
	IDLE, EXECUTING, COMPLETED
}
