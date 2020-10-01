package com.dimi.moviedatabase.presentation.common

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import com.dimi.moviedatabase.R
import com.dimi.moviedatabase.databinding.InfoDetailsTableRowBinding
import com.google.android.material.button.MaterialButton

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun ViewGroup.createButton(): MaterialButton {

    val layoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )
    layoutParams.setMargins(10, 0, 0, 0)

    val inflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    val button = inflater.inflate(
        R.layout.layout_genre_button,
        this,
        false
    ) as MaterialButton

    this.addView(button, layoutParams)

    return button
}

fun Layout?.isEllipsized(): Boolean {
    this?.let {
        val lines: Int = it.lineCount
        return if (lines > 0)
            it.getEllipsisCount(lines - 1) > 0
        else false
    } ?: return false
}

fun TableLayout.createDetailInfoLine(key: String, value: String): View {
    val inflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    return InfoDetailsTableRowBinding.inflate(inflater, this, false).also {
        it.infoLineKey.text = key
        it.infoLineValue.text = value
    }.root
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












