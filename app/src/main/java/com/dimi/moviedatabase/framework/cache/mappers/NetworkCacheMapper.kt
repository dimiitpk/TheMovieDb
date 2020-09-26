package com.dimi.moviedatabase.framework.cache.mappers

import com.dimi.moviedatabase.business.domain.model.Network
import com.dimi.moviedatabase.business.domain.util.EntityMapper
import com.dimi.moviedatabase.framework.cache.model.NetworkCacheEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkCacheMapper
@Inject
constructor(
) : EntityMapper<NetworkCacheEntity, Network> {
    fun mapFromEntityList(entities: List<NetworkCacheEntity>): List<Network> {
        return entities.map { mapFromEntity(it) }
    }

    fun mapToEntityList(entities: List<Network>): List<NetworkCacheEntity> {
        return entities.map { mapToEntity(it) }
    }

    override fun mapFromEntity(entity: NetworkCacheEntity): Network {
        return Network(
            id = entity.id,
            name = entity.name
        )
    }

    override fun mapToEntity(domainModel: Network): NetworkCacheEntity {
        return NetworkCacheEntity(
            id = domainModel.id,
            name = domainModel.name
        )
    }
}







