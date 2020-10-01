package com.dimi.moviedatabase.presentation.main.search.viewmodel

import com.dimi.moviedatabase.presentation.main.search.state.SearchStateEvent.*
import com.dimi.moviedatabase.presentation.main.search.state.SearchViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.resetPage() {
    val update = getCurrentViewStateOrNew()
    update.page = 1
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.refreshFromCache(clearLayoutManagerState: Boolean = false) {

    if (!isJobAlreadyActive(SearchMedia())) {
        setQueryExhausted(false)
        setStateEvent(SearchMedia(getMediaType(), clearLayoutManagerState))
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.loadFirstPage(newQuery: String? = null) {
    newQuery?.let {
        setQuery(it)
    }
    if (!isJobAlreadyActive(SearchMedia())) {
        setQueryExhausted(false)
        resetPage()
        setStateEvent(SearchMedia(getMediaType()))
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
private fun SearchViewModel.incrementPageNumber() {
    val update = getCurrentViewStateOrNew()
    val page = update.copy().page ?: 1// get current page
    update.page = page.plus(1)
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.nextPage() {
    if (!isJobAlreadyActive(SearchMedia())
        && !isQueryExhausted()
    ) {
        incrementPageNumber()
        setStateEvent(SearchMedia(getMediaType()))
        println("Attempting load next page")
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.handleIncomingData(viewState: SearchViewState) {
    viewState.let { movieFields ->
        movieFields.mediaList?.let {
            setDataList(it)
        }
    }
}



