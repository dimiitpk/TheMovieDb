package com.dimi.moviedatabase.framework.cache.model.relations

import androidx.room.*
import com.dimi.moviedatabase.framework.cache.model.PersonCacheEntity
import com.dimi.moviedatabase.framework.cache.model.TvShowCacheEntity
import com.dimi.moviedatabase.framework.cache.model.junction.TvShowActorsCrossRef

data class TvShowCast(

    @Embedded val tvShow: TvShowCacheEntity,
    @Relation(
        entity = TvShowActorsCrossRef::class,
        entityColumn = "tv_show_id",
        parentColumn = "id"
    )
    var crossRef: List<TvShowActorsCrossRef> = arrayListOf(),
    @Relation(
        parentColumn = "id",
        entity = PersonCacheEntity::class,
        entityColumn = "id",
        associateBy = Junction(
            value = TvShowActorsCrossRef::class,
            parentColumn = "tv_show_id",
            entityColumn = "actor_id"
        )
    )
    val actors: List<PersonCacheEntity> = arrayListOf()
)