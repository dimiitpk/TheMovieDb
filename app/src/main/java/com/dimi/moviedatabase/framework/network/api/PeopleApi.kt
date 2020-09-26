package com.dimi.moviedatabase.framework.network.api

import com.dimi.moviedatabase.framework.network.NetworkConstants
import com.dimi.moviedatabase.framework.network.model.PersonNetworkEntity
import com.dimi.moviedatabase.framework.network.responses.people.PeopleListResponse
import com.dimi.moviedatabase.framework.network.responses.people.PersonImagesResponse
import com.dimi.moviedatabase.framework.network.responses.people.PersonMovieCastResponse
import com.dimi.moviedatabase.framework.network.responses.people.PersonTvShowCastResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PeopleApi {

    @GET(NetworkConstants.POPULAR_PEOPLE_ENDPOINT)
    suspend fun getPopularPeoples(
        @Query(NetworkConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(NetworkConstants.LANGUAGE_REQUEST_PARAM) language: String,
        @Query(NetworkConstants.PAGE_REQUEST_PARAM) page: Int
    ): PeopleListResponse

    @GET(NetworkConstants.PEOPLE_SEARCH_ENDPOINT)
    suspend fun searchQuery(
        @Query(NetworkConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(NetworkConstants.LANGUAGE_REQUEST_PARAM) language: String,
        @Query(NetworkConstants.PAGE_REQUEST_PARAM) page: Int,
        @Query(NetworkConstants.ADULT_REQUEST_PARAM) include_adult: Boolean,
        @Query(NetworkConstants.QUERY_REQUEST_PARAM) query: String
    ): PeopleListResponse

    @GET("person/{personId}/images")
    suspend fun getPersonImages(
        @Path("personId") personId: Int,
        @Query(NetworkConstants.API_KEY_REQUEST_PARAM) apiKey: String
    ): PersonImagesResponse

    @GET("person/{personId}")
    suspend fun getPersonDetails(
        @Path("personId") personId: Int,
        @Query(NetworkConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(NetworkConstants.LANGUAGE_REQUEST_PARAM) language: String
    ): PersonNetworkEntity

    @GET("person/{personId}/movie_credits")
    suspend fun getPersonMovies(
        @Path("personId") personId: Long,
        @Query(NetworkConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(NetworkConstants.LANGUAGE_REQUEST_PARAM) language: String
    ): PersonMovieCastResponse

    @GET("person/{personId}/tv_credits")
    suspend fun getPersonTvShows(
        @Path("personId") personId: Long,
        @Query(NetworkConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(NetworkConstants.LANGUAGE_REQUEST_PARAM) language: String
    ): PersonTvShowCastResponse
}
