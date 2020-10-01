package com.dimi.moviedatabase.presentation

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.dimi.moviedatabase.R

@GlideModule
    class MyGlideModule : AppGlideModule() {

        override fun applyOptions(context: Context, builder: GlideBuilder) {
            super.applyOptions(context, builder)
            builder.setDefaultRequestOptions(
                RequestOptions()
                    .placeholder(R.drawable.ic_image_place_holder)
                    .fallback(R.drawable.ic_no_image)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .transform(RoundedCorners(25))
                    .error(R.drawable.ic_broken_image)
            )
        }
}