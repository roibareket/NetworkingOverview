package com.androidaccademy.myapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.androidaccademy.myapplication.network.NetworkManager
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class MainActivityViewModel : ViewModel() {
	private val networkManager = NetworkManager()

	val networkCallsProgressLiveData = MutableLiveData(NetworkCallProgress.IDLE)
	val networkCallsCompletedLiveEvent = networkCallsProgressLiveData
		.map { Event(it) }

	fun getDataFromMultipleRepos() {
		Observables.combineLatest(
			networkManager.gitHubService.listRepos("roibareket"),
			networkManager.gitHubService.listRepos("parahall")
		)
			.doOnSubscribe { networkCallsProgressLiveData.postValue(NetworkCallProgress.EXECUTING) }
			.doOnNext { networkCallsProgressLiveData.postValue(NetworkCallProgress.COMPLETED) }
			.subscribeOn(Schedulers.io())
			.map { (list1, list2) -> list1.plus(list2) }
			.subscribe({
				Timber.i(it.toString())
			}, {
				Timber.i(it, "network failed")
			})
	}

	fun getDataFromMultipleReposInitial() {
		networkManager.gitHubService.listRepos("roibareket")
			.subscribeOn(Schedulers.io())
			.subscribe({
				Timber.i(it.toString())
			}, {
				Timber.i(it, "network failed")
			})

		networkManager.gitHubService.listRepos("parahall")
			.subscribeOn(Schedulers.io())
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
