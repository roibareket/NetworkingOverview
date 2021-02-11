package com.androidaccademy.authentication.services.authenticator

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import com.androidaccademy.authentication.R

class AccountManagerProvider {

	companion object {
		fun getCurrentAccount(context: Context) =
			AccountManager.get(context)
				.getAccountsByType(context.getString(R.string.accountType))
				.getOrNull(0)

		fun addAccount(account: Account, password: String, context: Context) =
			AccountManager.get(context).addAccountExplicitly(account, password, null)

		fun setAuthToken(account: Account, tokenType: String, token: String, context: Context) =
			AccountManager.get(context).setAuthToken(account, tokenType, token)

		fun getAccessToken(context: Context) =
			getCurrentAccount(context)?.let { account ->
				AccountManager.get(context)
					.getAuthToken(account, ACCESS_TOKEN, null, null, null, null)
					.getResult()
					.getString(AccountManager.KEY_AUTHTOKEN)
			}
	}
}
