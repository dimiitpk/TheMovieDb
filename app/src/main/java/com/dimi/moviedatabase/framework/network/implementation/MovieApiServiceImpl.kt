package com.dimi.moviedatabase.framework.network.implementation

import com.dimi.moviedatabase.business.data.network.abstraction.MovieNetworkDataSource
import com.dimi.moviedatabase.business.data.network.responses.MovieSearchResponse
import com.dimi.moviedatabase.business.domain.model.*
import com.dimi.moviedatabase.framework.network.NetworkConstants
import com.dimi.moviedatabase.framework.network.api.MovieApi
import com.dimi.moviedatabase.framework.network.mappers.NetworkMapper
import com.dimi.moviedatabase.presentation.main.search.SortFilter
import com.dimi.moviedatabase.presentation.main.search.SortOrder
import com.dimi.moviedatabase.presentation.main.search.enums.MediaListType
import com.dimi.moviedatabase.util.GENRE_DEFAULT
import com.dimi.moviedatabase.util.Keys
import kotlinx.coroutines.FlowPreview

@FlowPreview
class MovieApiServiceImpl(
    private val movieApi: MovieApi,
    private val networkMapper: NetworkMapper
) : MovieNetworkDataSource {

    override suspend fun getListOfMovies(
        query: String,
        page: Int,
        genre: Int,
        mediaListType: MediaListType?,
        sortFilter: SortFilter,
        sortOrder: SortOrder
    ): MovieSearchResponse {

        val request = if (mediaListType != null) {
            when (mediaListType) {
                MediaListType.POPULAR -> movieApi.getPopularMovies(
                    apiKey = Keys.apiKey(),
                    page = page,
                    language = NetworkConstants.LANGUAGE_DEFAULT
                )
                MediaListType.UPCOMING_OR_ON_THE_AIR -> movieApi.getUpcomingMovies(
                    apiKey = Keys.apiKey(),
                    page = page,
                    language = NetworkConstants.LANGUAGE_DEFAULT
                )
                MediaListType.TRENDING -> movieApi.getTrendingMovies(
                    page = page,
                    apiKey = Keys.apiKey(),
                )
                MediaListType.TOP_RATED -> movieApi.getTopRatedMovies(
                    page = page,
                    apiKey = Keys.apiKey(),
                    language = NetworkConstants.LANGUAGE_DEFAULT
                )
            }
        } else {
            if (genre == GENRE_DEFAULT)
                if (query.isBlank())
                    movieApi.discoverMovies(
                        Keys.apiKey(),
                        NetworkConstants.LANGUAGE_DEFAULT,
                        NetworkConstants.SORT_BY_DEFAULT,
                        NetworkConstants.ADULT_DEFAULT,
                        NetworkConstants.VIDEO_DEFAULT,
                        page,
                        null,
                        null
                    )
                else
                    movieApi.searchQuery(
                        Keys.apiKey(),
                        NetworkConstants.LANGUAGE_DEFAULT,
                        page,
                        NetworkConstants.ADULT_DEFAULT,
                        query
                    )
            else
                movieApi.discoverMovies(
                    Keys.apiKey(),
                    NetworkConstants.LANGUAGE_DEFAULT,
                    sortBy = "${sortFilter.value}.${sortOrder.value}",
                    NetworkConstants.ADULT_DEFAULT,
                    NetworkConstants.VIDEO_DEFAULT,
                    page,
                    genre,
                    null
                )
        }
        return networkMapper.movieListNetworkMapper.mapFromEntity(request)
    }

    override suspend fun similarMovies(movieId: Int, page: Int): MovieSearchResponse {
        return networkMapper.movieListNetworkMapper.mapFromEntity(
            movieApi.getSimilarMovies(
                movieId = movieId,
                page = page,
                apiKey = Keys.apiKey()
            )
        )
    }

    override suspend fun movieRecommendations(movieId: Int, page: Int): MovieSearchResponse {
        return networkMapper.movieListNetworkMapper.mapFromEntity(
            movieApi.getMovieRecommendations(
                movieId = movieId,
                page = page,
                apiKey = Keys.apiKey()
            )
        )
    }

    override suspend fun getMovieDetails(movieId: Long): Movie {
        return networkMapper.movieDetailsNetworkMapper.mapFromEntity(
            movieApi.getMovieDetails(
                movieId = movieId,
                language = NetworkConstants.LANGUAGE_DEFAULT,
                apiKey = Keys.apiKey(),
                appendTo = "credits"
            )
        )
    }

    override suspend fun getMovieVideos(movieId: Int): List<Video> {
        return networkMapper.movieDetailsNetworkMapper.toYoutubeTrailersList(
            movieApi.getMovieVideos(
                movieId = movieId,
                apiKey = Keys.apiKey(),
                language = NetworkConstants.LANGUAGE_DEFAULT
            )
        )
    }

    override suspend fun getMovieImages(movieId: Int): List<Image> {
        val response = movieApi.getMovieImages(
            movieId = movieId,
            apiKey = Keys.apiKey(),
            includeImage = NetworkConstants.INCLUDE_IMAGE_DEFAULT
        )
        return networkMapper.imageMapper.mapFromEntityList(
            response.backdrops + response.posters
        )
    }
}