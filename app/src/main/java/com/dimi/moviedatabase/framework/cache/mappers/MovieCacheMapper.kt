package com.dimi.moviedatabase.framework.cache.mappers

import com.dimi.moviedatabase.business.domain.model.Movie
import com.dimi.moviedatabase.business.domain.util.EntityMapper
import com.dimi.moviedatabase.framework.cache.model.MovieCacheEntity
import com.dimi.moviedatabase.util.asString
import com.dimi.moviedatabase.util.toListOfInts
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MovieCacheMapper
@Inject
constructor() : EntityMapper<MovieCacheEntity, Movie> {

    override fun mapFromEntity(entity: MovieCacheEntity): Movie {
        return Movie(
            id = entity.id,
            voteCount = entity.voteCount,
            voteAverage = entity.voteAverage,
            releaseDate = entity.releaseDate,
            posterPath = entity.posterPath,
            popularity = entity.popularity,
            overview = entity.overview,
            genres = entity.genres!!.toListOfInts(),
            title = entity.title,
            backdropPath = entity.backdropPath,
            tagLine = entity.tagLine,
            originalTitle = entity.originalTitle,
            budget = entity.budget,
            imdbId = entity.imdbId,
            revenue = entity.revenue,
            runtime = entity.runtime,
            homepage = entity.homepage,
            status = entity.status
        )
    }

    override fun mapToEntity(domainModel: Movie): MovieCacheEntity {
        return MovieCacheEntity(
            id = domainModel.id,
            voteCount = domainModel.voteCount,
            voteAverage = domainModel.voteAverage,
            releaseDate = domainModel.releaseDate,
            posterPath = domainModel.posterPath,
            popularity = domainModel.popularity,
            overview = domainModel.overview,
            tagLine = domainModel.tagLine ?: "",
            genres = domainModel.genres!!.asString(),
            title = domainModel.title,
            backdropPath = domainModel.backdropPath,
            originalTitle = domainModel.originalTitle,
            budget = domainModel.budget,
            imdbId = domainModel.imdbId,
            revenue = domainModel.revenue,
            runtime = domainModel.runtime,
            homepage = domainModel.homepage,
            status = domainModel.status
        )
    }


    fun mapFromEntityList(entities: List<MovieCacheEntity>): List<Movie> {
        return entities.map { mapFromEntity(it) }
    }

    fun mapToEntityList(entities: List<Movie>): List<MovieCacheEntity> {
        return entities.map { mapToEntity(it) }
    }
}







