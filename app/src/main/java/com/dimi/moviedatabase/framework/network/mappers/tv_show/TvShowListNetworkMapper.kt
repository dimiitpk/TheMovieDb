package com.dimi.moviedatabase.framework.network.mappers.tv_show

import com.dimi.moviedatabase.business.data.network.responses.TvShowSearchResponse
import com.dimi.moviedatabase.business.domain.util.EntityMapper
import com.dimi.moviedatabase.framework.network.responses.tv_show.TvShowListResponse
import javax.inject.Inject

class TvShowListNetworkMapper
@Inject
constructor(
    private val tvShowNetworkMapper : TvShowNetworkMapper
) : EntityMapper<TvShowListResponse, TvShowSearchResponse> {

    override fun mapFromEntity(entity: TvShowListResponse): TvShowSearchResponse {
        return TvShowSearchResponse(
            page = entity.page,
            results = tvShowNetworkMapper.mapFromEntityList(entity.results),
            totalPages = entity.totalPages,
            totalResults = entity.totalResults
        )
    }

    override fun mapToEntity(domainModel: TvShowSearchResponse): TvShowListResponse {
        return TvShowListResponse(
            page = domainModel.page,
            results = tvShowNetworkMapper.mapToEntityList(domainModel.results),
            totalPages = domainModel.totalPages,
            totalResults = domainModel.totalResults
        )
    }
}