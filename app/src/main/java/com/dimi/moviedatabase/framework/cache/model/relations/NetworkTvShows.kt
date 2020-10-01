package com.dimi.moviedatabase.framework.cache.model.relations

import androidx.room.*
import com.dimi.moviedatabase.framework.cache.model.NetworkCacheEntity
import com.dimi.moviedatabase.framework.cache.model.TvShowCacheEntity
import com.dimi.moviedatabase.framework.cache.model.junction.TvShowNetworkCrossRef

data class NetworkTvShows(

    @Embedded val network: NetworkCacheEntity,
    @Relation(
        parentColumn = "id",
        entity = TvShowCacheEntity::class,
        entityColumn = "id",
        associateBy = Junction(
            value = TvShowNetworkCrossRef::class,
            parentColumn = "network_id",
            entityColumn = "tv_show_id"
        )
    )
    val tvShows: List<TvShowCacheEntity> = arrayListOf()
)