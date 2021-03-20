package com.androidaccademy.myapplication.ui

class Event<T>(private val value: T) {

	private var isConsumed = false

	fun getContentIfNotHandled(): T? =
		if (isConsumed) {
			null
		} else {
			isConsumed = true
			value
		}
}
