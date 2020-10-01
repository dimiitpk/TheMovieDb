package com.dimi.moviedatabase.framework.cache.model.relations

import androidx.room.*
import com.dimi.moviedatabase.framework.cache.model.NetworkCacheEntity
import com.dimi.moviedatabase.framework.cache.model.TvShowCacheEntity
import com.dimi.moviedatabase.framework.cache.model.junction.TvShowNetworkCrossRef

data class TvShowNetworks(

    @Embedded val tvShow: TvShowCacheEntity,
    @Relation(
        parentColumn = "id",
        entity = NetworkCacheEntity::class,
        entityColumn = "id",
        associateBy = Junction(
            value = TvShowNetworkCrossRef::class,
            parentColumn = "tv_show_id",
            entityColumn = "network_id"
        )
    )
    val networks: List<NetworkCacheEntity> = arrayListOf()
)