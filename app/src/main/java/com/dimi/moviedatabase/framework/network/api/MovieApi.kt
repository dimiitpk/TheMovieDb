package com.dimi.moviedatabase.framework.network.api

import com.dimi.moviedatabase.framework.network.NetworkConstants
import com.dimi.moviedatabase.framework.network.responses.common.ImagesResponse
import com.dimi.moviedatabase.framework.network.responses.common.VideoListResponse
import com.dimi.moviedatabase.framework.network.responses.movie.MovieDetailsResponse
import com.dimi.moviedatabase.framework.network.responses.movie.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET(NetworkConstants.POPULAR_MOVIES_ENDPOINT)
    suspend fun getPopularMovies(
        @Query(NetworkConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(NetworkConstants.LANGUAGE_REQUEST_PARAM) language: String,
        @Query(NetworkConstants.PAGE_REQUEST_PARAM) page: Int
    ): MovieListResponse

    @GET(NetworkConstants.TOP_RATED_MOVIES_ENDPOINT)
    suspend fun getTopRatedMovies(
        @Query(NetworkConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(NetworkConstants.LANGUAGE_REQUEST_PARAM) language: String,
        @Query(NetworkConstants.PAGE_REQUEST_PARAM) page: Int
    ): MovieListResponse

    @GET(NetworkConstants.UPCOMING_MOVIES_ENDPOINT)
    suspend fun getUpcomingMovies(
        @Query(NetworkConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(NetworkConstants.LANGUAGE_REQUEST_PARAM) language: String,
        @Query(NetworkConstants.PAGE_REQUEST_PARAM) page: Int
    ): MovieListResponse

    @GET(NetworkConstants.MOVIES_SEARCH_ENDPOINT)
    suspend fun searchQuery(
        @Query(NetworkConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(NetworkConstants.LANGUAGE_REQUEST_PARAM) language: String,
        @Query(NetworkConstants.PAGE_REQUEST_PARAM) page: Int,
        @Query(NetworkConstants.ADULT_REQUEST_PARAM) include_adult: Boolean,
        @Query(NetworkConstants.QUERY_REQUEST_PARAM) query: String
    ): MovieListResponse

    @GET(NetworkConstants.MOVIES_DISCOVER_ENDPOINT)
    suspend fun discoverMovies(
        @Query(NetworkConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(NetworkConstants.LANGUAGE_REQUEST_PARAM) language: String,
        @Query(NetworkConstants.SORT_BY_REQUEST_PARAM) sortBy: String,
        @Query(NetworkConstants.ADULT_REQUEST_PARAM) includeAdult: Boolean,
        @Query(NetworkConstants.VIDEO_REQUEST_PARAM) includeVideo: Boolean,
        @Query(NetworkConstants.PAGE_REQUEST_PARAM) page: Int,
        @Query(NetworkConstants.GENRE_REQUEST_PARAM) genre: Int?,
        @Query("vote_count.gte") voteCountGte: Int?
    ): MovieListResponse

    @GET(NetworkConstants.TRENDING_MOVIES_ENDPOINT)
    suspend fun getTrendingMovies(
        @Query(NetworkConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(NetworkConstants.PAGE_REQUEST_PARAM) page: Int
    ): MovieListResponse

    @GET("movie/{movieId}/videos")
    suspend fun getMovieVideos(
        @Path("movieId") movieId: Int,
        @Query(NetworkConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(NetworkConstants.LANGUAGE_REQUEST_PARAM) language: String
    ): VideoListResponse

    @GET("movie/{movieId}/recommendations")
    suspend fun getMovieRecommendations(
        @Path("movieId") movieId: Int,
        @Query(NetworkConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(NetworkConstants.PAGE_REQUEST_PARAM) page: Int
    ): MovieListResponse

    @GET("movie/{movieId}/similar")
    suspend fun getSimilarMovies(
        @Path("movieId") movieId: Int,
        @Query(NetworkConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(NetworkConstants.PAGE_REQUEST_PARAM) page: Int
    ): MovieListResponse

    @GET("movie/{movieId}/images")
    suspend fun getMovieImages(
        @Path("movieId") movieId: Int,
        @Query(NetworkConstants.API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(NetworkConstants.INCLUDE_IMAGE_LANG_PARAM) includeImage: String
    ): ImagesResponse

    @GET("movie/{movieId}")
    suspend fun getMovieDetails(
    @Path("movieId") movieId: Long,
    @Query(NetworkConstants.API_KEY_REQUEST_PARAM) apiKey: String,
    @Query(NetworkConstants.LANGUAGE_REQUEST_PARAM) language: String,
    @Query(NetworkConstants.APPEND_TO_RESPONSE_PARAM) appendTo: String
    ): MovieDetailsResponse
}