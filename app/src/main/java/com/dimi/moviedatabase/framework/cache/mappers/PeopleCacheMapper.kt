package com.dimi.moviedatabase.framework.cache.mappers

import com.dimi.moviedatabase.business.domain.model.Person
import com.dimi.moviedatabase.business.domain.util.EntityMapper
import com.dimi.moviedatabase.framework.cache.model.*
import com.dimi.moviedatabase.util.asString
import com.dimi.moviedatabase.util.toListOfString
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PeopleCacheMapper
@Inject
constructor(
   // private val movieCacheMapper: MovieCacheMapper
) : EntityMapper<PersonCacheEntity, Person> {

    override fun mapFromEntity(entity: PersonCacheEntity): Person {
        return Person(
            id = entity.id,
            name = entity.name,
            biography = entity.biography,
            gender = entity.gender,
            alsoKnownAs = entity.alsoKnownAs?.toListOfString(),
            department = entity.department,
            placeOfBirth = entity.placeOfBirth,
            profilePath = entity.profilePath,
            birthday = entity.birthday,
            deathDay = entity.deathDay,
            popular = entity.popularity
        )
    }

    override fun mapToEntity(domainModel: Person): PersonCacheEntity {
        return PersonCacheEntity(
            id = domainModel.id,
            name = domainModel.name,
            biography = domainModel.biography,
            gender = domainModel.gender,
            alsoKnownAs = domainModel.alsoKnownAs?.asString(),
            department = domainModel.department,
            placeOfBirth = domainModel.placeOfBirth,
            profilePath = domainModel.profilePath,
            birthday = domainModel.birthday,
            deathDay = domainModel.deathDay,
            popularity = domainModel.popularity
        )
    }


    fun mapFromEntityList(entities: List<PersonCacheEntity>): List<Person> {
        return entities.map { mapFromEntity(it) }
    }

    fun mapToEntityList(entities: List<Person>): List<PersonCacheEntity> {
        return entities.map { mapToEntity(it) }
    }

}







