package com.dimi.moviedatabase.framework.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "movie")
data class MovieCacheEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Long,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "popularity")
    var popularity: Double,

    @ColumnInfo(name = "vote_count")
    var voteCount: Int,

    @ColumnInfo(name = "release_date")
    var releaseDate: Date? = null,

    @ColumnInfo(name = "vote_average")
    var voteAverage: Float,

    @ColumnInfo(name = "overview")
    var overview: String,

    @ColumnInfo(name = "tag_line")
    var tagLine: String,

    @ColumnInfo(name = "poster_path")
    var posterPath: String?,

    @ColumnInfo(name = "genre_ids")
    var genres: String? = null,

    @ColumnInfo(name = "backdrop_path")
    var backdropPath: String?,

    @ColumnInfo(name = "budget")
    var budget: Int? = null,

    @ColumnInfo(name = "revenue")
    var revenue: Long? = null,

    @ColumnInfo(name = "status")
    var status: String? = null,

    @ColumnInfo(name = "homepage")
    var homepage: String? = null,

    @ColumnInfo(name = "original_title")
    var originalTitle: String? = null,

    @ColumnInfo(name = "runtime")
    var runtime: Int? = null,

    @ColumnInfo(name = "imdb_id")
    var imdbId: String? = null
)



