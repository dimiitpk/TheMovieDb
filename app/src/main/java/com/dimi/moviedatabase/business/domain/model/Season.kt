package com.dimi.moviedatabase.business.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Season(
    var id: Long,
    var airDate: Date? = null,
    var episodeCount: Int,
    var seasonName: String,
    var overview: String? = null,
    var posterPath: String? = null,
    var seasonNumber: Int,
    var tvShowId: Long
) : Parcelable