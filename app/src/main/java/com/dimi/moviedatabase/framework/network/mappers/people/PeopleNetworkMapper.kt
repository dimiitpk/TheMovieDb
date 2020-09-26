package com.dimi.moviedatabase.framework.network.mappers.people

import com.dimi.moviedatabase.business.domain.model.Person
import com.dimi.moviedatabase.business.domain.util.EntityMapper
import com.dimi.moviedatabase.framework.network.model.PersonNetworkEntity
import com.dimi.moviedatabase.util.toDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PeopleNetworkMapper
@Inject
constructor() : EntityMapper<PersonNetworkEntity, Person> {

    fun mapFromEntityList(entities: List<PersonNetworkEntity>): List<Person> {
        return entities.map { mapFromEntity(it) }
    }

    fun mapToEntityList(entities: List<Person>): List<PersonNetworkEntity> {
        return entities.map { mapToEntity(it) }
    }

    override fun mapFromEntity(entity: PersonNetworkEntity): Person {
        return Person(
            id = entity.id.toLong(),
            name = entity.name,
            gender = entity.gender,
            deathDay = entity.deathDay.toDate(),
            birthday = entity.birthday.toDate(),
            popular = entity.popularity,
            profilePath = entity.profilePath,
            placeOfBirth = entity.placeOfBirth,
            alsoKnownAs = entity.alsoKnownAs,
            biography = entity.biography,
            department = entity.department
        )
    }

    override fun mapToEntity(domainModel: Person): PersonNetworkEntity {
        return PersonNetworkEntity(
            id = domainModel.id.toInt(),
            name = domainModel.name,
            gender = domainModel.gender,
            deathDay = "",
            birthday = "",
            popularity = domainModel.popularity,
            profilePath = domainModel.profilePath,
            placeOfBirth = domainModel.placeOfBirth,
            alsoKnownAs = domainModel.alsoKnownAs,
            biography = domainModel.biography,
            department = domainModel.department
        )
    }
}