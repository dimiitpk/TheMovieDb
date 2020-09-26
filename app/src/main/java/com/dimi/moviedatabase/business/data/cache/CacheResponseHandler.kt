package com.dimi.moviedatabase.business.data.cache

import com.dimi.moviedatabase.business.data.cache.CacheErrors.CACHE_DATA_NULL
import com.dimi.moviedatabase.business.data.cache.CacheResult
import com.dimi.moviedatabase.business.domain.state.*
import com.dimi.moviedatabase.business.domain.state.ViewState as ViewStateInterface


abstract class CacheResponseHandler <ViewState : ViewStateInterface, Data>(
    private val response: CacheResult<Data?>,
    private val stateEvent: StateEvent?
){
    suspend fun getResult(): DataState<ViewState>? {

        return when(response){

            is CacheResult.GenericError -> {
                DataState.error(
                    response = Response(
                        message = "${stateEvent?.errorInfo()}\n\nReason: ${response.errorMessage}",
                        uiComponentType = UIComponentType.Dialog,
                        messageType = MessageType.Error
                    ),
                    stateEvent = stateEvent
                )
            }

            is CacheResult.Success -> {
                if(response.value == null){
                    DataState.error(
                        response = Response(
                            message = "${stateEvent?.errorInfo()}\n\nReason: $CACHE_DATA_NULL.",
                            uiComponentType = UIComponentType.Dialog,
                            messageType = MessageType.Error
                        ),
                        stateEvent = stateEvent
                    )
                }
                else{
                    handleSuccess(response.value)
                }
            }

        }
    }

    abstract suspend fun handleSuccess(responseValue: Data?): DataState<ViewState>?

}