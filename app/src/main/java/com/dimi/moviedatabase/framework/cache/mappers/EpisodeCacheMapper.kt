package com.dimi.moviedatabase.framework.cache.mappers

import com.dimi.moviedatabase.business.domain.model.Episode
import com.dimi.moviedatabase.business.domain.util.EntityMapper
import com.dimi.moviedatabase.framework.cache.model.EpisodeCacheEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EpisodeCacheMapper
@Inject
constructor(): EntityMapper<EpisodeCacheEntity, Episode>
{

    fun mapFromEntityList( entities: List<EpisodeCacheEntity> ) : List<Episode> {
        return entities.map { mapFromEntity( it )  }
    }

    fun mapToEntityList( entities: List<Episode> ) : List<EpisodeCacheEntity> {
        return entities.map { mapToEntity( it )  }
    }

    override fun mapFromEntity(entity: EpisodeCacheEntity): Episode {
        return Episode(
            id = entity.id,
            airDate = entity.airDate,
            name = entity.name,
            voteCount = entity.voteCount,
            episodeNumber = entity.episodeNumber,
            seasonNumber = entity.seasonNumber,
            stillPath = entity.stillPath,
            tvShowId = entity.tvShowId,
            overview = entity.overview,
            voteAverage = entity.voteAverage,
            seasonId = entity.seasonId
        )
    }

    override fun mapToEntity(domainModel: Episode): EpisodeCacheEntity {
        return EpisodeCacheEntity(
            id = domainModel.id,
            airDate = domainModel.airDate,
            name = domainModel.name,
            voteCount = domainModel.voteCount,
            episodeNumber = domainModel.episodeNumber,
            seasonNumber = domainModel.seasonNumber,
            stillPath = domainModel.stillPath,
            tvShowId = domainModel.tvShowId,
            overview = domainModel.overview,
            voteAverage = domainModel.voteAverage,
            seasonId = domainModel.seasonId
        )
    }

}







