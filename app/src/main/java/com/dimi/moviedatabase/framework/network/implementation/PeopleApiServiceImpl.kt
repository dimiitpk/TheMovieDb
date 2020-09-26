package com.dimi.moviedatabase.framework.network.implementation

import com.dimi.moviedatabase.business.data.network.abstraction.PeopleNetworkDataSource
import com.dimi.moviedatabase.business.data.network.responses.PeopleSearchResponse
import com.dimi.moviedatabase.business.domain.model.*
import com.dimi.moviedatabase.framework.network.NetworkConstants
import com.dimi.moviedatabase.framework.network.api.PeopleApi
import com.dimi.moviedatabase.framework.network.mappers.NetworkMapper
import com.dimi.moviedatabase.util.Keys
import kotlinx.coroutines.FlowPreview

@FlowPreview
class PeopleApiServiceImpl(
    private val peopleApi: PeopleApi,
    private val networkMapper: NetworkMapper
) : PeopleNetworkDataSource {

    override suspend fun searchPeoples(query: String, page: Int): PeopleSearchResponse {
        return networkMapper.peopleListNetworkMapper.mapFromEntity(
            peopleApi.searchQuery(
                page = page,
                apiKey = Keys.apiKey(),
                query = query,
                include_adult = NetworkConstants.ADULT_DEFAULT,
                language = NetworkConstants.LANGUAGE_DEFAULT
            )
        )
    }

    override suspend fun popularPeoples(page: Int): PeopleSearchResponse {
        return networkMapper.peopleListNetworkMapper.mapFromEntity(
            peopleApi.getPopularPeoples(
                page = page,
                apiKey = Keys.apiKey(),
                language = NetworkConstants.LANGUAGE_DEFAULT
            )
        )
    }

    override suspend fun getPersonDetails(personId: Int): Person {
        return networkMapper.peopleListNetworkMapper.peopleNetworkMapper.mapFromEntity(
            peopleApi.getPersonDetails(
                personId = personId,
                apiKey = Keys.apiKey(),
                language = NetworkConstants.LANGUAGE_DEFAULT
            )
        )
    }

    override suspend fun getPersonMovieCast(personId: Long): List<Movie> {
        return networkMapper.personMovieCastNetworkMapper.mapFromEntityList(
            peopleApi.getPersonMovies(
                personId = personId,
                apiKey = Keys.apiKey(),
                language = NetworkConstants.LANGUAGE_DEFAULT
            )
        )
    }

    override suspend fun getPersonTvShowCast(personId: Long): List<TvShow> {
        return networkMapper.personTvShowCastNetworkMapper.mapFromEntityList(
            peopleApi.getPersonTvShows(
                personId = personId,
                apiKey = Keys.apiKey(),
                language = NetworkConstants.LANGUAGE_DEFAULT
            )
        )
    }

    override suspend fun getPersonImages(personId: Int): List<Image> {
        return networkMapper.imageMapper.mapFromEntityList(
            peopleApi.getPersonImages(
                personId = personId,
                apiKey = Keys.apiKey()
            ).profiles
        )
    }
}