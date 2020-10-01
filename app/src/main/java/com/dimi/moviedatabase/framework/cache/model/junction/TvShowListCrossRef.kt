package com.dimi.moviedatabase.framework.cache.model.junction

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.*
import androidx.room.Index
import com.dimi.moviedatabase.framework.cache.model.MediaListEntity
import com.dimi.moviedatabase.framework.cache.model.TvShowCacheEntity

@Entity(
    tableName = "media_tv_show_list",
    primaryKeys = ["media_list_id", "order"],
    indices = [
        Index("media_list_id"),
        Index("order"),
        Index("tv_show_id")
    ],
    foreignKeys = [
        ForeignKey(
            entity = TvShowCacheEntity::class,
            parentColumns = ["id"],
            childColumns = ["tv_show_id"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = MediaListEntity::class,
            parentColumns = ["id"],
            childColumns = ["media_list_id"],
            onDelete = CASCADE
        )
    ]
)
data class TvShowListCrossRef(
    @ColumnInfo(name = "media_list_id")
    val mediaListId: Int,
    @ColumnInfo(name = "tv_show_id")
    val tvShowId: Long,
    @ColumnInfo(name = "order")
    val order: Int
)