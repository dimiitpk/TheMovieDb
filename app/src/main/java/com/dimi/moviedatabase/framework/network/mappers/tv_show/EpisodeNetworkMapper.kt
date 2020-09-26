package com.dimi.moviedatabase.framework.network.mappers.tv_show

import com.dimi.moviedatabase.business.domain.model.Episode
import com.dimi.moviedatabase.business.domain.util.EntityMapper
import com.dimi.moviedatabase.framework.network.responses.tv_show.EpisodeResponse
import com.dimi.moviedatabase.util.toDate
import com.dimi.moviedatabase.util.toSimpleString
import javax.inject.Inject

class EpisodeNetworkMapper
@Inject
constructor(
) : EntityMapper<EpisodeResponse, Episode> {

    override fun mapFromEntity(entity: EpisodeResponse): Episode {
        return Episode(
            seasonNumber = entity.seasonNumber,
            episodeNumber = entity.episodeNumber,
            overview = entity.overview,
            stillPath = entity.stillPath,
            voteAverage = entity.voteAverage,
            voteCount = entity.voteCount,
            tvShowId = entity.tvShowId,
            airDate = entity.airDate.toDate(),
            name = entity.name,
            id = entity.id,
            seasonId = -1
        )
    }

    override fun mapToEntity(domainModel: Episode): EpisodeResponse {
        return EpisodeResponse(
            seasonNumber = domainModel.seasonNumber,
            episodeNumber = domainModel.episodeNumber,
            overview = domainModel.overview,
            stillPath = domainModel.stillPath,
            voteAverage = domainModel.voteAverage,
            voteCount = domainModel.voteCount,
            tvShowId = domainModel.tvShowId,
            airDate = domainModel.airDate.toSimpleString(),
            name = domainModel.name,
            id = domainModel.id
        )
    }

}