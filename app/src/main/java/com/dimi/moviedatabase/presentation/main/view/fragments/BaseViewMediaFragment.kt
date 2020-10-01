package com.dimi.moviedatabase.presentation.main.view.fragments

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import com.bumptech.glide.RequestManager
import com.dimi.moviedatabase.presentation.GlideApp
import com.dimi.moviedatabase.presentation.common.BaseDBDialogFragment
import com.dimi.moviedatabase.presentation.main.view.viewmodel.ViewMediaViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseViewMediaFragment<T : ViewDataBinding>(
    @LayoutRes layoutId: Int,
    private val customRequestManager: Boolean = false
) : BaseDBDialogFragment<T>(layoutId) {

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