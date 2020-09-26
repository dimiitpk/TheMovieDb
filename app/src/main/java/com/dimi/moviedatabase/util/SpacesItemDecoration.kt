package com.dimi.moviedatabase.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class SpacesItemDecoration(
    space: Int,
    private val onlyBottomAndTopSpaces : Boolean = false
) : RecyclerView.ItemDecoration() {

    private var halfSpace = space / 2

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (parent.paddingLeft != halfSpace) {
            parent.setPadding(halfSpace, halfSpace, halfSpace, halfSpace)
            parent.clipToPadding = false
        }
        outRect.top = halfSpace
        outRect.bottom = halfSpace
        if(!onlyBottomAndTopSpaces) outRect.left = halfSpace
        if(!onlyBottomAndTopSpaces) outRect.right = halfSpace
    }
}