package com.dimi.moviedatabase.business.data.network.abstraction

import com.dimi.moviedatabase.business.data.network.responses.PeopleSearchResponse
import com.dimi.moviedatabase.business.domain.model.*

interface PeopleNetworkDataSource {

    suspend fun searchPeoples(
        query: String,
        page: Int
    ): PeopleSearchResponse

    suspend fun popularPeoples(
        page: Int,
    ): PeopleSearchResponse

    suspend fun getPersonDetails(
        personId: Int
    ): Person

    suspend fun getPersonMovieCast(
        personId: Long
    ): List<Movie>

    suspend fun getPersonTvShowCast(
        personId: Long
    ): List<TvShow>

    suspend fun getPersonImages(personId: Int): List<Image>
}
