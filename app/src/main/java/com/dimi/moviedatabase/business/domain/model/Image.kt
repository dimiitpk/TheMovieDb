package com.dimi.moviedatabase.business.domain.model

import android.net.Uri
import android.os.Parcelable
import com.dimi.moviedatabase.framework.network.NetworkConstants
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Image(
    var aspectRatio : Float,
    var filePath: String,
    var height: Int,
    var isPoster: Boolean = false
) : Parcelable