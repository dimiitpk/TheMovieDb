package com.dimi.moviedatabase.framework.network.mappers.tv_show

import com.dimi.moviedatabase.business.domain.model.Person
import com.dimi.moviedatabase.business.domain.util.EntityMapper
import com.dimi.moviedatabase.framework.network.model.TvShowCastNetworkEntity
import javax.inject.Inject

class TvShowCastNetworkMapper
@Inject
constructor(
) : EntityMapper<TvShowCastNetworkEntity, Person> {

    fun mapFromEntityList(entities: List<TvShowCastNetworkEntity>) : List<Person> {
        return entities.map {
            mapFromEntity( it )
        }
    }

    fun mapToEntityList( entities: List<Person> ) : List<TvShowCastNetworkEntity> {
        return entities.map { mapToEntity( it )  }
    }

    override fun mapFromEntity(entity: TvShowCastNetworkEntity): Person {
        return Person(
            name = entity.name,
            id = entity.actorId,
            character = entity.character,
            profilePath = entity.profile_path,
            priority = entity.priority
        )
    }

    override fun mapToEntity(domainModel: Person): TvShowCastNetworkEntity {
        return TvShowCastNetworkEntity(
            name = domainModel.name,
            actorId = domainModel.id,
            character = "",
            priority = 0,
            id = ""
        )
    }

}