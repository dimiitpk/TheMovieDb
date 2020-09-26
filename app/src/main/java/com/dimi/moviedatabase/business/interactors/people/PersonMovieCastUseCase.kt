package com.dimi.moviedatabase.business.interactors.people

import com.dimi.moviedatabase.business.data.cache.abstraction.PeopleCacheDataSource
import com.dimi.moviedatabase.business.data.network.abstraction.PeopleNetworkDataSource
import com.dimi.moviedatabase.business.data.util.NetworkBoundResource
import com.dimi.moviedatabase.business.domain.model.Movie
import com.dimi.moviedatabase.business.domain.state.*
import com.dimi.moviedatabase.business.interactors.SharedUseCasesKeys
import com.dimi.moviedatabase.util.printLogD
import com.dimi.moviedatabase.util.printLogE
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.FlowPreview
import com.dimi.moviedatabase.business.domain.state.ViewState as ViewStateInterface
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class PersonMovieCastUseCase(
    private val networkDataSource: PeopleNetworkDataSource,
    private val cacheDataSource: PeopleCacheDataSource
) {

    @FlowPreview
    fun <ViewState : ViewStateInterface> getResults(
        viewState: ViewState,
        personId: Long,
        stateEvent: StateEvent
    ) = object : NetworkBoundResource<List<Movie>, List<Movie>, ViewState>(
        viewState = viewState,
        dispatcher = IO,
        stateEvent = stateEvent,
        apiCall = {
            networkDataSource.getPersonMovieCast(
                personId = personId
            )
        },
        cacheCall = {
            cacheDataSource.getPersonMovieCast(
                personId = personId
            )
        }) {

        override suspend fun updateCache(networkObject: List<Movie>): Response? {
            withContext(Default) {
                for (movie in networkObject) {
                    launch {
                        try {
                            printLogD(
                                "PersonMovieCast",
                                "updateLocalDb: inserting movie: $movie"
                            )

                            cacheDataSource.insertMovieCast(movie, personId.toInt())

                        } catch (e: Exception) {
                            printLogE(
                                "PersonMovieCast",
                                "updateLocalDb: error updating cache data on movie with title: ${movie.title}. " + "${e.message}"
                            )
                        }
                    }
                }
            }
            return Response(
                message = "",
                uiComponentType = UIComponentType.None,
                messageType = MessageType.Success
            )
        }

        override suspend fun setViewState(finalResult: List<Movie>): Response? {
            println("finalResult: $finalResult")
            viewState.setData(
                hashMapOf(
                    SharedUseCasesKeys.USE_CASE_RECOMMENDED_MEDIA to finalResult
                )
            )
            return null
        }

    }.result
}