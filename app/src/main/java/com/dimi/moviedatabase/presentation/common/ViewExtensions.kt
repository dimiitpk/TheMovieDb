package com.dimi.moviedatabase.presentation.common

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun TextView.setTextAndCompoundDrawablesColor(@ColorInt colorId: Int) {
    setTextColor(
        colorId
    )
    for (drawable in compoundDrawables)
        drawable?.colorFilter = PorterDuffColorFilter(colorId, PorterDuff.Mode.MULTIPLY)

}

fun View.fadeIn(listener: Animator.AnimatorListener? = null) {
    val animationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
    apply {
        visible()
        alpha = 0f
        animate()
            .alpha(1f)
            .setDuration(animationDuration.toLong())
            .setListener(listener)
    }
}

fun View.fadeOut(listener: Animator.AnimatorListener? = null) {
    val animationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
    apply {
        animate()
            .alpha(0f)
            .setDuration(animationDuration.toLong())
            .setListener(
                listener ?: object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        invisible()
                    }
                })
    }
}












