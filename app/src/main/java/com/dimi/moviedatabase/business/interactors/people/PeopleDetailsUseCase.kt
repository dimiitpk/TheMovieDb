package com.dimi.moviedatabase.business.interactors.people

import com.dimi.moviedatabase.business.data.cache.abstraction.PeopleCacheDataSource
import com.dimi.moviedatabase.business.data.network.abstraction.PeopleNetworkDataSource
import com.dimi.moviedatabase.business.data.util.NetworkBoundResource
import com.dimi.moviedatabase.business.domain.model.Person
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


class PeopleDetailsUseCase(
    private val networkDataSource: PeopleNetworkDataSource,
    private val cacheDataSource: PeopleCacheDataSource
) {

    @FlowPreview
    fun <ViewState : ViewStateInterface> getResults(
        viewState: ViewState,
        personId: Int,
        viewStateKey: String,
        stateEvent: StateEvent
    ) = object : NetworkBoundResource<Person, Person, ViewState>(
        viewState = viewState,
        dispatcher = IO,
        stateEvent = stateEvent,
        apiCall = {
            networkDataSource.getPersonDetails(
                personId = personId
            )
        },
        cacheCall = {
            cacheDataSource.getPersonDetails(
                personId = personId
            )
        }) {

        override suspend fun updateCache(networkObject: Person): Response? {
            withContext(IO) {
                launch {
                    try {
                        printLogD("PersonDetails", "updateLocalDb: inserting person: $networkObject")

                        cacheDataSource.insertPerson(networkObject, true)


                    } catch (e: Exception) {
                        printLogE(
                            "PersonDetails",
                            "updateLocalDb: error updating cache data on person with name: ${networkObject.name}. " + "${e.message}"
                        )
                    }
                }
            }
            return Response(
                message = MESSAGE_DETAILS_QUERY_SUCCESSFUL,
                uiComponentType = UIComponentType.None,
                messageType = MessageType.Success
            )
        }

        override suspend fun setViewState(finalResult: Person): Response? {
            viewState.setData(
                hashMapOf(
                    viewStateKey to finalResult
                )
            )
            return null
        }

    }.result
}