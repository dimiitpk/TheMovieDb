package com.dimi.moviedatabase.presentation.common.adapters

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dimi.moviedatabase.R
import com.dimi.moviedatabase.business.domain.model.*
import com.dimi.moviedatabase.framework.network.NetworkConstants
import com.dimi.moviedatabase.presentation.GlideApp
import com.dimi.moviedatabase.presentation.common.*
import com.google.android.material.button.MaterialButton


object BindingAdapters {

    @BindingAdapter("android:youtubePlayerState")
    @JvmStatic
    fun View.visibilityByYoutubePlayerState(visibilityState: VisibilityState?) {
        if (visibilityState == VisibilityState.Displayed) this.visible() else this.gone()
    }

    @BindingAdapter("android:textByVisibilityState")
    @JvmStatic
    fun TextView.setTextByVisibilityState(visibilityState: VisibilityState?) {
        text =
            if (visibilityState == VisibilityState.Displayed) context.getString(R.string.hide_trailer) else context.getString(
                R.string.play_trailer
            )
    }

    @BindingAdapter("app:youtubePlayerStateAnimated")
    @JvmStatic
    fun View.animateVisibilityByYoutubePlayerState(visibilityState: VisibilityState?) {
        if (visibilityState == VisibilityState.Displayed) {
            this.fadeIn(
                object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(p0: Animator?) {
                        this@animateVisibilityByYoutubePlayerState.visible()
                    }
                }
            )
        } else {
            this.fadeOut(
                object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(p0: Animator?) {
                        this@animateVisibilityByYoutubePlayerState.gone()
                    }
                })
        }
    }


    @BindingAdapter("app:goneUnless")
    @JvmStatic
    fun View.goneUnless(boolean: Boolean) {
        if (boolean) this.visible() else this.gone()
    }

    @BindingAdapter("app:invisibleUnless")
    @JvmStatic
    fun View.invisibleUnless(boolean: Boolean) {
        if (boolean) this.visible() else this.invisible()
    }

    @BindingAdapter("app:loadSmallCircleImage")
    @JvmStatic
    fun ImageView.loadSmallCircleImageWithGlide(filePath: String?) {

        GlideApp.with(context)
            .load(NetworkConstants.SMALL_IMAGE_URL_PREFIX + filePath)
            .circleCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    }

    @BindingAdapter("app:loadSmallImage")
    @JvmStatic
    fun ImageView.loadSmallImageWithGlide(filePath: String?) {

        GlideApp.with(context)
            .load(NetworkConstants.SMALL_IMAGE_URL_PREFIX + filePath)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    }

    @BindingAdapter("app:loadBigImage")
    @JvmStatic
    fun ImageView.loadBigImageWithGlide(filePath: String?) {

        GlideApp.with(context)
            .load(NetworkConstants.BIG_IMAGE_URL_PREFIX + filePath)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    }

    @BindingAdapter("app:loadOriginalImageList")
    @JvmStatic
    fun ImageView.loadOriginalImageWithGlide(list: List<Image>?) {
        list?.let { images ->
            if (images.isNotEmpty())
                GlideApp.with(context)
                    .load(NetworkConstants.ORIGINAL_IMAGE_URL_PREFIX + images[0].filePath)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(this)
        }
    }

    @BindingAdapter("app:voteAverage")
    @JvmStatic
    fun TextView.setVoteAverage(voteAverage: Float) {
        this.text = this.context.getString(R.string.vote_average_format, (voteAverage * 10).toInt())
    }
}