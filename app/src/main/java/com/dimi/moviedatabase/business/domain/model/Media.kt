package com.dimi.moviedatabase.business.domain.model

import android.os.Parcelable
import com.dimi.moviedatabase.business.domain.state.MediaType
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
open class Media(
    open var id: Long,
    open var title: String,
    open var popularity: Double,
    open var voteCount: Int,
    open var voteAverage: Float,
    open var overview: String,
    open var posterPath: String?,
    open var backdropPath: String?,
    open var genres: List<Int>? = null,
    open var castList: List<Person>? = null,
    open var runtime: Int? = null,
    open var status: String? = null,
    open var originalTitle: String? = null,
    open var homepage: String? = null,
    open var character: String? = null,
    open var releaseDate: Date? = null,
    var mediaType: MediaType
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Media

        if (id != other.id) return false
        if (title != other.title) return false
        if (popularity != other.popularity) return false
        if (voteCount != other.voteCount) return false
        if (voteAverage != other.voteAverage) return false
        if (overview != other.overview) return false
        if (posterPath != other.posterPath) return false
        if (backdropPath != other.backdropPath) return false
        if (genres != other.genres) return false
        if (castList != other.castList) return false

        return true
    }

    override fun hashCode(): Int {
        var result : Int = id.toInt()
        result = 31 * result + title.hashCode()
        result = 31 * result + popularity.hashCode()
        result = 31 * result + voteCount
        result = 31 * result + voteAverage.hashCode()
        result = 31 * result + overview.hashCode()
        result = 31 * result + (posterPath?.hashCode() ?: 0)
        result = 31 * result + (backdropPath?.hashCode() ?: 0)
        result = 31 * result + (genres?.hashCode() ?: 0)
        result = 31 * result + (castList?.hashCode() ?: 0)

        return result
    }
}