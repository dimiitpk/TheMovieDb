package com.dimi.moviedatabase.framework.cache.mappers

import com.dimi.moviedatabase.business.domain.model.TvShow
import com.dimi.moviedatabase.business.domain.util.EntityMapper
import com.dimi.moviedatabase.framework.cache.model.*
import com.dimi.moviedatabase.util.asString
import com.dimi.moviedatabase.util.toListOfInts
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvShowCacheMapper
@Inject
constructor(
) : EntityMapper<TvShowCacheEntity, TvShow> {

    override fun mapFromEntity(entity: TvShowCacheEntity): TvShow {
        return TvShow(
            id = entity.id,
            voteCount = entity.voteCount,
            voteAverage = entity.voteAverage,
            firstAirDate = entity.firstAirDate,
            posterPath = entity.posterPath,
            popularity = entity.popularity,
            overview = entity.overview,
            numberOfEpisodes = entity.numberOfEpisodes,
            numberOfSeasons = entity.numberOfSeasons,
            genres = entity.genres!!.toListOfInts(),
            title = entity.title,
            backdropPath = entity.backdropPath,
            status = entity.status,
            homepage = entity.homepage,
            runtime = entity.runtime,
            originalTitle = entity.originalTitle,
            type = entity.type,
            lastAirDate = entity.lastAirDate
        )
    }

    override fun mapToEntity(domainModel: TvShow): TvShowCacheEntity {
        return TvShowCacheEntity(
            id = domainModel.id,
            voteCount = domainModel.voteCount,
            voteAverage = domainModel.voteAverage,
            firstAirDate = domainModel.firstAirDate,
            numberOfEpisodes = domainModel.numberOfEpisodes,
            numberOfSeasons = domainModel.numberOfSeasons,
            posterPath = domainModel.posterPath,
            popularity = domainModel.popularity,
            overview = domainModel.overview,
            genres = domainModel.genres!!.asString(),
            title = domainModel.title,
            backdropPath = domainModel.backdropPath,
            status = domainModel.status,
            homepage = domainModel.homepage,
            runtime = domainModel.runtime,
            originalTitle = domainModel.originalTitle,
            type = domainModel.type,
            lastAirDate = domainModel.lastAirDate
        )
    }


    fun mapFromEntityList(entities: List<TvShowCacheEntity>): List<TvShow> {
        return entities.map { mapFromEntity(it) }
    }

    fun mapToEntityList(entities: List<TvShow>): List<TvShowCacheEntity> {
        return entities.map { mapToEntity(it) }
    }

}







