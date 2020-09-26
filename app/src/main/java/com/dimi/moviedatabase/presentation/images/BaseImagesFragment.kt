package com.dimi.moviedatabase.presentation.images

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
abstract class BaseImagesFragment(
    @LayoutRes fragmentResId: Int
) : Fragment(fragmentResId) {

    lateinit var uiController: UIController
    lateinit var permissions: Permissions

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.let {
            if (it is FullScreenImageActivity) {
                try {
                    uiController = context as UIController
                } catch (e: ClassCastException) {
                    e.printStackTrace()
                }
                try {
                    permissions = context as Permissions
                } catch (e: ClassCastException) {
                    e.printStackTrace()
                }
            }
        }
    }
}