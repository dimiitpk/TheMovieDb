package com.dimi.moviedatabase.framework.cache.model.junction

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.*
import androidx.room.Index
import com.dimi.moviedatabase.framework.cache.model.PersonCacheEntity
import com.dimi.moviedatabase.framework.cache.model.TvShowCacheEntity

@Entity(
    tableName = "tv_show_actors_cross",
    primaryKeys = ["tv_show_id", "actor_id"],
    indices = [
        Index("tv_show_id"),
        Index("actor_id")
    ],
    foreignKeys = [
        ForeignKey(
            entity = TvShowCacheEntity::class,
            parentColumns = ["id"],
            childColumns = ["tv_show_id"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = PersonCacheEntity::class,
            parentColumns = ["id"],
            childColumns = ["actor_id"],
            onDelete = CASCADE
        )
    ]
)
data class TvShowActorsCrossRef(
    @ColumnInfo(name = "tv_show_id")
    val tvShowId: Long,
    @ColumnInfo(name = "actor_id")
    val actorId: Long,
    @ColumnInfo(name = "character")
    val character: String,
    @ColumnInfo(name = "priority")
    val priority: Int
)