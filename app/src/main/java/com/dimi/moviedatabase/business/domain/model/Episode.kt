package com.dimi.moviedatabase.business.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Episode(

    var id: Int,
    var seasonId: Long,
    var tvShowId: Int,
    var seasonNumber: Int,
    var episodeNumber: Int,
    var airDate: Date? = null,
    var overview: String,
    var name: String,
    var voteAverage: Float,
    var voteCount: Int,
    var stillPath: String? = null
) : Parcelable