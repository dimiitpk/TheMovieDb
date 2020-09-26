package com.dimi.moviedatabase.framework.network.mappers.movie

import com.dimi.moviedatabase.business.domain.model.Movie
import com.dimi.moviedatabase.business.domain.util.EntityMapper
import com.dimi.moviedatabase.framework.network.model.MovieNetworkEntity
import com.dimi.moviedatabase.util.toDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieNetworkMapper
@Inject
constructor() : EntityMapper<MovieNetworkEntity, Movie> {

    fun mapFromEntityList(entities: List<MovieNetworkEntity>): List<Movie> {
        return entities.map { mapFromEntity(it) }
    }

    fun mapToEntityList(entities: List<Movie>): List<MovieNetworkEntity> {
        return entities.map { mapToEntity(it) }
    }

    override fun mapFromEntity(entity: MovieNetworkEntity): Movie {
        return Movie(
            id = entity.id.toLong(),
            title = entity.title,
            genres = entity.genres,
            overview = entity.overview,
            popularity = entity.popularity,
            posterPath = entity.posterPath,
            releaseDate = entity.releaseDate.toDate(),
            voteAverage = entity.voteAverage,
            voteCount = entity.voteCount,
            backdropPath = entity.backdropPath,
            tagLine = null
        )
    }

    override fun mapToEntity(domainModel: Movie): MovieNetworkEntity {
        return MovieNetworkEntity(
            id = domainModel.id.toInt(),
            title = domainModel.title,
            genres = domainModel.genres,
            overview = domainModel.overview,
            popularity = domainModel.popularity,
            posterPath = domainModel.posterPath,
            releaseDate = "",
            voteAverage = domainModel.voteAverage,
            voteCount = domainModel.voteCount,
            backdropPath = domainModel.backdropPath
        )
    }
}