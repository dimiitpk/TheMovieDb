package com.dimi.moviedatabase.business.interactors.movie

import com.dimi.moviedatabase.business.data.cache.abstraction.MovieCacheDataSource
import com.dimi.moviedatabase.business.data.network.abstraction.MovieNetworkDataSource
import com.dimi.moviedatabase.business.data.util.NetworkBoundResource
import com.dimi.moviedatabase.business.domain.model.Movie
import com.dimi.moviedatabase.business.domain.state.*
import com.dimi.moviedatabase.business.interactors.SharedUseCasesKeys.MESSAGE_DETAILS_QUERY_SUCCESSFUL
import com.dimi.moviedatabase.util.printLogD
import com.dimi.moviedatabase.util.printLogE
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.FlowPreview
import com.dimi.moviedatabase.business.domain.state.ViewState as ViewStateInterface
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class MovieDetailsUseCase(
    private val networkDataSource: MovieNetworkDataSource,
    private val cacheDataSource: MovieCacheDataSource
) {

    @FlowPreview
    fun <ViewState : ViewStateInterface> getResults(
        viewState: ViewState,
        movieId: Long,
        viewStateKey: String,
        stateEvent: StateEvent
    ) = object : NetworkBoundResource<Movie, Movie, ViewState>(
        viewState = viewState,
        dispatcher = IO,
        stateEvent = stateEvent,
        apiCall = {
            networkDataSource.getMovieDetails(
                movieId = movieId
            )
        },
        cacheCall = {
            cacheDataSource.getMovie(
                movieId = movieId
            )
        }) {

        override suspend fun updateCache(networkObject: Movie): Response? {
            var message = MESSAGE_DETAILS_QUERY_SUCCESSFUL
            if (networkObject.id == -1L)
                message = DETAILS_FAILED
            withContext(IO) {
                launch {
                    try {
                        printLogD("MovieDetails", "updateLocalDb: inserting movie: $networkObject")

                        cacheDataSource.insertMovie(networkObject, true)

                        networkObject.castList?.let { castList ->
                            if (castList.isNotEmpty()) {
                                for (cast in castList) {
                                    try {
                                        launch {
                                            printLogD(
                                                "MovieDetails",
                                                "updateLocalDb: inserting cast: $cast"
                                            )
                                            cacheDataSource.insertCast(
                                                person = cast,
                                                movieId = networkObject.id
                                            )
                                        }

                                    } catch (e: Exception) {
                                        printLogE(
                                            "MovieDetails",
                                            "updateLocalDb: error updating cache data on cast with name: ${cast.name}. " + "${e.message}"
                                        )
                                    }
                                }
                            }

                        }

                    } catch (e: Exception) {
                        printLogE(
                            "MovieDetails",
                            "updateLocalDb: error updating cache data on movie with title: ${networkObject.title}. " + "${e.message}"
                        )
                    }
                }
            }
            return Response(
                message = message,
                uiComponentType = UIComponentType.None,
                messageType = MessageType.Success
            )
        }

        override suspend fun setViewState(finalResult: Movie): Response? {
            viewState.setData(
                hashMapOf(
                    viewStateKey to finalResult
                )
            )
            return null
        }

    }.result

    companion object {
        const val DETAILS_FAILED = "Failed to retrieve movie details."
    }
}