package com.dimi.moviedatabase.business.domain.model

import android.os.Parcelable
import com.dimi.moviedatabase.business.domain.state.MediaType
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class TvShow(
    override var id: Long,
    override var title: String,
    override var popularity: Double,
    override var voteCount: Int,
    var firstAirDate: Date? = null,
    var lastAirDate: Date? = null,
    override var voteAverage: Float,
    override var overview: String,
    var numberOfEpisodes: Int? = null,
    var numberOfSeasons: Int? = null,
    override var posterPath: String?,
    override var backdropPath: String?,
    var genres: List<Int>? = null,
    var castList: List<Person>? = null,
    var seasons: List<Season>? = null,
    var networks: List<Network>? = null,
    val type: String? = null,
    var runtime: Int? = null,
    var status: String? = null,
    var originalTitle: String? = null,
    var homepage: String? = null,
    override var character: String? = null,
    var order: Int? = null
) : Parcelable, Media(
    id,
    title,
    popularity,
    voteCount,
    voteAverage,
    overview,
    posterPath,
    backdropPath,
    character,
    firstAirDate,
    MediaType.TV_SHOW
)