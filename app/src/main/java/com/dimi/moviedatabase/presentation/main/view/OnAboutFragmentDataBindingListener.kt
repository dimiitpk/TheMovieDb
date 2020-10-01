package com.dimi.moviedatabase.presentation.main.view

import com.dimi.moviedatabase.presentation.common.OnDataBindingClickListener
import com.dimi.moviedatabase.presentation.main.search.enums.ViewType

interface OnAboutFragmentDataBindingListener : OnDataBindingClickListener {

    fun onClickViewType(viewType: ViewType, value: String)
}