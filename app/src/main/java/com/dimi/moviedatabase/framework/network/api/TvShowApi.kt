package com.dimi.moviedatabase.framework.network.api

import com.dimi.moviedatabase.framework.network.NetworkConstants
import com.dimi.moviedatabase.framework.network.responses.common.ImagesResponse
import com.dimi.moviedatabase.framework.network.responses.common.VideoListResponse
import com.dimi.moviedatabase.framework.network.responses.tv_show.SeasonResponse
import com.dimi.moviedatabase.framework.network.responses.tv_show.TvShowDetailsResponse
import com.dimi.moviedatabase.framework.network.responses.tv_show.TvShowListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvShowApi {

    @GET(NetworkConstants.POPULAR_TV_SHOW_ENDPOINT)
    suspend fun getPopularTvShows(
        @Query(NetworkConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(NetworkConstants.LANGUAGE_REQUEST_PARAM) language: String,
        @Query(NetworkConstants.PAGE_REQUEST_PARAM) page: Int
    ): TvShowListResponse

    @GET(NetworkConstants.TOP_RATED_TV_SHOW_ENDPOINT)
    suspend fun getTopRatedTvShows(
        @Query(NetworkConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(NetworkConstants.LANGUAGE_REQUEST_PARAM) language: String,
        @Query(NetworkConstants.PAGE_REQUEST_PARAM) page: Int
    ): TvShowListResponse

    @GET(NetworkConstants.ON_THE_AIR_TV_SHOW_ENDPOINT)
    suspend fun getOnTheAirTvShows(
        @Query(NetworkConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(NetworkConstants.LANGUAGE_REQUEST_PARAM) language: String,
        @Query(NetworkConstants.PAGE_REQUEST_PARAM) page: Int
    ): TvShowListResponse

    @GET(NetworkConstants.TV_SHOW_SEARCH_ENDPOINT)
    suspend fun searchTvShowQuery(
        @Query(NetworkConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(NetworkConstants.LANGUAGE_REQUEST_PARAM) language: String,
        @Query(NetworkConstants.PAGE_REQUEST_PARAM) page: Int,
        @Query(NetworkConstants.ADULT_REQUEST_PARAM) include_adult: Boolean,
        @Query(NetworkConstants.QUERY_REQUEST_PARAM) query: String
    ): TvShowListResponse

    @GET(NetworkConstants.TV_SHOW_GENRE_ENDPOINT)
    suspend fun getTvShowsByGenre(
        @Query(NetworkConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(NetworkConstants.LANGUAGE_REQUEST_PARAM) language: String,
        @Query(NetworkConstants.SORT_BY_REQUEST_PARAM) sortBy: String,
        @Query(NetworkConstants.PAGE_REQUEST_PARAM) page: Int,
        @Query(NetworkConstants.TIMEZONE_REQUEST_PARAM) timeZone: String,
        @Query(NetworkConstants.GENRE_REQUEST_PARAM) genre: Int,
        @Query(NetworkConstants.FIRST_AIR_DATES_REQUEST_PARAM) firstAirDates: Boolean
    ): TvShowListResponse

    @GET(NetworkConstants.TV_SHOW_GENRE_ENDPOINT)
    suspend fun getTvShowsByNetwork(
        @Query(NetworkConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(NetworkConstants.LANGUAGE_REQUEST_PARAM) language: String,
        @Query(NetworkConstants.SORT_BY_REQUEST_PARAM) sortBy: String,
        @Query(NetworkConstants.PAGE_REQUEST_PARAM) page: Int,
        @Query(NetworkConstants.TIMEZONE_REQUEST_PARAM) timeZone: String,
        @Query(NetworkConstants.NETWORK_REQUEST_PARAM) networkId: Long,
        @Query(NetworkConstants.FIRST_AIR_DATES_REQUEST_PARAM) firstAirDates: Boolean
    ): TvShowListResponse

    @GET("tv/{tvShowId}/recommendations")
    suspend fun getTvShowRecommendations(
        @Path("tvShowId") tvShowId: Long,
        @Query(NetworkConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(NetworkConstants.PAGE_REQUEST_PARAM) page: Int
    ): TvShowListResponse

    @GET("tv/{tvShowId}/similar")
    suspend fun getSimilarTvShows(
        @Path("tvShowId") tvShowId: Long,
        @Query(NetworkConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(NetworkConstants.PAGE_REQUEST_PARAM) page: Int
    ): TvShowListResponse

    @GET(NetworkConstants.TRENDING_TV_SHOW_ENDPOINT)
    suspend fun getTrendingTvShows(
        @Query(NetworkConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(NetworkConstants.PAGE_REQUEST_PARAM) page: Int
    ): TvShowListResponse

    @GET("tv/{tvShowId}")
    suspend fun getTVShowDetails(
        @Path("tvShowId") tvShowId: Long,
        @Query(NetworkConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(NetworkConstants.LANGUAGE_REQUEST_PARAM) language: String,
        @Query(NetworkConstants.APPEND_TO_RESPONSE_PARAM) appendTo: String
    ): TvShowDetailsResponse

    @GET("tv/{tvShowId}/videos")
    suspend fun getVideosByTvShowId(
        @Path("tvShowId") tvShowId: Long,
        @Query(NetworkConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(NetworkConstants.LANGUAGE_REQUEST_PARAM) language: String
    ) : VideoListResponse

    @GET("tv/{tvShowId}/images")
    suspend fun getTvShowImages(
        @Path("tvShowId") tvShowId: Long,
        @Query(NetworkConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(NetworkConstants.INCLUDE_IMAGE_LANG_PARAM) includeImage: String
    ): ImagesResponse

    @GET("tv/{tvShowId}/season/{seasonNumber}")
    suspend fun getTvSeasonEpisodes(
        @Path("tvShowId") tvShowId: Long,
        @Path("seasonNumber") seasonNumber: Int,
        @Query(NetworkConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(NetworkConstants.LANGUAGE_REQUEST_PARAM) language: String
    ) : SeasonResponse
}