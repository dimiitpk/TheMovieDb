package com.dimi.moviedatabase.framework.network.mappers.people

import com.dimi.moviedatabase.business.data.network.responses.PeopleSearchResponse
import com.dimi.moviedatabase.business.domain.util.EntityMapper
import com.dimi.moviedatabase.framework.network.responses.people.PeopleListResponse
import javax.inject.Inject

class PeopleListNetworkMapper
@Inject
constructor(
    val peopleNetworkMapper: PeopleNetworkMapper
) : EntityMapper<PeopleListResponse, PeopleSearchResponse> {

    override fun mapFromEntity(entity: PeopleListResponse): PeopleSearchResponse {
        return PeopleSearchResponse(
            page = entity.page,
            results = peopleNetworkMapper.mapFromEntityList(entity.results),
            totalPages = entity.totalPages,
            totalResults = entity.totalResults
        )
    }

    override fun mapToEntity(domainModel: PeopleSearchResponse): PeopleListResponse {
        return PeopleListResponse(
            page = domainModel.page,
            results = peopleNetworkMapper.mapToEntityList(domainModel.results),
            totalPages = domainModel.totalPages,
            totalResults = domainModel.totalResults
        )
    }
}