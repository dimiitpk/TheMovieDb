package com.dimi.moviedatabase.business.interactors.tv_show

import com.dimi.moviedatabase.business.data.cache.abstraction.TvShowCacheDataSource
import com.dimi.moviedatabase.business.data.network.abstraction.TvShowNetworkDataSource
import com.dimi.moviedatabase.business.data.util.NetworkBoundResource
import com.dimi.moviedatabase.business.domain.model.Episode
import com.dimi.moviedatabase.business.domain.model.Season
import com.dimi.moviedatabase.business.domain.state.*
import com.dimi.moviedatabase.util.printLogD
import com.dimi.moviedatabase.util.printLogE
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.FlowPreview
import com.dimi.moviedatabase.business.domain.state.ViewState as ViewStateInterface
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class TvShowEpisodesUseCase(
    private val networkDataSource: TvShowNetworkDataSource,
    private val cacheDataSource: TvShowCacheDataSource
) {

    @FlowPreview
    fun <ViewState : ViewStateInterface> getResults(
        viewState: ViewState,
        tvShowId: Long,
        season: Season,
        viewStateKey: String,
        stateEvent: StateEvent
    ) = object : NetworkBoundResource<List<Episode>, List<Episode>, ViewState>(
        viewState = viewState,
        dispatcher = IO,
        stateEvent = stateEvent,
        apiCall = {
            networkDataSource.getEpisodesPerSeason(
                tvShowId = tvShowId,
                season = season.seasonNumber
            )
        },
        cacheCall = {
            cacheDataSource.getEpisodesPerSeason(
                tvShowId = tvShowId,
                season = season
            )
        }) {

        override suspend fun updateCache(networkObject: List<Episode>): Response? {
            withContext(IO) {
                networkObject.let { episodes ->
                    if (episodes.isNotEmpty()) {
                        for (episode in episodes) {
                            try {
                                launch {
                                    printLogD(
                                        "TvShowEpisodesUseCase",
                                        "updateLocalDb: inserting episode: $episode"
                                    )
                                    cacheDataSource.insertEpisode(episode, season)
                                }

                            } catch (e: Exception) {
                                printLogE(
                                    "TvShowEpisodesUseCase",
                                    "updateLocalDb: error updating cache data on episode with name: ${episode.name}. " + "${e.message}"
                                )
                            }
                        }
                    }
                }
            }
            return Response(
                message = RETRIEVING_TV_SHOWS_EPISODES_SUCCESSFUL,
                uiComponentType = UIComponentType.None,
                messageType = MessageType.Success
            )
        }

        override suspend fun setViewState(finalResult: List<Episode>): Response? {
            viewState.setData(
                hashMapOf(
                    viewStateKey to finalResult
                )
            )
            return null
        }

    }.result

    companion object {
        const val RETRIEVING_TV_SHOWS_EPISODES_SUCCESSFUL = "Successfully retrieved list of episodes."
    }
}