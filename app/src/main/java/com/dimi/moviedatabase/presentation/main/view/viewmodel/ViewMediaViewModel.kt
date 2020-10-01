package com.dimi.moviedatabase.presentation.main.view.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.dimi.moviedatabase.business.domain.state.*
import com.dimi.moviedatabase.business.interactors.movie.MovieUseCases
import com.dimi.moviedatabase.business.interactors.people.PeopleUseCases
import com.dimi.moviedatabase.business.interactors.tv_show.TvShowUseCases
import com.dimi.moviedatabase.presentation.common.BaseViewModel
import com.dimi.moviedatabase.presentation.common.VisibilityState
import com.dimi.moviedatabase.presentation.common.VisibilityState.*
import com.dimi.moviedatabase.presentation.main.view.state.ViewMediaViewState
import com.dimi.moviedatabase.presentation.main.view.state.ViewMediaStateEvent.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
@FlowPreview
class ViewMediaViewModel
@ViewModelInject
constructor(
    private val movieUseCases: MovieUseCases,
    private val tvShowUseCases: TvShowUseCases,
    private val peopleUseCases: PeopleUseCases,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel<ViewMediaViewState>(savedStateHandle) {

    private val _trailerPlayerState: MutableLiveData<VisibilityState> = MutableLiveData(Hidden)
    private val _videosPlayerState: MutableLiveData<VisibilityState> = MutableLiveData(Hidden)

    init {
        println("ViewMediaViewModel -> ${ViewMediaViewState.getBundleKey()}")
    }

    override fun handleNewData(data: ViewMediaViewState) {
        data.let { viewState ->
            viewState.media?.let { movie ->
                setViewItem(movie)
            }
            viewState.trailers?.let { list ->
                setTrailers(list)
            }
            viewState.backdrops?.let { list ->
                setBackDrops(list)
            }
            viewState.posters?.let { list ->
                setPosters(list)
            }
            viewState.similarMedia?.let { list ->
                setSimilarMedia(list)
            }
            viewState.recommendedMedia?.let { list ->
                setRecommendedMedia(list)
            }
            viewState.seasonEpisodes?.let { list ->
                setSeasonEpisodes(list)
            }
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {

        val job: Flow<DataState<ViewMediaViewState>?> = when (stateEvent) {
            is GetMovieTrailers -> {

                movieUseCases.movieTrailersUseCase.getResults(
                    viewState = ViewMediaViewState(),
                    movieId = stateEvent.mediaId.toInt(),
                    stateEvent = stateEvent
                )
            }
            is GetImages -> {
                when (stateEvent.mediaType) {
                    MediaType.MOVIE -> movieUseCases.movieImagesUseCase.getResults(
                        viewState = ViewMediaViewState(),
                        movieId = stateEvent.mediaId.toInt(),
                        stateEvent = stateEvent
                    )
                    MediaType.TV_SHOW -> tvShowUseCases.tvShowImagesUseCase.getResults(
                        viewState = ViewMediaViewState(),
                        tvShowId = stateEvent.mediaId,
                        stateEvent = stateEvent
                    )
                    MediaType.PERSON -> peopleUseCases.personImagesUseCase.getResults(
                        viewState = ViewMediaViewState(),
                        personId = stateEvent.mediaId.toInt(),
                        stateEvent = stateEvent
                    )
                }
            }
            is GetPersonMovieCast -> {
                peopleUseCases.personMovieCastUseCase.getResults(
                    viewState = ViewMediaViewState(),
                    personId = stateEvent.mediaId,
                    stateEvent = stateEvent
                )
            }
            is GetPersonTvShowCast -> {
                peopleUseCases.personTvShowCastUseCase.getResults(
                    viewState = ViewMediaViewState(),
                    personId = stateEvent.mediaId,
                    stateEvent = stateEvent
                )
            }
            is GetTvShowTrailers -> {
                tvShowUseCases.tvShowTrailersUseCase.getResults(
                    viewState = ViewMediaViewState(),
                    tvShowId = stateEvent.mediaId,
                    stateEvent = stateEvent
                )
            }
            is GetDetails -> {
                when (stateEvent.mediaType) {
                    MediaType.MOVIE -> movieUseCases.movieDetailsUseCase.getResults(
                        viewState = ViewMediaViewState(),
                        movieId = stateEvent.mediaId,
                        stateEvent = stateEvent,
                        viewStateKey = ViewMediaViewState.MEDIA_DETAILS
                    )
                    MediaType.TV_SHOW -> tvShowUseCases.tvShowDetailsUseCase.getResults(
                        viewState = ViewMediaViewState(),
                        tvShowId = stateEvent.mediaId,
                        stateEvent = stateEvent,
                        viewStateKey = ViewMediaViewState.MEDIA_DETAILS
                    )
                    MediaType.PERSON -> peopleUseCases.peopleDetailsUseCase.getResults(
                        viewState = ViewMediaViewState(),
                        personId = stateEvent.mediaId.toInt(),
                        stateEvent = stateEvent,
                        viewStateKey = ViewMediaViewState.MEDIA_DETAILS
                    )
                }
            }
            is GetSimilarTvShows -> {
                tvShowUseCases.tvSimilarTvShowsUseCase.getResults(
                    viewState = ViewMediaViewState(),
                    tvShowId = stateEvent.mediaId,
                    stateEvent = stateEvent
                )
            }
            is GetTvShowRecommendations -> {
                tvShowUseCases.tvShowRecommendationsUseCase.getResults(
                    viewState = ViewMediaViewState(),
                    tvShowId = stateEvent.mediaId,
                    stateEvent = stateEvent
                )
            }
            is GetEpisodesPerSeason -> {
                tvShowUseCases.tvShowEpisodesUseCase.getResults(
                    viewState = ViewMediaViewState(),
                    tvShowId = stateEvent.mediaId,
                    season = stateEvent.season,
                    stateEvent = stateEvent,
                    viewStateKey = ViewMediaViewState.TV_SHOW_EPISODES
                )
            }
            is GetSimilarMovies -> {
                movieUseCases.similarMoviesUseCase.getResults(
                    viewState = ViewMediaViewState(),
                    movieId = stateEvent.mediaId.toInt(),
                    stateEvent = stateEvent
                )
            }
            is GetMovieRecommendations -> {
                movieUseCases.movieRecommendationsUseCase.getResults(
                    viewState = ViewMediaViewState(),
                    movieId = stateEvent.mediaId.toInt(),
                    stateEvent = stateEvent
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

    fun createFullScreenInfo() {
        setStateEvent(
            CreateStateMessageEvent(
                stateMessage = StateMessage(
                    response = Response(
                        "Fullscreen is not available in app.Click on Youtube icon to switch to official app.",
                        UIComponentType.Toast,
                        MessageType.None
                    )
                )
            )
        )
    }

    fun loadSimilarMedia() {
        getMedia()?.let { media ->
            when (getMediaType()) {
                MediaType.MOVIE -> setStateEvent(GetSimilarMovies(media.id))
                MediaType.TV_SHOW -> setStateEvent(GetSimilarTvShows(media.id))
                MediaType.PERSON -> setStateEvent(GetPersonTvShowCast(media.id))
            }
        }
    }

    fun loadMediaRecommendations() {
        getMedia()?.let { media ->
            when (getMediaType()) {
                MediaType.MOVIE -> setStateEvent(GetMovieRecommendations(media.id))
                MediaType.TV_SHOW -> setStateEvent(GetTvShowRecommendations(media.id))
                MediaType.PERSON -> setStateEvent(GetPersonMovieCast(media.id))
            }
        }
    }

    fun loadDetails(mediaType: MediaType, mediaId: Long) {
        setStateEvent(GetDetails(mediaId, mediaType))
    }

    fun loadImages(mediaType: MediaType, mediaId: Long) {
        setStateEvent(GetImages(mediaId, mediaType))
    }

    fun loadTrailers(mediaType: MediaType, mediaId: Long) {
        if (mediaType == MediaType.MOVIE) setStateEvent(GetMovieTrailers(mediaId))
        else if (mediaType == MediaType.TV_SHOW) setStateEvent(GetTvShowTrailers(mediaId))
    }

    override fun initNewViewState() = ViewMediaViewState()

    val videosPlayerState: LiveData<VisibilityState>
        get() = _videosPlayerState

    fun setVideosPlayerState(state: VisibilityState) {
        if (state.toString() != _videosPlayerState.value.toString()) {
            _videosPlayerState.value = state
        }
    }

    fun isVideosPlayerDisplayed(): Boolean {
        return (videosPlayerState.value.toString() == Displayed.toString())
    }


    val trailerPlayerState: LiveData<VisibilityState>
        get() = _trailerPlayerState

    fun setTrailerPlayerState(state: VisibilityState) {
        if (state.toString() != _trailerPlayerState.value.toString()) {
            _trailerPlayerState.value = state
        }
    }

    fun isTrailerPlayerDisplayed(): Boolean {
        return (trailerPlayerState.value.toString() == Displayed.toString())
    }

    override fun getViewStateCopyWithoutBigLists(viewState: ViewMediaViewState): ViewMediaViewState {
        return viewState.copy()
    }

    override fun getUniqueViewStateIdentifier(): String {
        return ViewMediaViewState.getBundleKey()
    }
}