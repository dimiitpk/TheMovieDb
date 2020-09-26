package com.dimi.moviedatabase.business.interactors.tv_show

import com.dimi.moviedatabase.business.data.cache.abstraction.TvShowCacheDataSource
import com.dimi.moviedatabase.business.data.network.ApiResponseHandler
import com.dimi.moviedatabase.business.data.network.abstraction.TvShowNetworkDataSource
import com.dimi.moviedatabase.business.data.network.responses.TvShowSearchResponse
import com.dimi.moviedatabase.business.data.util.safeApiCall
import com.dimi.moviedatabase.business.domain.state.*
import com.dimi.moviedatabase.business.interactors.SharedUseCasesKeys.USE_CASE_RECOMMENDED_MEDIA
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import com.dimi.moviedatabase.business.domain.state.ViewState as ViewStateInterface

class TvShowRecommendationsUseCase(
    private val networkDataSource: TvShowNetworkDataSource,
    private val cacheDataSource: TvShowCacheDataSource
) {

    fun <ViewState : ViewStateInterface> getResults(
        viewState: ViewState,
        tvShowId: Long,
        stateEvent: StateEvent
    ) = flow {

        val result = safeApiCall(IO) {
            networkDataSource.getTvShowRecommendations(
                tvShowId, 1
            )
        }

        val request = object : ApiResponseHandler<ViewState, TvShowSearchResponse>(
            response = result,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(responseValue: TvShowSearchResponse?): DataState<ViewState>? {

                responseValue?.let {
                    return if (responseValue.results.isNullOrEmpty())
                        DataState.data(
                            response = Response(
                                message = RETRIEVING_RECOMMENDATION_TV_SHOWS_FAILED,
                                messageType = MessageType.Success,
                                uiComponentType = UIComponentType.None
                            ),
                            data = viewState,
                            stateEvent = stateEvent
                        )
                    else {
                        cacheDataSource.insertTvShows(responseValue.results)
                        viewState.setData(
                            hashMapOf(
                                USE_CASE_RECOMMENDED_MEDIA to responseValue.results
                            )
                        )
                        DataState.data(
                            response = Response(
                                message = RETRIEVING_RECOMMENDATION_TV_SHOWS_SUCCESSFUL,
                                messageType = MessageType.Success,
                                uiComponentType = UIComponentType.None
                            ),
                            data = viewState,
                            stateEvent = stateEvent
                        )
                    }
                } ?: return DataState.data(
                    response = Response(
                        message = RETRIEVING_RECOMMENDATION_TV_SHOWS_FAILED,
                        messageType = MessageType.Success,
                        uiComponentType = UIComponentType.None
                    ),
                    data = viewState,
                    stateEvent = stateEvent
                )
            }

        }.getResult()

        emit(request)
    }

    companion object {
        const val RETRIEVING_RECOMMENDATION_TV_SHOWS_SUCCESSFUL =
            "Successfully retrieved tv shows recommendations."
        const val RETRIEVING_RECOMMENDATION_TV_SHOWS_FAILED =
            "Failed retrieving tv shows recommendations."
    }
}