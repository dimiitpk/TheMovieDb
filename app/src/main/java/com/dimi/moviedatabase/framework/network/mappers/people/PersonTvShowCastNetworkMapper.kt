package com.dimi.moviedatabase.framework.network.mappers.people

import com.dimi.moviedatabase.business.domain.model.TvShow
import com.dimi.moviedatabase.business.domain.util.EntityMapper
import com.dimi.moviedatabase.framework.network.responses.people.PersonTvShowCast
import com.dimi.moviedatabase.framework.network.responses.people.PersonTvShowCastResponse
import com.dimi.moviedatabase.util.toDate
import javax.inject.Inject

class PersonTvShowCastNetworkMapper
@Inject
constructor() :
    EntityMapper<PersonTvShowCast, TvShow> {

    fun mapFromEntityList( response: PersonTvShowCastResponse) : List<TvShow> {
        return response.results.map {
            mapFromEntity( it )
        }
    }

    override fun mapFromEntity(entity: PersonTvShowCast): TvShow {
        return TvShow(
            id = entity.id.toLong(),
            title = entity.title,
            genres = entity.genres,
            overview = entity.overview,
            popularity = entity.popularity,
            posterPath = entity.posterPath,
            firstAirDate = entity.firstAirDate.toDate(),
            voteAverage = entity.voteAverage,
            backdropPath = entity.backdropPath,
            voteCount = entity.voteCount,
            originalTitle = entity.originalTitle,
            character = entity.character
        )
    }

    override fun mapToEntity(domainModel: TvShow): PersonTvShowCast {
        return PersonTvShowCast(
            id = 0,
            backdropPath = "",
            posterPath = "",
            title = "",
            genres = domainModel.genres ?: arrayListOf(),
            overview = "",
            popularity = 0.0,
            firstAirDate = "",
            voteAverage = 0f,
            voteCount = 0,
            originalTitle = "",
            character = "",
            creditId = ""
        )
    }
}