package com.dimi.moviedatabase.framework.cache.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.dimi.moviedatabase.business.domain.model.Movie
import java.util.*

@Entity(
    tableName = "season",
    foreignKeys = [
        ForeignKey(
            entity = TvShowCacheEntity::class,
            parentColumns = ["id"],
            childColumns = ["tvShowId"],
            onDelete = CASCADE
        )
    ]
)
data class SeasonCacheEntity (

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Long,

    @ColumnInfo(name = "air_date")
    var airDate: Date? = null,

    @ColumnInfo(name = "episode_count")
    var episodeCount: Int,

    @ColumnInfo(name = "name")
    var seasonName: String,

    @ColumnInfo(name = "overview")
    var overview: String? = null,

    @ColumnInfo(name = "poster_path")
    var posterPath: String? = null,

    @ColumnInfo(name = "season_number")
    var seasonNumber: Int,

    @ColumnInfo(name = "tvShowId", index = true)
    var tvShowId: Long

) {


}