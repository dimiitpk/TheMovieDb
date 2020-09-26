package com.dimi.moviedatabase.business.interactors.people

import com.dimi.moviedatabase.business.data.cache.abstraction.PeopleCacheDataSource
import com.dimi.moviedatabase.business.data.network.NetworkErrors
import com.dimi.moviedatabase.business.data.network.abstraction.PeopleNetworkDataSource
import com.dimi.moviedatabase.business.data.util.NetworkBoundResource
import com.dimi.moviedatabase.business.data.network.responses.PeopleSearchResponse
import com.dimi.moviedatabase.business.domain.model.Person
import com.dimi.moviedatabase.business.domain.state.MessageType
import com.dimi.moviedatabase.business.domain.state.Response
import com.dimi.moviedatabase.business.domain.state.StateEvent
import com.dimi.moviedatabase.business.domain.state.UIComponentType
import com.dimi.moviedatabase.util.printLogD
import com.dimi.moviedatabase.util.printLogE
import com.dimi.moviedatabase.business.domain.state.ViewState as ViewStateInterface

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SearchPeopleUseCase(
    private val networkDataSource: PeopleNetworkDataSource,
    private val cacheDataSource: PeopleCacheDataSource
) {

    @FlowPreview
    fun <ViewState : ViewStateInterface> getResults(
        viewState: ViewState,
        query: String = "",
        page: Int = 1,
        viewStateKey: String,
        stateEvent: StateEvent
    ) = object : NetworkBoundResource<PeopleSearchResponse, List<Person>, ViewState>(
        viewState = viewState,
        stateEvent = stateEvent,
        dispatcher = IO,
        apiCall = {
            networkDataSource.searchPeoples(
                query = query,
                page = page
            )
        },
        cacheCall = {
            cacheDataSource.searchPeoples(
                query = query,
                page = page
            )
        }
    ) {

        override suspend fun updateCache(networkObject: PeopleSearchResponse): Response? {

            var message = SEARCH_SUCCESSFUL
            var componentType: UIComponentType = UIComponentType.None
            var messageType: MessageType = MessageType.Success
            val peoples: List<Person>

            if (networkObject.totalResults == 0) {
                message = "$SEARCH_NO_MATCHING_RESULTS $query"
                componentType = UIComponentType.Toast
                messageType = MessageType.Error
            } else {
                if (NetworkErrors.isThereAnyResults(
                        networkObject.page,
                        networkObject.totalPages
                    ) || networkObject.totalResults == 1
                ) {
                    peoples = networkObject.results
                    withContext(IO) {
                        for (person in peoples) {
                            try {
                                launch {
                                    printLogD(
                                        "SearchPeoples",
                                        "updateLocalDb: inserting person: $person"
                                    )
                                    cacheDataSource.insertPerson(
                                        person
                                    )
                                }
                            } catch (e: Exception) {
                                printLogE(
                                    "SearchPeoples",
                                    "updateLocalDb: error updating cache data on person with name: ${person.name}. " + "${e.message}"
                                )
                            }
                        }
                    }
                } else {
                    message = SEARCH_NO_MORE_RESULTS
                    componentType = UIComponentType.Toast
                    messageType = MessageType.Error
                }
            }

            return Response(
                message = message,
                uiComponentType = componentType,
                messageType = messageType
            )
        }

        override suspend fun setViewState(finalResult: List<Person>): Response? {
            return if (finalResult.isNullOrEmpty()) {
                Response(
                    message = "$SEARCH_NO_MATCHING_RESULTS $query",
                    uiComponentType = UIComponentType.Toast,
                    messageType = MessageType.Error
                )
            } else {
                viewState.setData(
                    hashMapOf(
                        viewStateKey to finalResult
                    )
                )
                null
            }
        }
    }.result

    companion object {
        const val SEARCH_SUCCESSFUL = "Successfully retrieved list of peoples."
        const val SEARCH_FAILED = "Failed to retrieve the list of peoples."
        const val SEARCH_NO_MATCHING_RESULTS = "There are no peoples that match query:"
        const val SEARCH_NO_MORE_RESULTS = "There are no more results for that query."
    }
}