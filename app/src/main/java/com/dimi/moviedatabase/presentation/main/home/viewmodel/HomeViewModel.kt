package com.dimi.moviedatabase.presentation.main.home.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.dimi.moviedatabase.business.domain.state.*
import com.dimi.moviedatabase.business.interactors.movie.MovieUseCases
import com.dimi.moviedatabase.business.interactors.tv_show.TvShowUseCases
import com.dimi.moviedatabase.presentation.common.BaseViewModel
import com.dimi.moviedatabase.presentation.main.home.state.HomeStateEvent.*
import com.dimi.moviedatabase.presentation.main.home.state.HomeViewState
import com.dimi.moviedatabase.presentation.main.search.enums.MediaListType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
@FlowPreview
class HomeViewModel
@ViewModelInject
constructor(
    private val movieUseCases: MovieUseCases,
    private val tvShowUseCases: TvShowUseCases,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel<HomeViewState>(savedStateHandle) {

    override fun handleNewData(data: HomeViewState) {
        data.let { viewState ->
            viewState.popularMovies?.let {
                setPopularMoviesList(it)
            }
            viewState.topRatedMovies?.let {
                setTopRatedMovies(it)
            }
            viewState.popularTvShows?.let {
                setPopularTvShows(it)
            }
            viewState.topRatedTvShows?.let {
                setTopRatedTvShows(it)
            }
            viewState.upcomingMovies?.let {
                setUpcomingMovies(it)
            }
            viewState.onTheAirTvShows?.let {
                setOnTheAirTvShows(it)
            }
            viewState.trendingMovies?.let {
                setTrendingMovies(it)
            }
            viewState.trendingTvShows?.let {
                setTrendingTvShows(it)
            }
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {

        val job: Flow<DataState<HomeViewState>?> = when (stateEvent) {

            is PopularMovies -> {
                movieUseCases.searchMoviesUseCase.getResults(
                    viewState = HomeViewState(),
                    viewStateKey = MediaListType.POPULAR.name.plus(MediaType.MOVIE.name),
                    stateEvent = stateEvent,
                    mediaListType = MediaListType.POPULAR
                )
            }
            is TopRatedMovies -> {
                movieUseCases.searchMoviesUseCase.getResults(
                    viewState = HomeViewState(),
                    viewStateKey = MediaListType.TOP_RATED.name.plus(MediaType.MOVIE.name),
                    stateEvent = stateEvent,
                    mediaListType = MediaListType.TOP_RATED
                )
            }
            is TrendingMovies -> {
                movieUseCases.searchMoviesUseCase.getResults(
                    viewState = HomeViewState(),
                    viewStateKey = MediaListType.TRENDING.name.plus(MediaType.MOVIE.name),
                    stateEvent = stateEvent,
                    mediaListType = MediaListType.TRENDING
                )
            }
            is UpcomingMovies -> {
                movieUseCases.searchMoviesUseCase.getResults(
                    viewState = HomeViewState(),
                    viewStateKey = MediaListType.UPCOMING_OR_ON_THE_AIR.name.plus(MediaType.MOVIE.name),
                    stateEvent = stateEvent,
                    mediaListType = MediaListType.UPCOMING_OR_ON_THE_AIR
                )
            }
            is TrendingTvShows -> {
                tvShowUseCases.searchTvShowsUseCase.getResults(
                    viewState = HomeViewState(),
                    mediaListType = MediaListType.TRENDING,
                    stateEvent = stateEvent,
                    viewStateKey = MediaListType.TRENDING.name.plus(MediaType.TV_SHOW.name),
                )
            }
            is PopularTvShows -> {
                tvShowUseCases.searchTvShowsUseCase.getResults(
                    viewState = HomeViewState(),
                    mediaListType = MediaListType.POPULAR,
                    stateEvent = stateEvent,
                    viewStateKey = MediaListType.POPULAR.name.plus(MediaType.TV_SHOW.name),
                )
            }
            is TopRatedTvShows -> {
                tvShowUseCases.searchTvShowsUseCase.getResults(
                    viewState = HomeViewState(),
                    mediaListType = MediaListType.TOP_RATED,
                    stateEvent = stateEvent,
                    viewStateKey = MediaListType.TOP_RATED.name.plus(MediaType.TV_SHOW.name),
                )
            }
            is OnTheAirTvShows -> {
                tvShowUseCases.searchTvShowsUseCase.getResults(
                    viewState = HomeViewState(),
                    mediaListType = MediaListType.UPCOMING_OR_ON_THE_AIR,
                    stateEvent = stateEvent,
                    viewStateKey = MediaListType.UPCOMING_OR_ON_THE_AIR.name.plus(MediaType.TV_SHOW.name),
                )
            }
            is CreateStateMessageEvent -> {
                emitStateMessageEvent(
                    stateMessage = stateEvent.stateMessage,
                    stateEvent = stateEvent
                )
            }

            else -> {
                emitInvalidStateEvent(stateEvent)
            }
        }
        launchJob(stateEvent, job)
    }


    override fun initNewViewState() = HomeViewState()


    override fun getViewStateCopyWithoutBigLists(viewState: HomeViewState): HomeViewState {
        return viewState.copy(
            popularMovies = emptyList(),
            popularTvShows = emptyList(),
            trendingMovies = emptyList(),
            trendingTvShows = emptyList(),
            topRatedMovies = emptyList(),
            topRatedTvShows = emptyList(),
            upcomingMovies = emptyList(),
            onTheAirTvShows = emptyList()
        )
    }

    override fun getUniqueViewStateIdentifier(): String {
        return HomeViewState.getBundleKey()
    }
}