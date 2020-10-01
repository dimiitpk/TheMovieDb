package com.dimi.moviedatabase.business.data.network.abstraction

import com.dimi.moviedatabase.business.data.network.responses.MovieSearchResponse
import com.dimi.moviedatabase.business.domain.model.*
import com.dimi.moviedatabase.presentation.common.enums.SortFilter
import com.dimi.moviedatabase.presentation.common.enums.SortOrder
import com.dimi.moviedatabase.presentation.main.search.enums.MediaListType

interface MovieNetworkDataSource {

    suspend fun getListOfMovies(
        query: String,
        page: Int,
        genre: Int,
        mediaListType: MediaListType? = null,
        sortFilter: SortFilter,
        sortOrder: SortOrder
    ): MovieSearchResponse

    suspend fun similarMovies(
        movieId: Int,
        page: Int
    ): MovieSearchResponse

    suspend fun movieRecommendations(
        movieId: Int,
        page: Int
    ): MovieSearchResponse

    suspend fun getMovieDetails(
        movieId: Long
    ): Movie

    suspend fun getMovieVideos(
        movieId: Int
    ): List<Video>

    suspend fun getMovieImages(
        movieId: Int
    ): List<Image>
}
