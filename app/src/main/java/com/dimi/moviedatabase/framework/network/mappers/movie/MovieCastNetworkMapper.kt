package com.dimi.moviedatabase.framework.network.mappers.movie

import com.dimi.moviedatabase.business.domain.model.Person
import com.dimi.moviedatabase.business.domain.util.EntityMapper
import com.dimi.moviedatabase.framework.network.model.MovieCastNetworkEntity
import javax.inject.Inject

class MovieCastNetworkMapper
@Inject
    constructor(
    ) : EntityMapper<MovieCastNetworkEntity, Person> {

        fun mapFromEntityList(entities: List<MovieCastNetworkEntity> ) : List<Person> {
            return entities.map {
                mapFromEntity( it )
            }
        }

        fun mapToEntityList( entities: List<Person> ) : List<MovieCastNetworkEntity> {
            return entities.map { mapToEntity( it )  }
        }

        override fun mapFromEntity(entity: MovieCastNetworkEntity): Person {
            return Person(
                id = entity.actorId,
                name = entity.name,
                character = entity.character,
                profilePath = entity.profile_path,
                priority = entity.priority
            )
        }

        override fun mapToEntity(domainModel: Person): MovieCastNetworkEntity {
            return MovieCastNetworkEntity(
                name = domainModel.name,
                actorId = domainModel.id,
                character = "",
                priority = 0,
                id = -1
            )
        }

}