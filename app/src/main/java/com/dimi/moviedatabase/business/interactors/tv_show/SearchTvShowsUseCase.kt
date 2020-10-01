package com.dimi.moviedatabase.business.interactors.tv_show

import com.dimi.moviedatabase.business.data.cache.abstraction.TvShowCacheDataSource
import com.dimi.moviedatabase.business.data.network.NetworkErrors
import com.dimi.moviedatabase.business.data.network.abstraction.TvShowNetworkDataSource
import com.dimi.moviedatabase.business.data.util.NetworkBoundResource
import com.dimi.moviedatabase.business.data.network.responses.TvShowSearchResponse
import com.dimi.moviedatabase.business.domain.model.Network
import com.dimi.moviedatabase.business.domain.model.TvShow
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


class SearchTvShowsUseCase(
    private val networkDataSource: TvShowNetworkDataSource,
    private val cacheDataSource: TvShowCacheDataSource
) {

    @FlowPreview
    fun <ViewState : ViewStateInterface> getResults(
        viewState: ViewState,
        query: String = "",
        page: Int = 1,
        genre: Int = GENRE_DEFAULT,
        mediaListType: MediaListType? = null,
        network: Network? = null,
        viewStateKey: String,
        sortFilter: SortFilter = SortFilter.BY_POPULARITY,
        sortOrder: SortOrder = SortOrder.DESCENDING,
        stateEvent: StateEvent
    ) = object : NetworkBoundResource<TvShowSearchResponse, List<TvShow>, ViewState>(
        viewState = viewState,
        stateEvent = stateEvent,
        dispatcher = IO,
        apiCall = {
            networkDataSource.getListOfTvShows(
                query = query,
                page = page,
                genre = genre,
                mediaListType = mediaListType,
                network = network,
                sortFilter = sortFilter,
                sortOrder = sortOrder
            )
        },
        cacheCall = {
            cacheDataSource.getListOfTvShows(
                query = query,
                page = page,
                genre = genre,
                mediaListType = mediaListType,
                network = network
            )
        }
    ) {

        override suspend fun updateCache(networkObject: TvShowSearchResponse): Response? {

            var message = SEARCH_SUCCESSFUL
            var componentType: UIComponentType = UIComponentType.None
            var messageType: MessageType = MessageType.Success
            val tvShowList: List<TvShow>

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
                    tvShowList = networkObject.results
                    withContext(IO) {
                        for ((index,tvShow) in tvShowList.withIndex()) {
                            try {
                                launch {
                                    printLogD(
                                        "SearchMovies",
                                        "updateLocalDb: inserting movie: $tvShow"
                                    )

                                    cacheDataSource.insertTvShow(
                                        tvShow,
                                        networkId = network?.id,
                                        mediaListType = mediaListType,
                                        order = if (mediaListType != null) (((networkObject.page - 1) * 20) + index + 1) else null
                                    )
                                }
                            } catch (e: Exception) {
                                printLogE(
                                    "SearchTvShows",
                                    "updateLocalDb: error updating cache data on movie with title: ${tvShow.title}. " + "${e.message}"
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

        override suspend fun setViewState(finalResult: List<TvShow>): Response? {
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
        const val SEARCH_SUCCESSFUL = "Successfully retrieved list of tv shows."
        const val SEARCH_FAILED = "Failed to retrieve the list of tv shows."
        const val SEARCH_NO_MATCHING_RESULTS = "There are no tv shows that match query:"
        const val SEARCH_NO_MORE_RESULTS = "There are no more results for that query."
    }
}