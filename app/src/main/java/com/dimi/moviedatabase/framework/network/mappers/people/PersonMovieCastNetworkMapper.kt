package com.dimi.moviedatabase.framework.network.mappers.people

import com.dimi.moviedatabase.business.domain.model.Movie
import com.dimi.moviedatabase.business.domain.util.EntityMapper
import com.dimi.moviedatabase.framework.network.responses.people.PersonMovieCast
import com.dimi.moviedatabase.framework.network.responses.people.PersonMovieCastResponse
import com.dimi.moviedatabase.util.toDate
import javax.inject.Inject

class PersonMovieCastNetworkMapper
@Inject
constructor() :
    EntityMapper<PersonMovieCast, Movie> {

    fun mapFromEntityList( response: PersonMovieCastResponse) : List<Movie> {
        return response.results.map {
            val movie = mapFromEntity( it )
            movie.actorId = response.id
            movie
        }
    }

    override fun mapFromEntity(entity: PersonMovieCast): Movie {
        return Movie(
            id = entity.id.toLong(),
            title = entity.title,
            genres = entity.genres,
            overview = entity.overview,
            popularity = entity.popularity,
            posterPath = entity.posterPath,
            releaseDate = entity.releaseDate.toDate(),
            voteAverage = entity.voteAverage,
            backdropPath = entity.backdropPath,
            voteCount = entity.voteCount,
            originalTitle = entity.originalTitle,
            character = entity.character,
            tagLine = null
        )
    }

    override fun mapToEntity(domainModel: Movie): PersonMovieCast {
        return PersonMovieCast(
            id = 0,
            backdropPath = "",
            posterPath = "",
            title = "",
            genres = domainModel.genres ?: arrayListOf(),
            overview = "",
            popularity = 0.0,
            releaseDate = "",
            voteAverage = 0f,
            voteCount = 0,
            originalTitle = "",
            adult = false,
            character = "",
            creditId = ""
        )
    }
}