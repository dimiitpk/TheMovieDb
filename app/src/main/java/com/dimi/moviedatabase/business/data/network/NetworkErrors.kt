package com.dimi.moviedatabase.business.data.network

import com.dimi.moviedatabase.business.interactors.movie.SearchMoviesUseCase
import kotlinx.coroutines.FlowPreview

object NetworkErrors {

    const val NETWORK_ERROR_UNKNOWN = "Unknown network error"
    const val NETWORK_ERROR = "Network error"
    const val NETWORK_ERROR_TIMEOUT = "Network timeout"
    const val NETWORK_DATA_NULL = "Network data is null"


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