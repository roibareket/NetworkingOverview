package com.androidaccademy.myapplication.android

import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree


class App : Application() {

	override fun onCreate() {
		super.onCreate()
		initTimber()
		instance = this
	}

	private fun initTimber() = Timber.plant(DebugTree())

	companion object{
		lateinit var instance: App
	}
}
