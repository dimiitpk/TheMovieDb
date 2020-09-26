package com.dimi.moviedatabase.framework.cache.model.relations

import androidx.room.*
import com.dimi.moviedatabase.framework.cache.model.PersonCacheEntity
import com.dimi.moviedatabase.framework.cache.model.TvShowCacheEntity
import com.dimi.moviedatabase.framework.cache.model.junction.TvShowActorsCrossRef

data class PersonTvShowCast(

    @Embedded val person: PersonCacheEntity,
    @Relation(
        entity = TvShowActorsCrossRef::class,
        entityColumn = "actor_id",
        parentColumn = "id"
    )
    var crossRef: List<TvShowActorsCrossRef> = arrayListOf(),
    @Relation(
        parentColumn = "id",
        entity = TvShowCacheEntity::class,
        entityColumn = "id",
        associateBy = Junction(
            value = TvShowActorsCrossRef::class,
            parentColumn = "actor_id",
            entityColumn = "tv_show_id"
        )
    )
    val tvShows: List<TvShowCacheEntity> = arrayListOf()
)