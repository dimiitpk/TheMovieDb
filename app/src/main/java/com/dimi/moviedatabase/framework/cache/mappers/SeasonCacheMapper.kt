package com.dimi.moviedatabase.framework.cache.mappers

import com.dimi.moviedatabase.business.domain.model.Season
import com.dimi.moviedatabase.business.domain.util.EntityMapper
import com.dimi.moviedatabase.framework.cache.model.SeasonCacheEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeasonCacheMapper
@Inject
constructor(
    val episodeCacheMapper: EpisodeCacheMapper
): EntityMapper<SeasonCacheEntity, Season>
{

    fun mapFromEntityList( entities: List<SeasonCacheEntity> ) : List<Season> {
        return entities.map { mapFromEntity( it )  }
    }

    fun mapToEntityList( entities: List<Season> ) : List<SeasonCacheEntity> {
        return entities.map { mapToEntity( it )  }
    }

    override fun mapFromEntity(entity: SeasonCacheEntity): Season {
        return Season(
            id = entity.id,
            airDate = entity.airDate,
            seasonName = entity.seasonName,
            seasonNumber = entity.seasonNumber,
            posterPath = entity.posterPath,
            tvShowId = entity.tvShowId,
            overview = entity.overview,
            episodeCount = entity.episodeCount
        )
    }

    override fun mapToEntity(domainModel: Season): SeasonCacheEntity {
        return SeasonCacheEntity(
            id = domainModel.id,
            airDate = domainModel.airDate,
            seasonName = domainModel.seasonName,
            seasonNumber = domainModel.seasonNumber,
            posterPath = domainModel.posterPath,
            tvShowId = domainModel.tvShowId,
            overview = domainModel.overview,
            episodeCount = domainModel.episodeCount
        )
    }

}







