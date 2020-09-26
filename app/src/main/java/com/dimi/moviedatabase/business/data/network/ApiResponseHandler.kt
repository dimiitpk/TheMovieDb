package com.dimi.moviedatabase.business.data.network

import com.dimi.moviedatabase.business.data.network.ApiResult.*
import com.dimi.moviedatabase.business.data.network.NetworkErrors.NETWORK_DATA_NULL
import com.dimi.moviedatabase.business.data.network.NetworkErrors.NETWORK_ERROR
import com.dimi.moviedatabase.business.domain.state.*
import com.dimi.moviedatabase.business.domain.state.ViewState as ViewStateInterface


abstract class ApiResponseHandler <ViewState : ViewStateInterface, Data>(
    private val response: ApiResult<Data?>,
    private val stateEvent: StateEvent?
){

    suspend fun getResult(): DataState<ViewState>? {

        return when(response){

            is GenericError -> {
                DataState.error(
                    response = Response(
                        message = "${stateEvent?.errorInfo()}\n\nReason: ${response.errorMessage.toString()}",
                        uiComponentType = UIComponentType.Dialog,
                        messageType = MessageType.Error
                    ),
                    stateEvent = stateEvent
                )
            }

            is NetworkError -> {
                DataState.error(
                    response = Response(
                        message = "${stateEvent?.errorInfo()}\n\nReason: $NETWORK_ERROR",
                        uiComponentType = UIComponentType.Dialog,
                        messageType = MessageType.Error
                    ),
                    stateEvent = stateEvent
                )
            }

            is Success -> {
                if(response.value == null){
                    DataState.error(
                        response = Response(
                            message = "${stateEvent?.errorInfo()}\n\nReason: $NETWORK_DATA_NULL.",
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