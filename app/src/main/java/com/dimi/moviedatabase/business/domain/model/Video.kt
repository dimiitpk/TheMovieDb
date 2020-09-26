package com.dimi.moviedatabase.business.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Video(

    var id: String,
    var movieId: Int,
    var key: String,
    var name: String,
    var site: String,
    var type: String
) : Parcelable
