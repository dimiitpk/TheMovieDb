package com.dimi.moviedatabase.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.navigation.findNavController
import com.dimi.moviedatabase.R
import com.dimi.moviedatabase.presentation.common.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        findNavController(R.id.main_fragment_container).handleDeepLink(intent)
    }

    override fun displayProgressBar(isDisplayed: Boolean) {
        if(isDisplayed)
            progress_bar.visible()
        else
            progress_bar.gone()
    }
}