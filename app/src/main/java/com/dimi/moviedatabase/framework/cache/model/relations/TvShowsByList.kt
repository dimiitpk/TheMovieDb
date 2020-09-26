package com.dimi.moviedatabase.framework.cache.model.relations

import androidx.room.*
import com.dimi.moviedatabase.framework.cache.model.MediaListEntity
import com.dimi.moviedatabase.framework.cache.model.TvShowCacheEntity
import com.dimi.moviedatabase.framework.cache.model.junction.TvShowListCrossRef

data class TvShowsByList(

    @Embedded val mediaList: MediaListEntity,
    @Relation(
        entity = TvShowListCrossRef::class,
        entityColumn = "media_list_id",
        parentColumn = "id"
    )
    var crossRef: List<TvShowListCrossRef> = arrayListOf(),
    @Relation(
        parentColumn = "id",
        entity = TvShowCacheEntity::class,
        entityColumn = "id",
        associateBy = Junction(
            value = TvShowListCrossRef::class,
            parentColumn = "media_list_id",
            entityColumn = "tv_show_id"
        )
    )
    val tvShows: List<TvShowCacheEntity> = arrayListOf()
)