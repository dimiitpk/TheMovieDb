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
    override var genres: List<Int>? = null,
    override var castList: List<Person>? = null,
    var seasons: List<Season>? = null,
    var networks: List<Network>? = null,
    val type: String? = null,
    override var runtime: Int? = null,
    override var status: String? = null,
    override var originalTitle: String? = null,
    override var homepage: String? = null,
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
    genres,
    castList,
    runtime,
    status,
    originalTitle,
    homepage,
    character,
    firstAirDate,
    MediaType.TV_SHOW
) {

    override fun toString(): String {
        return "TvShow(id=$id, title='$title', popularity=$popularity, voteCount=$voteCount, firstAirDate=$firstAirDate, lastAirDate=$lastAirDate, voteAverage=$voteAverage, overview='$overview', numberOfEpisodes=$numberOfEpisodes, numberOfSeasons=$numberOfSeasons, posterPath=$posterPath, backdropPath=$backdropPath, genres=$genres, castList=$castList, seasons=$seasons, networks=$networks, type=$type, runtime=$runtime, status=$status, originalTitle=$originalTitle, homepage=$homepage, character=$character)"
    }
}