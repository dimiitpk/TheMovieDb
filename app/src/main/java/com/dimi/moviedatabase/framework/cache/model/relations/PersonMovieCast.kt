package com.dimi.moviedatabase.framework.cache.model.relations

import androidx.room.*
import com.dimi.moviedatabase.framework.cache.model.MovieCacheEntity
import com.dimi.moviedatabase.framework.cache.model.PersonCacheEntity
import com.dimi.moviedatabase.framework.cache.model.junction.MovieActorsCrossRef

data class PersonMovieCast(

    @Embedded val person: PersonCacheEntity,
    @Relation(
        entity = MovieActorsCrossRef::class,
        entityColumn = "actor_id",
        parentColumn = "id"
    )
    var crossRef: List<MovieActorsCrossRef> = arrayListOf(),
    @Relation(
        parentColumn = "id",
        entity = MovieCacheEntity::class,
        entityColumn = "id",
        associateBy = Junction(
            value = MovieActorsCrossRef::class,
            parentColumn = "actor_id",
            entityColumn = "movie_id"
        )
    )
    val movies: List<MovieCacheEntity> = arrayListOf()
)