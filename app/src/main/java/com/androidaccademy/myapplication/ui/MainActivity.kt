package com.androidaccademy.myapplication.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.androidaccademy.myapplication.R
import com.androidaccademy.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
	private val viewModel by lazy { ViewModelProvider(this).get(MainActivityViewModel::class.java) }
	lateinit var binding: ActivityMainBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

		binding.apply {
			testBtn.setOnClickListener {
				viewModel.getDataFromMultipleRepos()
			}

			viewModel.networkCallsProgressLiveData.observe(this@MainActivity, {
				resultTxt.text = it.name
			})
		}

		viewModel.networkCallsCompletedLiveEvent.observe(this, {
				if(it.getContentIfNotHandled() == NetworkCallProgress.COMPLETED){
					Toast.makeText(this, "Network call completed", Toast.LENGTH_SHORT).show()
				}
		})
	}
}
