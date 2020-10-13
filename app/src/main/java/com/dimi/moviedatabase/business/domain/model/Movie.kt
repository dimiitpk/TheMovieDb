package com.dimi.moviedatabase.business.domain.model

import android.os.Parcelable
import com.dimi.moviedatabase.business.domain.state.MediaType
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Movie(
    override var id: Long,
    override var title: String,
    override var popularity: Double,
    override var voteCount: Int,
    override var releaseDate: Date? = null,
    override var voteAverage: Float,
    override var overview: String,
    var tagLine: String?,
    override var posterPath: String?,
    override var backdropPath: String?,
    var genres: List<Int>? = null,
    var castList: List<Person>? = null,
    var budget: Int? = null,
    var revenue: Long? = null,
    var status: String? = null,
    var homepage: String? = null,
    var originalTitle: String? = null,
    var runtime: Int? = null,
    var imdbId: String? = null,
    override var character: String? = null,
    var actorId: Int? = null,
    var order: Int? = null
) : Parcelable,
    Media(
        id,
        title,
        popularity,
        voteCount,
        voteAverage,
        overview,
        posterPath,
        backdropPath,
        character,
        releaseDate,
        MediaType.MOVIE
    ) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as Movie

        if (id != other.id) return false
        if (title != other.title) return false
        if (popularity != other.popularity) return false
        if (voteCount != other.voteCount) return false
        if (releaseDate != other.releaseDate) return false
        if (voteAverage != other.voteAverage) return false
        if (overview != other.overview) return false
        if (tagLine != other.tagLine) return false
        if (posterPath != other.posterPath) return false
        if (backdropPath != other.backdropPath) return false
        if (genres != other.genres) return false
        if (castList != other.castList) return false
        if (budget != other.budget) return false
        if (revenue != other.revenue) return false
        if (status != other.status) return false
        if (homepage != other.homepage) return false
        if (originalTitle != other.originalTitle) return false
        if (runtime != other.runtime) return false
        if (imdbId != other.imdbId) return false
        if (character != other.character) return false
        if (actorId != other.actorId) return false
        if (order != other.order) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + popularity.hashCode()
        result = 31 * result + voteCount
        result = 31 * result + (releaseDate?.hashCode() ?: 0)
        result = 31 * result + voteAverage.hashCode()
        result = 31 * result + overview.hashCode()
        result = 31 * result + (tagLine?.hashCode() ?: 0)
        result = 31 * result + (posterPath?.hashCode() ?: 0)
        result = 31 * result + (backdropPath?.hashCode() ?: 0)
        result = 31 * result + (genres?.hashCode() ?: 0)
        result = 31 * result + (castList?.hashCode() ?: 0)
        result = 31 * result + (budget ?: 0)
        result = 31 * result + (revenue?.hashCode() ?: 0)
        result = 31 * result + (status?.hashCode() ?: 0)
        result = 31 * result + (homepage?.hashCode() ?: 0)
        result = 31 * result + (originalTitle?.hashCode() ?: 0)
        result = 31 * result + (runtime ?: 0)
        result = 31 * result + (imdbId?.hashCode() ?: 0)
        result = 31 * result + (character?.hashCode() ?: 0)
        result = 31 * result + (actorId ?: 0)
        result = 31 * result + (order ?: 0)
        return result
    }
}