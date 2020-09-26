package com.dimi.moviedatabase.framework.cache.model.junction

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.*
import androidx.room.Index
import com.dimi.moviedatabase.framework.cache.model.NetworkCacheEntity
import com.dimi.moviedatabase.framework.cache.model.PersonCacheEntity
import com.dimi.moviedatabase.framework.cache.model.TvShowCacheEntity

@Entity(
    tableName = "tv_show_network_cross",
    primaryKeys = ["tv_show_id", "network_id"],
    indices = [
        Index("tv_show_id"),
        Index("network_id")
    ],
    foreignKeys = [
        ForeignKey(
            entity = TvShowCacheEntity::class,
            parentColumns = ["id"],
            childColumns = ["tv_show_id"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = NetworkCacheEntity::class,
            parentColumns = ["id"],
            childColumns = ["network_id"],
            onDelete = CASCADE
        )
    ]
)
data class TvShowNetworkCrossRef(
    @ColumnInfo(name = "tv_show_id")
    val tvShowId: Long,
    @ColumnInfo(name = "network_id")
    val networkId: Long
)