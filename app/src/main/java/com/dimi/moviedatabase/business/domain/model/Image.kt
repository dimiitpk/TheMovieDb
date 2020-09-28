package com.dimi.moviedatabase.business.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Image(
    var aspectRatio : Float,
    var filePath: String,
    var height: Int,
    var isPoster: Boolean = false
) : Parcelable