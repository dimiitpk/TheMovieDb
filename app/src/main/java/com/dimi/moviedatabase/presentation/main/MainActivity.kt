package com.dimi.moviedatabase.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.dimi.moviedatabase.R
import com.dimi.moviedatabase.databinding.ActivityMainBinding
import com.dimi.moviedatabase.presentation.common.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        findNavController(R.id.main_fragment_container).handleDeepLink(intent)
    }

    override fun displayProgressBar(isDisplayed: Boolean) {
        if(isDisplayed)
            binding.progressBar.visible()
        else
            binding.progressBar.gone()
    }
}