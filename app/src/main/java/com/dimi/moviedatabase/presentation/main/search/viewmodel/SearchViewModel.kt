package com.dimi.moviedatabase.presentation.main.search.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.dimi.moviedatabase.business.domain.model.Media
import com.dimi.moviedatabase.business.domain.state.*
import com.dimi.moviedatabase.business.interactors.movie.MovieUseCases
import com.dimi.moviedatabase.business.interactors.people.PeopleUseCases
import com.dimi.moviedatabase.business.interactors.tv_show.TvShowUseCases
import com.dimi.moviedatabase.presentation.common.BaseViewModel
import com.dimi.moviedatabase.presentation.common.enums.SortFilter
import com.dimi.moviedatabase.presentation.common.enums.SortOrder
import com.dimi.moviedatabase.presentation.main.search.UserPreferencesRepository
import com.dimi.moviedatabase.presentation.main.search.enums.ViewType
import com.dimi.moviedatabase.presentation.main.search.state.SearchStateEvent.*
import com.dimi.moviedatabase.presentation.main.search.state.SearchViewState
import com.dimi.moviedatabase.util.cutList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@FlowPreview
class SearchViewModel
@ViewModelInject
constructor(
    private val movieUseCases: MovieUseCases,
    private val tvShowUseCases: TvShowUseCases,
    private val peopleUseCases: PeopleUseCases,
    val userPreferencesRepository: UserPreferencesRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel<SearchViewState>(savedStateHandle) {

    val userPreferencesFlowLiveData = userPreferencesRepository.userPreferencesFlow.asLiveData()

    private val searchQueryFlow = MutableStateFlow("")

    fun setSearchQuery(query: String) {
        searchQueryFlow.value = query
    }

    init {
        viewModelScope.launch {
            searchQueryFlow
                .debounce(300)
                .filter {
                    return@filter (isValidQuery(it) && (it != getSearchQuery()))
                }
                .distinctUntilChanged()
                .flowOn(Dispatchers.Default)
                .collect {
                    loadFirstPage(it)
                }
        }
    }

    override fun handleNewData(data: SearchViewState) {
        data.let { viewState ->
            viewState.mediaList?.let { mediaList ->
                when (getViewType()) {
                    ViewType.GENRE, ViewType.NETWORK -> {
                        val sortedAndFilteredList = filterList(
                            mediaList,
                            getSortFilter(),
                            getSortOrder()
                        )
                        setDataList(
                            sortedAndFilteredList.cutList(getPage())
                        )
                    }
                    ViewType.NONE, ViewType.SEARCH -> {
                        setDataList(
                            mediaList//.cutList(getPage())
                        )
                    }
                }
            }
            viewState.isQueryExhausted?.let { isQueryExhausted ->
                setQueryExhausted(isQueryExhausted)
            }
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {

        val job: Flow<DataState<SearchViewState>?> = when (stateEvent) {

            is SearchMedia -> {
                if (stateEvent.clearLayoutManagerState) {
                    clearLayoutManagerState()
                }
                when (stateEvent.mediaType) {
                    MediaType.MOVIE ->
                        movieUseCases.searchMoviesUseCase.getResults(
                            viewState = SearchViewState(),
                            page = getPage(),
                            query = getSearchQuery(),
                            genre = getGenre(),
                            sortFilter = getSortFilter(),
                            sortOrder = getSortOrder(),
                            mediaListType = getMediaListType(),
                            viewStateKey = MediaType.MOVIE.name,
                            stateEvent = stateEvent
                        )
                    MediaType.TV_SHOW ->
                        tvShowUseCases.searchTvShowsUseCase.getResults(
                            viewState = SearchViewState(),
                            page = getPage(),
                            query = getSearchQuery(),
                            genre = getGenre(),
                            network = getNetwork(),
                            mediaListType = getMediaListType(),
                            sortFilter = getSortFilter(),
                            sortOrder = getSortOrder(),
                            viewStateKey = MediaType.TV_SHOW.name,
                            stateEvent = stateEvent
                        )
                    MediaType.PERSON ->
                        peopleUseCases.searchPeopleUseCase.getResults(
                            viewState = SearchViewState(),
                            page = getPage(),
                            query = getSearchQuery(),
                            viewStateKey = MediaType.PERSON.name,
                            stateEvent = stateEvent
                        )
                }
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

    private fun isValidQuery(query: String): Boolean {
        return (query.isNotBlank() && query.length >= 2)
    }

    override fun initNewViewState() = SearchViewState()

    override fun getViewStateCopyWithoutBigLists(viewState: SearchViewState): SearchViewState {
        return viewState.copy(mediaList = emptyList())
    }

    override fun getUniqueViewStateIdentifier(): String {
        return SearchViewState.getBundleKey()
    }

    private fun filterList(
        list: List<Media>,
        sortFilter: SortFilter,
        sortOrder: SortOrder
    ): List<Media> {

        return when (sortFilter) {
            SortFilter.BY_POPULARITY -> {
                when (sortOrder) {
                    SortOrder.DESCENDING -> list.sortedByDescending { it.popularity }
                    SortOrder.ASCENDING -> list.sortedBy { it.popularity }
                }
            }
            SortFilter.BY_FIRST_AIR_DATE -> {
                when (sortOrder) {
                    SortOrder.DESCENDING -> list.sortedByDescending { it.releaseDate?.time }
                    SortOrder.ASCENDING -> list.sortedBy { it.releaseDate?.time }
                }
            }
            SortFilter.BY_VOTE_COUNT -> {
                when (sortOrder) {
                    SortOrder.DESCENDING -> list.sortedByDescending { it.voteCount }
                    SortOrder.ASCENDING -> list.sortedBy { it.voteCount }
                }
            }
        }
    }
}