package com.dimi.moviedatabase.framework.cache.model.relations

import androidx.room.*
import com.dimi.moviedatabase.framework.cache.model.MediaListEntity
import com.dimi.moviedatabase.framework.cache.model.MovieCacheEntity
import com.dimi.moviedatabase.framework.cache.model.junction.MovieListCrossRef

data class MoviesByList(

    @Embedded val mediaList: MediaListEntity,
    @Relation(
        entity = MovieListCrossRef::class,
        entityColumn = "media_list_id",
        parentColumn = "id"
    )
    var crossRef: List<MovieListCrossRef> = arrayListOf(),
    @Relation(
        parentColumn = "id",
        entity = MovieCacheEntity::class,
        entityColumn = "id",
        associateBy = Junction(
            value = MovieListCrossRef::class,
            parentColumn = "media_list_id",
            entityColumn = "movie_id"
        )
    )
    val movies: List<MovieCacheEntity> = arrayListOf()
)