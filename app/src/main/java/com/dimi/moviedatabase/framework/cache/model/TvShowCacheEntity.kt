package com.dimi.moviedatabase.framework.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "tv_show")
data class TvShowCacheEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Long,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "popularity")
    var popularity: Double,

    @ColumnInfo(name = "vote_count")
    var voteCount: Int,

    @ColumnInfo(name = "number_of_episodes")
    var numberOfEpisodes: Int? = null,

    @ColumnInfo(name = "number_of_seasons")
    var numberOfSeasons: Int? = null,

    @ColumnInfo(name = "first_air_date")
    var firstAirDate: Date? = null,

    @ColumnInfo(name = "last_air_date")
    var lastAirDate: Date? = null,

    @ColumnInfo(name = "vote_average")
    var voteAverage: Float,

    @ColumnInfo(name = "overview")
    var overview: String,

    @ColumnInfo(name = "poster_path")
    var posterPath: String?,

    @ColumnInfo(name = "genre_ids")
    var genres: String? = null,

    @ColumnInfo(name = "backdrop_path")
    var backdropPath: String?,

    @ColumnInfo(name = "type")
    val type: String? = null,

    @ColumnInfo(name = "runtime")
    var runtime: Int? = null,

    @ColumnInfo(name = "status")
    var status: String? = null,

    @ColumnInfo(name = "original_title")
    var originalTitle: String? = null,

    @ColumnInfo(name = "homepage")
    var homepage: String? = null

)



