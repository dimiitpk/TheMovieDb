package com.dimi.moviedatabase.presentation.main

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.dimi.moviedatabase.presentation.common.UIController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.lang.ClassCastException

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
abstract class BaseMainFragment(
    @LayoutRes fragmentResId: Int
) : Fragment(fragmentResId) {

    lateinit var uiController: UIController

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.let {
            if (it is MainActivity) {
                try {
                    uiController = context as UIController
                } catch (e: ClassCastException) {
                    e.printStackTrace()
                }
            }
        }
    }
}