package com.dimi.moviedatabase.framework.cache.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import java.util.*

@Entity(
    tableName = "episode",
    foreignKeys = [
        ForeignKey(
            entity = SeasonCacheEntity::class,
            parentColumns = ["id"],
            childColumns = ["season_id"],
            onDelete = CASCADE
        )
    ]
)
data class EpisodeCacheEntity (

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "show_id")
    var tvShowId: Int,

    @ColumnInfo(name = "season_id", index = true)
    var seasonId: Long,

    @ColumnInfo(name = "season_number")
    var seasonNumber: Int,

    @ColumnInfo(name = "episode_number")
    var episodeNumber: Int,

    @ColumnInfo(name = "air_date")
    var airDate: Date? = null,

    @ColumnInfo(name = "overview")
    var overview: String,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "vote_average")
    var voteAverage: Float,

    @ColumnInfo(name = "vote_count")
    var voteCount: Int,

    @ColumnInfo(name = "still_path")
    var stillPath: String? = null

)  {
    override fun toString(): String {
        return "EpisodeCacheEntity(id=$id, tvShowId=$tvShowId, seasonId=$seasonId, seasonNumber=$seasonNumber, episodeNumber=$episodeNumber, airDate='$airDate', overview='$overview', name='$name', voteAverage='$voteAverage', voteCount='$voteCount', stillPath=$stillPath)"
    }
}