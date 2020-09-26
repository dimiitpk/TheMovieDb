package com.dimi.moviedatabase.framework.cache.model.junction

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.*
import androidx.room.Index
import com.dimi.moviedatabase.framework.cache.model.MovieCacheEntity
import com.dimi.moviedatabase.framework.cache.model.PersonCacheEntity

@Entity(
    tableName = "movie_actors_cross",
    primaryKeys = ["movie_id", "actor_id"],
    indices = [
        Index("movie_id"),
        Index("actor_id")
    ],
    foreignKeys = [
        ForeignKey(
            entity = MovieCacheEntity::class,
            parentColumns = ["id"],
            childColumns = ["movie_id"],
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
data class MovieActorsCrossRef(
    @ColumnInfo(name = "movie_id")
    val movieId: Long,
    @ColumnInfo(name = "actor_id")
    val actorId: Long,
    @ColumnInfo(name = "character")
    val character: String,
    @ColumnInfo(name = "priority")
    val priority: Int
)