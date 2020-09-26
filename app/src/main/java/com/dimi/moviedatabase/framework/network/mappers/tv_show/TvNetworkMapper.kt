package com.dimi.moviedatabase.framework.network.mappers.tv_show

import com.dimi.moviedatabase.business.domain.model.Network
import com.dimi.moviedatabase.business.domain.util.EntityMapper
import com.dimi.moviedatabase.framework.network.responses.tv_show.NetworkResponse
import javax.inject.Inject

class TvNetworkMapper @Inject
constructor(
) : EntityMapper<NetworkResponse, Network> {
    override fun mapFromEntity(entity: NetworkResponse): Network {
        return Network(id = entity.id, name = entity.name)
    }

    override fun mapToEntity(domainModel: Network): NetworkResponse {
        return NetworkResponse(
            id = domainModel.id,
            name = domainModel.name,
            logoPath = "",
            originCountry = ""
        )
    }

    fun mapFromEntityList(entities: List<NetworkResponse>): List<Network> {
        return entities.map {
            mapFromEntity(it)
        }
    }

}