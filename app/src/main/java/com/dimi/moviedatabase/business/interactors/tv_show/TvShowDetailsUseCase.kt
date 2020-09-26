package com.dimi.moviedatabase.business.interactors.tv_show

import com.dimi.moviedatabase.business.data.cache.abstraction.TvShowCacheDataSource
import com.dimi.moviedatabase.business.data.network.abstraction.TvShowNetworkDataSource
import com.dimi.moviedatabase.business.data.util.NetworkBoundResource
import com.dimi.moviedatabase.business.domain.model.TvShow
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


class TvShowDetailsUseCase(
    private val networkDataSource: TvShowNetworkDataSource,
    private val cacheDataSource: TvShowCacheDataSource
) {

    @FlowPreview
    fun <ViewState : ViewStateInterface> getResults(
        viewState: ViewState,
        tvShowId: Long,
        viewStateKey: String,
        stateEvent: StateEvent
    ) = object : NetworkBoundResource<TvShow, TvShow, ViewState>(
        viewState = viewState,
        dispatcher = IO,
        stateEvent = stateEvent,
        apiCall = {
            networkDataSource.getTvShowDetails(
                tvShowId = tvShowId
            )
        },
        cacheCall = {
            cacheDataSource.getTvShowDetails(
                tvShowId = tvShowId
            )
        }) {

        override suspend fun updateCache(networkObject: TvShow): Response? {
            withContext(IO) {
                launch {
                    try {
                        printLogD(
                            "TvShowDetails",
                            "updateLocalDb: inserting tv show: $networkObject"
                        )

                        cacheDataSource.insertTvShow(networkObject, upsert = true)

                        networkObject.castList?.let { castList ->
                            if (castList.isNotEmpty()) {
                                for (cast in castList) {
                                    try {
                                        launch {
                                            printLogD(
                                                "TvShowDetails",
                                                "updateLocalDb: inserting cast: $cast"
                                            )
                                            cacheDataSource.insertCast(cast, networkObject.id)
                                        }

                                    } catch (e: Exception) {
                                        printLogE(
                                            "TvShowDetails",
                                            "updateLocalDb: error updating cache data on cast with name: ${cast.name}. " + "${e.message}"
                                        )
                                    }
                                }
                            }
                        }
                        networkObject.networks?.let { networks ->
                            if (networks.isNotEmpty()) {
                                for (network in networks) {
                                    try {
                                        launch {
                                            printLogD(
                                                "TvShowDetails",
                                                "updateLocalDb: inserting network: $network"
                                            )
                                            cacheDataSource.insertNetwork(network, networkObject.id)
                                        }

                                    } catch (e: Exception) {
                                        printLogE(
                                            "TvShowDetails",
                                            "updateLocalDb: error updating cache data on network with name: ${network.name}. " + "${e.message}"
                                        )
                                    }
                                }
                            }
                        }
                        networkObject.seasons?.let { seasons ->
                            if (seasons.isNotEmpty()) {
                                for ( season in seasons) {
                                    try {
                                        launch {
                                            printLogD(
                                                "TvShowDetails",
                                                "updateLocalDb: inserting season: $season"
                                            )
                                            cacheDataSource.insertSeason(season, networkObject.id)
                                        }

                                    } catch (e: Exception) {
                                        printLogE(
                                            "TvShowDetails",
                                            "updateLocalDb: error updating cache data on season with name: ${season.seasonName}. " + "${e.message}"
                                        )
                                    }
                                }
                            }
                        }

                    } catch (e: Exception) {
                        printLogE(
                            "TvShowDetails",
                            "updateLocalDb: error updating cache data on tv show with title: ${networkObject.title}. " + "${e.message}"
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

        override suspend fun setViewState(finalResult: TvShow): Response? {
            viewState.setData(
                hashMapOf(
                    viewStateKey to finalResult
                )
            )
            return null
        }

    }.result
}