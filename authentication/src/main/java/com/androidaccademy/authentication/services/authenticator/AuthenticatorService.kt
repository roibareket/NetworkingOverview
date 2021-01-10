package com.androidaccademy.authentication.services.authenticator

import android.app.Service
import android.content.Intent
import android.os.IBinder

class AuthenticatorService : Service() {

	val authenticator = CompanyAuthenticator(this)

	override fun onBind(intent: Intent?): IBinder? = authenticator.iBinder
}
