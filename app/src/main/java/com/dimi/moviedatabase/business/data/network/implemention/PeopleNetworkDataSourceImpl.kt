package com.dimi.moviedatabase.business.data.network.implemention

import com.dimi.moviedatabase.business.data.network.abstraction.PeopleNetworkDataSource
import com.dimi.moviedatabase.business.data.network.responses.PeopleSearchResponse
import com.dimi.moviedatabase.business.domain.model.Image
import com.dimi.moviedatabase.business.domain.model.Movie
import com.dimi.moviedatabase.business.domain.model.Person
import com.dimi.moviedatabase.business.domain.model.TvShow

class PeopleNetworkDataSourceImpl(
    private val mainApiService: PeopleNetworkDataSource
) : PeopleNetworkDataSource {

    override suspend fun searchPeoples(
        query: String,
        page: Int
    ): PeopleSearchResponse {
        return if (query.isBlank()) {
            mainApiService.popularPeoples(
                page = page
            )
        } else
            mainApiService.searchPeoples(
                query = query,
                page = page
            )
    }

    override suspend fun popularPeoples(page: Int): PeopleSearchResponse {
        return mainApiService.popularPeoples(page)
    }

    override suspend fun getPersonDetails(personId: Int): Person {
        return mainApiService.getPersonDetails(personId)
    }

    override suspend fun getPersonMovieCast(personId: Long): List<Movie> {
        return mainApiService.getPersonMovieCast(personId)
    }

    override suspend fun getPersonTvShowCast(personId: Long): List<TvShow> {
        return mainApiService.getPersonTvShowCast(personId)
    }

    override suspend fun getPersonImages(personId: Int): List<Image> {
        return mainApiService.getPersonImages(personId)
    }
}





























