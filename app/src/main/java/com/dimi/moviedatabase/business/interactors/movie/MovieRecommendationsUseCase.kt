package com.dimi.moviedatabase.business.interactors.movie

import com.dimi.moviedatabase.business.data.cache.abstraction.MovieCacheDataSource
import com.dimi.moviedatabase.business.data.network.ApiResponseHandler
import com.dimi.moviedatabase.business.data.network.abstraction.MovieNetworkDataSource
import com.dimi.moviedatabase.business.data.network.responses.MovieSearchResponse
import com.dimi.moviedatabase.business.data.util.safeApiCall
import com.dimi.moviedatabase.business.domain.state.*
import com.dimi.moviedatabase.business.interactors.SharedUseCasesKeys.USE_CASE_RECOMMENDED_MEDIA
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import com.dimi.moviedatabase.business.domain.state.ViewState as ViewStateInterface

class MovieRecommendationsUseCase(
    private val networkDataSource: MovieNetworkDataSource,
    private val cacheDataSource: MovieCacheDataSource
) {

    fun <ViewState : ViewStateInterface> getResults(
        viewState: ViewState,
        movieId: Int,
        stateEvent: StateEvent
    ) = flow {

        val result = safeApiCall(IO) {
            networkDataSource.movieRecommendations(
                movieId, 1
            )
        }

        val request = object : ApiResponseHandler<ViewState, MovieSearchResponse>(
            response = result,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(responseValue: MovieSearchResponse?): DataState<ViewState>? {

                responseValue?.let {
                    return if (responseValue.results.isNullOrEmpty())
                        DataState.data(
                            response = Response(
                                message = RETRIEVING_RECOMMEND_MOVIES_FAILED,
                                messageType = MessageType.Success,
                                uiComponentType = UIComponentType.None
                            ),
                            data = viewState,
                            stateEvent = stateEvent
                        )
                    else {
                        cacheDataSource.insertMovies(responseValue.results)
                        viewState.setData(
                            hashMapOf(
                                USE_CASE_RECOMMENDED_MEDIA to responseValue.results
                            )
                        )
                        DataState.data(
                            response = Response(
                                message = USE_CASE_RECOMMENDED_MEDIA,
                                messageType = MessageType.Success,
                                uiComponentType = UIComponentType.None
                            ),
                            data = viewState,
                            stateEvent = stateEvent
                        )
                    }
                } ?: return DataState.data(
                    response = Response(
                        message = RETRIEVING_RECOMMEND_MOVIES_FAILED,
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
        const val RETRIEVING_RECOMMEND_MOVIES_FAILED =
            "Failed retrieving recommended movies."
    }
}