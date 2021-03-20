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
			.addTo(compositeDisposable)
	}

	override fun onCleared() {
		super.onCleared()
		compositeDisposable.clear()
	}
}

enum class NetworkCallProgress {
	IDLE, EXECUTING, COMPLETED
}
