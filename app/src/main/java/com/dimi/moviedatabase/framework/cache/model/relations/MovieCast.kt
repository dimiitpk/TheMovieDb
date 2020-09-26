package com.dimi.moviedatabase.framework.cache.model.relations

import androidx.room.*
import com.dimi.moviedatabase.framework.cache.model.MovieCacheEntity
import com.dimi.moviedatabase.framework.cache.model.PersonCacheEntity
import com.dimi.moviedatabase.framework.cache.model.junction.MovieActorsCrossRef

data class MovieCast(

    @Embedded val movie: MovieCacheEntity,
    @Relation(
        entity = MovieActorsCrossRef::class,
        entityColumn = "movie_id",
        parentColumn = "id"
    )
    var crossRef: List<MovieActorsCrossRef> = arrayListOf(),
    @Relation(
        parentColumn = "id",
        entity = PersonCacheEntity::class,
        entityColumn = "id",
        associateBy = Junction(
            value = MovieActorsCrossRef::class,
            parentColumn = "movie_id",
            entityColumn = "actor_id"
        )
    )
    val actors: List<PersonCacheEntity> = arrayListOf()
)