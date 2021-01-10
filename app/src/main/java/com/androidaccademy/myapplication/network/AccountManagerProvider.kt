package com.androidaccademy.myapplication.network

import android.accounts.AccountManager
import com.androidaccademy.authentication.services.authenticator.AUTH_TOKEN_TYPE
import com.androidaccademy.myapplication.R
import com.androidaccademy.myapplication.android.App

class AccountManagerProvider private constructor() {

	val accountManager = AccountManager.get(App.instance)

	fun getAuthToken() =
		accountManager.getAccountsByType(App.instance.getString(R.string.accountType)).getOrNull(0)?.let { account ->
			accountManager.getAuthToken(account, AUTH_TOKEN_TYPE, null, null, null, null)
				.getResult()
				.getString(AccountManager.KEY_AUTHTOKEN)
		}

	companion object {
		val instance = AccountManagerProvider()
	}
}
