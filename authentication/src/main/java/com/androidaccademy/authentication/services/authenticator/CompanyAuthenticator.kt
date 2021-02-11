package com.androidaccademy.authentication.services.authenticator

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.os.Bundle
import com.androidaccademy.authentication.network.AuthApi
import com.androidaccademy.authentication.ui.login.LoginActivity

const val ACCESS_TOKEN = "ACCESS_TOKEN"
const val REFRESH_TOKEN = "REFRESH_TOKEN"
class CompanyAuthenticator(val context: Context) : AbstractAccountAuthenticator(context) {

	override fun addAccount(
		response: AccountAuthenticatorResponse?,
		accountType: String?,
		authTokenType: String?,
		requiredFeatures: Array<out String>?,
		options: Bundle?
	) =
		Bundle().apply {
			putParcelable(
				AccountManager.KEY_INTENT,
				LoginActivity.newIntentAddNewAccount(
					context,
					accountType
				)
			)
		}

	override fun getAuthToken(
		response: AccountAuthenticatorResponse,
		account: Account,
		authTokenType: String,
		options: Bundle?
	): Bundle =
		// when we have account we have token. not mandatory but simplify a lot :)
		AccountManager.get(context).let { accountManager ->
			Bundle().apply {
				putString(AccountManager.KEY_ACCOUNT_NAME, account.name)
				putString(AccountManager.KEY_ACCOUNT_TYPE, account.type)
				putString(
					AccountManager.KEY_AUTHTOKEN,
					accountManager.peekAuthToken(account, authTokenType)
				)
			}
		}

	override fun getAuthTokenLabel(authTokenType: String): String = "App Token"

	override fun hasFeatures(
		response: AccountAuthenticatorResponse?,
		account: Account?,
		features: Array<out String>?
	): Bundle =
		Bundle().apply {
			putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
		}

	override fun confirmCredentials(
		response: AccountAuthenticatorResponse?,
		account: Account?,
		options: Bundle?
	): Bundle? = null

	override fun updateCredentials(
		response: AccountAuthenticatorResponse?,
		account: Account?,
		authTokenType: String?,
		options: Bundle?
	): Bundle? = null

	override fun editProperties(
		response: AccountAuthenticatorResponse?,
		accountType: String?
	): Bundle? = null
}
