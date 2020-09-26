package com.dimi.moviedatabase.business.interactors.people

import com.dimi.moviedatabase.business.data.cache.abstraction.PeopleCacheDataSource
import com.dimi.moviedatabase.business.data.network.abstraction.PeopleNetworkDataSource
import com.dimi.moviedatabase.business.data.util.NetworkBoundResource
import com.dimi.moviedatabase.business.domain.model.TvShow
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


class PersonTvShowCastUseCase(
    private val networkDataSource: PeopleNetworkDataSource,
    private val cacheDataSource: PeopleCacheDataSource
) {

    @FlowPreview
    fun <ViewState : ViewStateInterface> getResults(
        viewState: ViewState,
        personId: Long,
        stateEvent: StateEvent
    ) = object : NetworkBoundResource<List<TvShow>, List<TvShow>, ViewState>(
        viewState = viewState,
        dispatcher = IO,
        stateEvent = stateEvent,
        apiCall = {
            networkDataSource.getPersonTvShowCast(
                personId = personId
            )
        },
        cacheCall = {
            cacheDataSource.getPersonTvShowCast(
                personId = personId
            )
        }) {

        override suspend fun updateCache(networkObject: List<TvShow>): Response? {
            withContext(Default) {
                for (tvShow in networkObject) {
                    launch {
                        try {
                            printLogD(
                                "PersonTvShowCast",
                                "updateLocalDb: inserting tvShow: $tvShow"
                            )

                            cacheDataSource.insertTvShowCast(tvShow, personId)

                        } catch (e: Exception) {
                            printLogE(
                                "PersonTvShowCast",
                                "updateLocalDb: error updating cache data on tv show with title: ${tvShow.title}. " + "${e.message}"
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

        override suspend fun setViewState(finalResult: List<TvShow>): Response? {
            println("finalResult: $finalResult")
            viewState.setData(
                hashMapOf(
                    SharedUseCasesKeys.USE_CASE_SIMILAR_MEDIA to finalResult
                )
            )
            return null
        }

    }.result
}