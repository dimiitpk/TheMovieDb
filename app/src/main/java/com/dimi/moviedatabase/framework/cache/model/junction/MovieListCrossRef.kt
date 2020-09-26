package com.dimi.moviedatabase.framework.cache.model.junction

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.*
import androidx.room.Index
import com.dimi.moviedatabase.framework.cache.model.MediaListEntity
import com.dimi.moviedatabase.framework.cache.model.MovieCacheEntity
import com.dimi.moviedatabase.framework.cache.model.PersonCacheEntity

@Entity(
    tableName = "media_movie_list",
    primaryKeys = ["media_list_id", "order"],
    indices = [
        Index("media_list_id"),
        Index("order"),
        Index("movie_id")
    ],
    foreignKeys = [
        ForeignKey(
            entity = MovieCacheEntity::class,
            parentColumns = ["id"],
            childColumns = ["movie_id"],
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
data class MovieListCrossRef(
    @ColumnInfo(name = "media_list_id")
    val mediaListId: Int,
    @ColumnInfo(name = "movie_id")
    val movieId: Long,
    @ColumnInfo(name = "order")
    val order: Int
)