package com.dimi.moviedatabase.business.data.network

import com.dimi.moviedatabase.business.data.MovieFactory
import com.dimi.moviedatabase.business.data.network.abstraction.MovieNetworkDataSource
import com.dimi.moviedatabase.business.data.network.responses.MovieSearchResponse
import com.dimi.moviedatabase.business.domain.model.Image
import com.dimi.moviedatabase.business.domain.model.Movie
import com.dimi.moviedatabase.business.domain.model.Video
import com.dimi.moviedatabase.presentation.common.enums.SortFilter
import com.dimi.moviedatabase.presentation.common.enums.SortOrder
import com.dimi.moviedatabase.presentation.main.search.enums.MediaListType
import com.dimi.moviedatabase.util.Constants.TEST_FORCE_FAIL
import com.dimi.moviedatabase.util.Constants.TEST_FORCE_THROW_EXCEPTION
import com.dimi.moviedatabase.util.Constants.TEST_VALID_MOVIE_ID
import com.dimi.moviedatabase.util.Constants.TEST_VALID_GENRE_ID
import com.dimi.moviedatabase.util.Constants.TEST_VALID_SEARCH_QUERY
import com.dimi.moviedatabase.util.GENRE_DEFAULT
import java.lang.Exception

class FakeMovieNetworkDataSourceImpl(
    private val movieSearchResponse: MovieSearchResponse,
    private val movieGenreSearchResponse: MovieSearchResponse,
    private val movieQuerySearchResponse: MovieSearchResponse,
    private val movieDetails: Movie,
    private val movieTrailers: List<Video>,
    private val movieImages: List<Image>
) : MovieNetworkDataSource {

    override suspend fun getListOfMovies(
        query: String,
        page: Int,
        genre: Int,
        mediaListType: MediaListType?,
        sortFilter: SortFilter,
        sortOrder: SortOrder
    ): MovieSearchResponse {

        if (query == TEST_FORCE_THROW_EXCEPTION.toString())
            throw Exception("Something went wrong getting results from web.")

        if (genre != GENRE_DEFAULT) {
            if (page > movieGenreSearchResponse.totalPages)
                return MovieSearchResponse(
                    page = 1,
                    totalResults = 0,
                    totalPages = 1,
                    results = emptyList()
                )

            return if (genre == TEST_VALID_GENRE_ID)
                movieGenreSearchResponse
            else MovieSearchResponse(
                page = 1,
                totalResults = 0,
                totalPages = 1,
                results = emptyList()
            )
        }

        if (query.isNotBlank()) {

            if (page > movieQuerySearchResponse.totalPages)
                return MovieSearchResponse(
                    page = 1,
                    totalResults = 0,
                    totalPages = 1,
                    results = emptyList()
                )

            return if (query == TEST_VALID_SEARCH_QUERY)
                movieQuerySearchResponse
            else MovieSearchResponse(
                page = 1,
                totalResults = 0,
                totalPages = 1,
                results = emptyList()
            )
        }

        if (page > movieSearchResponse.totalPages)
            return MovieSearchResponse(
                page = 1,
                totalResults = 0,
                totalPages = 1,
                results = emptyList()
            )

        return movieSearchResponse


    }

    override suspend fun similarMovies(movieId: Int, page: Int): MovieSearchResponse {
        return when (movieId) {
            TEST_VALID_MOVIE_ID.toInt() -> movieSearchResponse
            TEST_FORCE_THROW_EXCEPTION -> throw Exception("Something went wrong getting results from web.")
            else -> MovieSearchResponse(
                page = 1,
                totalResults = 0,
                totalPages = 1,
                results = emptyList()
            )
        }
    }

    override suspend fun movieRecommendations(movieId: Int, page: Int): MovieSearchResponse {
        return when (movieId) {
            TEST_VALID_MOVIE_ID.toInt() -> movieSearchResponse
            TEST_FORCE_THROW_EXCEPTION -> throw Exception("Something went wrong getting results from web.")
            else -> MovieSearchResponse(
                page = 1,
                totalResults = 0,
                totalPages = 1,
                results = emptyList()
            )
        }
    }

    override suspend fun getMovieDetails(movieId: Long): Movie {
        return when (movieId) {
            TEST_VALID_MOVIE_ID -> movieDetails
            TEST_FORCE_THROW_EXCEPTION.toLong() -> {
                throw Exception("Something went wrong getting results from web.")
            }
            else -> MovieFactory.createMovie(TEST_FORCE_FAIL)
        }
    }

    override suspend fun getMovieVideos(movieId: Int): List<Video> {
        return when (movieId) {
            TEST_VALID_MOVIE_ID.toInt() -> movieTrailers
            TEST_FORCE_THROW_EXCEPTION -> throw Exception("Something went wrong getting results from web.")
            else -> emptyList()
        }
    }

    override suspend fun getMovieImages(movieId: Int): List<Image> {
        return when (movieId) {
            TEST_VALID_MOVIE_ID.toInt() -> movieImages
            TEST_FORCE_THROW_EXCEPTION -> throw Exception("Something went wrong getting results from web.")
            else -> emptyList()
        }
    }

}