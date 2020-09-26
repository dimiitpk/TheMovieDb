package com.dimi.moviedatabase.presentation.main.view.fragments

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.dimi.moviedatabase.R
import com.dimi.moviedatabase.presentation.GlideApp
import com.dimi.moviedatabase.presentation.common.BaseDialogFragment
import com.dimi.moviedatabase.presentation.main.view.viewmodel.ViewMediaViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseViewMediaFragment(
    @LayoutRes layoutId: Int,
    private val customRequestManager: Boolean = false
) : BaseDialogFragment(layoutId) {

    val viewModel: ViewMediaViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    lateinit var requestManager: RequestManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (!customRequestManager)
            requestManager = GlideApp.with(this)
    }
}