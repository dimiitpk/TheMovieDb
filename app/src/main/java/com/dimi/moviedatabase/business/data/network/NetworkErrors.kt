package com.dimi.moviedatabase.business.data.network

import com.dimi.moviedatabase.business.interactors.movie.SearchMoviesUseCase
import kotlinx.coroutines.FlowPreview

object NetworkErrors {

    private const val UNABLE_TO_RESOLVE_HOST = "Unable to resolve host"
    const val UNABLE_TODO_OPERATION_WO_INTERNET = "Can't do that operation without an internet connection"
    const val ERROR_CHECK_NETWORK_CONNECTION = "Check network connection."
    const val NETWORK_ERROR_UNKNOWN = "Unknown network error"
    const val NETWORK_ERROR = "Network error"
    const val NETWORK_ERROR_TIMEOUT = "Network timeout"
    const val NETWORK_DATA_NULL = "Network data is null"


    fun isNetworkError(msg: String): Boolean{
        return when{
            msg.contains(UNABLE_TO_RESOLVE_HOST) -> true
            else-> false
        }
    }


    const val INVALID_PAGE = "Invalid page."

    @FlowPreview
    fun isPaginationDone(errorResponse: String?): Boolean{
        return errorResponse?.contains(SearchMoviesUseCase.SEARCH_NO_MORE_RESULTS)?: false
    }


    @FlowPreview
    fun isThereAnyResults(page: Int, totalPages: Int): Boolean {
        // conditions for no more results per request
        return !( page > 1 && totalPages >= 1 && page > totalPages )
    }
}