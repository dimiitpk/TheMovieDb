package com.dimi.moviedatabase

import androidx.appcompat.app.AppCompatActivity
import com.dimi.moviedatabase.business.domain.state.Response
import com.dimi.moviedatabase.business.domain.state.StateMessageCallback
import com.dimi.moviedatabase.presentation.common.UIController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HiltTestActivity : AppCompatActivity(), UIController {
    override fun displayProgressBar(isDisplayed: Boolean) {

    }

    override fun hideSoftKeyboard() {
    }

    override fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    ) {
    }
}