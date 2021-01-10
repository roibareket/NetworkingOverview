package com.androidaccademy.myapplication.network

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Repo(
	val id: Int,
	@SerializedName("name")
	val name: String)
