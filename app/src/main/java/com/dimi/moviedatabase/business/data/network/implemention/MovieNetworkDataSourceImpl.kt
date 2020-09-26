package com.dimi.moviedatabase.business.data.network.implemention

import com.dimi.moviedatabase.business.data.network.abstraction.MovieNetworkDataSource
import com.dimi.moviedatabase.business.data.network.responses.MovieSearchResponse
import com.dimi.moviedatabase.business.domain.model.*
import com.dimi.moviedatabase.presentation.main.search.SortFilter
import com.dimi.moviedatabase.presentation.main.search.SortOrder
import com.dimi.moviedatabase.presentation.main.search.enums.MediaListType

class MovieNetworkDataSourceImpl(
    private val mainApiService: MovieNetworkDataSource
) : MovieNetworkDataSource {

    override suspend fun getListOfMovies(
        query: String,
        page: Int,
        genre: Int,
        mediaListType: MediaListType?,
        sortFilter: SortFilter,
        sortOrder: SortOrder
    ): MovieSearchResponse {
        return mainApiService.getListOfMovies(
            query = query,
            page = page,
            genre = genre,
            mediaListType = mediaListType,
            sortFilter = sortFilter,
            sortOrder = sortOrder
        )
    }

    override suspend fun similarMovies(movieId: Int, page: Int): MovieSearchResponse {
        return mainApiService.similarMovies(movieId, page)
    }

    override suspend fun movieRecommendations(movieId: Int, page: Int): MovieSearchResponse {
        return mainApiService.movieRecommendations(movieId, page)
    }

    override suspend fun getMovieDetails(movieId: Long): Movie {
        return mainApiService.getMovieDetails(movieId)
    }

    override suspend fun getMovieVideos(movieId: Int): List<Video> {
        return mainApiService.getMovieVideos(movieId)
    }

    override suspend fun getMovieImages(movieId: Int): List<Image> {
        return mainApiService.getMovieImages(movieId)
    }
}





























