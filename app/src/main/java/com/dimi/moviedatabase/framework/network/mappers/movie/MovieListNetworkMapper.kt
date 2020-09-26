package com.dimi.moviedatabase.framework.network.mappers.movie

import com.dimi.moviedatabase.business.data.network.responses.MovieSearchResponse
import com.dimi.moviedatabase.business.domain.util.EntityMapper
import com.dimi.moviedatabase.framework.network.responses.movie.MovieListResponse
import javax.inject.Inject

class MovieListNetworkMapper
@Inject
constructor(
    private val movieNetworkMapper : MovieNetworkMapper
) : EntityMapper<MovieListResponse, MovieSearchResponse> {

    override fun mapFromEntity(entity: MovieListResponse): MovieSearchResponse {
        return MovieSearchResponse(
            page = entity.page,
            results = movieNetworkMapper.mapFromEntityList(entity.results),
            totalPages = entity.totalPages,
            totalResults = entity.totalResults
        )
    }

    override fun mapToEntity(domainModel: MovieSearchResponse): MovieListResponse {
        return MovieListResponse(
            page = domainModel.page,
            results = movieNetworkMapper.mapToEntityList(domainModel.results),
            totalPages = domainModel.totalPages,
            totalResults = domainModel.totalResults
        )
    }
}