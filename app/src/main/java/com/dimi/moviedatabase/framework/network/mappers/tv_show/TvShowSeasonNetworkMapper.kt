package com.dimi.moviedatabase.framework.network.mappers.tv_show

import com.dimi.moviedatabase.business.domain.model.Episode
import com.dimi.moviedatabase.business.domain.model.Season
import com.dimi.moviedatabase.business.domain.util.EntityMapper
import com.dimi.moviedatabase.framework.network.responses.tv_show.SeasonResponse
import com.dimi.moviedatabase.util.toDate
import javax.inject.Inject

class TvShowSeasonNetworkMapper
@Inject
constructor(
    private val episodeNetworkMapper: EpisodeNetworkMapper
) : EntityMapper<SeasonResponse, Season> {

    fun mapFromEntityList(entities: List<SeasonResponse>, tvShowId: Long ) : List<Season> {
        return entities.map {
            val season = mapFromEntity( it )
            season.tvShowId = tvShowId
            season
        }
    }

    fun mapToEntityList( entities: List<Season> ) : List<SeasonResponse> {
        return entities.map { mapToEntity( it )  }
    }

    override fun mapFromEntity(entity: SeasonResponse): Season {
        return Season(
            seasonName = entity.name,
            episodeCount = entity.episodeCount,
            overview = entity.overview,
            posterPath = entity.posterPath,
            seasonNumber = entity.seasonNumber,
            tvShowId = -1,
            airDate = entity.airDate.toDate(),
            id = entity.id.toLong()
        )
    }

    override fun mapToEntity(domainModel: Season): SeasonResponse {
        return SeasonResponse(
            name = domainModel.seasonName,
            episodeCount = domainModel.episodeCount,
            overview = "",
            posterPath = "",
            seasonNumber = domainModel.seasonNumber,
            airDate = "",
            id = 0,
            episodes = emptyList()
        )
    }

    fun mapToEpisodeList(response: SeasonResponse): List<Episode> {
        return response.episodes.map { episodeNetworkMapper.mapFromEntity( it )  }
    }
}