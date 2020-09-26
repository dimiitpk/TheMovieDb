package com.dimi.moviedatabase.business.interactors.tv_show

import com.dimi.moviedatabase.business.data.network.ApiResponseHandler
import com.dimi.moviedatabase.business.data.network.abstraction.TvShowNetworkDataSource
import com.dimi.moviedatabase.business.data.util.safeApiCall
import com.dimi.moviedatabase.business.domain.model.Video
import com.dimi.moviedatabase.business.domain.state.*
import com.dimi.moviedatabase.business.interactors.SharedUseCasesKeys.USE_CASE_TRAILERS
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import com.dimi.moviedatabase.business.domain.state.ViewState as ViewStateInterface

class TvShowTrailersUseCase(
    private val networkDataSource: TvShowNetworkDataSource
) {

    fun <ViewState : ViewStateInterface> getResults(
        viewState: ViewState,
        tvShowId: Long,
        stateEvent: StateEvent
    ) = flow {

        val result = safeApiCall(IO) {
            networkDataSource.getTvShowVideos(
                tvShowId
            )
        }

        val request = object : ApiResponseHandler<ViewState, List<Video>>(
            response = result,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(responseValue: List<Video>?): DataState<ViewState>? {

                return if (responseValue.isNullOrEmpty())
                    DataState.data(
                        response = Response(
                            message = RETRIEVING_TRAILERS_FAILED,
                            messageType = MessageType.Success,
                            uiComponentType = UIComponentType.None
                        ),
                        data = viewState,
                        stateEvent = stateEvent
                    )
                else {
                    viewState.setData(
                        hashMapOf(
                            USE_CASE_TRAILERS to responseValue
                        )
                    )
                    DataState.data(
                        response = Response(
                            message = RETRIEVING_TRAILERS_SUCCESSFUL,
                            messageType = MessageType.Success,
                            uiComponentType = UIComponentType.None
                        ),
                        data = viewState,
                        stateEvent = stateEvent
                    )
                }
            }

        }.getResult()

        emit(request)
    }

    companion object {
        const val RETRIEVING_TRAILERS_SUCCESSFUL =
            "Successfully retrieved trailers for this movie."

        const val RETRIEVING_TRAILERS_FAILED =
            "Failed retrieving trailers for this movie."
    }
}