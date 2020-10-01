package com.dimi.moviedatabase.business.interactors.movie

import com.dimi.moviedatabase.business.data.cache.abstraction.MovieCacheDataSource
import com.dimi.moviedatabase.business.data.network.NetworkErrors
import com.dimi.moviedatabase.business.data.network.abstraction.MovieNetworkDataSource
import com.dimi.moviedatabase.business.data.util.NetworkBoundResource
import com.dimi.moviedatabase.business.domain.model.Movie
import com.dimi.moviedatabase.business.data.network.responses.MovieSearchResponse
import com.dimi.moviedatabase.business.domain.state.MessageType
import com.dimi.moviedatabase.business.domain.state.Response
import com.dimi.moviedatabase.business.domain.state.StateEvent
import com.dimi.moviedatabase.business.domain.state.UIComponentType
import com.dimi.moviedatabase.presentation.common.enums.SortFilter
import com.dimi.moviedatabase.presentation.common.enums.SortOrder
import com.dimi.moviedatabase.presentation.main.search.enums.MediaListType
import com.dimi.moviedatabase.util.GENRE_DEFAULT
import com.dimi.moviedatabase.util.printLogD
import com.dimi.moviedatabase.util.printLogE
import com.dimi.moviedatabase.business.domain.state.ViewState as ViewStateInterface

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SearchMoviesUseCase(
    private val networkDataSource: MovieNetworkDataSource,
    private val cacheDataSource: MovieCacheDataSource
) {

    @FlowPreview
    fun <ViewState : ViewStateInterface> getResults(
        viewState: ViewState,
        query: String = "",
        page: Int = 1,
        genre: Int = GENRE_DEFAULT,
        mediaListType: MediaListType? = null,
        viewStateKey: String,
        sortFilter: SortFilter = SortFilter.BY_POPULARITY,
        sortOrder: SortOrder = SortOrder.DESCENDING,
        stateEvent: StateEvent
    ) = object : NetworkBoundResource<MovieSearchResponse, List<Movie>, ViewState>(
        viewState = viewState,
        stateEvent = stateEvent,
        dispatcher = IO,
        apiCall = {
            networkDataSource.getListOfMovies(
                query = query,
                page = page,
                genre = genre,
                mediaListType = mediaListType,
                sortFilter = sortFilter,
                sortOrder = sortOrder
            )
        },
        cacheCall = {
            cacheDataSource.getListOfMovies(
                query = query,
                page = page,
                genre = genre,
                mediaListType = mediaListType
            )
        }
    ) {

        override suspend fun updateCache(networkObject: MovieSearchResponse): Response? {

            var message = SEARCH_SUCCESSFUL
            var componentType: UIComponentType = UIComponentType.None
            var messageType: MessageType = MessageType.Success
            val movieList: List<Movie>

            if (networkObject.totalResults == 0) {
                message = if( query.isNotBlank() )"$SEARCH_NO_MATCHING_RESULTS $query" else SEARCH_FAILED
                componentType = UIComponentType.Toast
                messageType = MessageType.Error
            } else {
                if (NetworkErrors.isThereAnyResults(
                        networkObject.page,
                        networkObject.totalPages
                    ) || networkObject.totalResults == 1
                ) {
                    movieList = networkObject.results
                    withContext(IO) {
                        for ((index, movie) in movieList.withIndex()) {
                            try {
                                launch {
                                    printLogD(
                                        "SearchMovies",
                                        "updateLocalDb: inserting movie: $movie"
                                    )
                                    cacheDataSource.insertMovie(
                                        movie = movie,
                                        mediaListType = mediaListType,
                                        order = if (mediaListType != null) (((networkObject.page - 1) * 20) + index + 1) else null
                                    )
                                }
                            } catch (e: Exception) {
                                printLogE(
                                    "SearchMovies",
                                    "updateLocalDb: error updating cache data on movie with title: ${movie.title}. " + "${e.message}"
                                )
                            }
                        }
                    }
                } else {
                    message = SEARCH_NO_MORE_RESULTS
                    componentType = UIComponentType.Toast
                    messageType = MessageType.Error
                }
            }

            return Response(
                message = message,
                uiComponentType = componentType,
                messageType = messageType
            )
        }

        override suspend fun setViewState(finalResult: List<Movie>): Response? {
            return if (finalResult.isNullOrEmpty()) {
                Response(
                    message = if( query.isNotBlank() )"$SEARCH_NO_MATCHING_RESULTS $query" else SEARCH_FAILED,
                    uiComponentType = UIComponentType.Toast,
                    messageType = MessageType.Error
                )
            } else {
                viewState.setData(
                    hashMapOf(
                        viewStateKey to finalResult
                    )
                )
                null
            }
        }
    }.result

    companion object {
        const val SEARCH_SUCCESSFUL = "Successfully retrieved list of movies."
        const val SEARCH_FAILED = "Failed to retrieve the list of movies."
        const val SEARCH_NO_MATCHING_RESULTS = "There are no movies that match query:"
        const val SEARCH_NO_MORE_RESULTS = "There are no more results for that query."
    }
}