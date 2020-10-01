package com.dimi.moviedatabase.business.data.util

import com.dimi.moviedatabase.business.data.cache.CacheResponseHandler
import com.dimi.moviedatabase.business.data.network.ApiResult
import com.dimi.moviedatabase.business.data.network.NetworkErrors.NETWORK_ERROR
import com.dimi.moviedatabase.business.data.util.GenericErrors.ERROR_UNKNOWN
import com.dimi.moviedatabase.business.domain.state.DataState
import com.dimi.moviedatabase.business.domain.state.Response
import com.dimi.moviedatabase.business.domain.state.StateEvent
import com.dimi.moviedatabase.business.domain.state.UIComponentType
import com.dimi.moviedatabase.business.domain.state.ViewState as ViewStateInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


@FlowPreview
abstract class NetworkBoundResource<NetworkResponse, CacheResponse, ViewState : ViewStateInterface>(
    private val dispatcher: CoroutineDispatcher,
    private val viewState: ViewState,
    private val stateEvent: StateEvent,
    private val apiCall: suspend () -> NetworkResponse?,
    private val cacheCall: suspend () -> CacheResponse?
) {

    val result: Flow<DataState<ViewState>?> = flow {

        emit(returnCache(markJobComplete = false, cacheResponse = null))

        val apiResult = safeApiCall(dispatcher) { apiCall.invoke() }

        var response: Response? = null

        when (apiResult) {
            is ApiResult.GenericError -> {
                emit(
                    buildError<ViewState>(
                        apiResult.errorMessage ?: ERROR_UNKNOWN,
                        UIComponentType.Dialog,
                        stateEvent
                    )
                )
            }

            is ApiResult.NetworkError -> {
                emit(
                    buildError<ViewState>(
                        NETWORK_ERROR,
                        UIComponentType.Dialog,
                        stateEvent
                    )
                )
            }

            is ApiResult.Success -> {
                if (apiResult.value == null) {
                    emit(
                        buildError<ViewState>(
                            ERROR_UNKNOWN,
                            UIComponentType.Dialog,
                            stateEvent
                        )
                    )
                } else {
                    response = updateCache(apiResult.value)
                }
            }
        }

        emit(returnCache(markJobComplete = true, cacheResponse = response))
    }

    private suspend fun returnCache(
        markJobComplete: Boolean,
        cacheResponse: Response?
    ): DataState<ViewState>? {

        val cacheResult = safeCacheCall(dispatcher) { cacheCall.invoke() }



        var jobCompleteMarker: StateEvent? = null
        if (markJobComplete) {
            jobCompleteMarker = stateEvent
        }

        return object : CacheResponseHandler<ViewState, CacheResponse>(
            response = cacheResult,
            stateEvent = jobCompleteMarker
        ) {
            override suspend fun handleSuccess(responseValue: CacheResponse?): DataState<ViewState> {

                var finalResponse : Response? = cacheResponse
                if( responseValue != null )
                    finalResponse = setViewState(responseValue) ?: cacheResponse
                if( jobCompleteMarker == null ) finalResponse = cacheResponse

                return DataState.data(
                    response = finalResponse,
                    data = viewState,
                    stateEvent = jobCompleteMarker
                )
            }
        }.getResult()

    }

    abstract suspend fun setViewState(finalResult: CacheResponse): Response?

    abstract suspend fun updateCache(networkObject: NetworkResponse): Response?
}















