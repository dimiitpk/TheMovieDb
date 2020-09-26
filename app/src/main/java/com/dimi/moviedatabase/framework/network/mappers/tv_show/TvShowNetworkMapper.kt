package com.dimi.moviedatabase.framework.network.mappers.tv_show

import com.dimi.moviedatabase.business.domain.model.TvShow
import com.dimi.moviedatabase.business.domain.util.EntityMapper
import com.dimi.moviedatabase.framework.network.model.TvShowNetworkEntity
import com.dimi.moviedatabase.util.toDate
import com.dimi.moviedatabase.util.toSimpleString
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvShowNetworkMapper
@Inject
constructor() : EntityMapper<TvShowNetworkEntity, TvShow> {

    fun mapFromEntityList(entities: List<TvShowNetworkEntity>): List<TvShow> {
        return entities.map { mapFromEntity(it) }
    }

    fun mapToEntityList(entities: List<TvShow>): List<TvShowNetworkEntity> {
        return entities.map { mapToEntity(it) }
    }

    override fun mapFromEntity(entity: TvShowNetworkEntity): TvShow {
        return TvShow(
            id = entity.id.toLong(),
            title = entity.title,
            genres = entity.genres,
            overview = entity.overview,
            popularity = entity.popularity,
            posterPath = entity.posterPath,
            firstAirDate = entity.firstAirDate.toDate(),
            voteAverage = entity.voteAverage,
            voteCount = entity.voteCount,
            backdropPath = entity.backdropPath
        )
    }

    override fun mapToEntity(domainModel: TvShow): TvShowNetworkEntity {
        return TvShowNetworkEntity(
            id = domainModel.id.toInt(),
            title = domainModel.title,
            genres = domainModel.genres,
            overview = domainModel.overview,
            popularity = domainModel.popularity,
            posterPath = domainModel.posterPath,
            firstAirDate = domainModel.firstAirDate.toSimpleString(),
            voteAverage = domainModel.voteAverage,
            voteCount = domainModel.voteCount,
            backdropPath = domainModel.backdropPath
        )
    }
}