package com.dimi.moviedatabase.presentation.main.search.viewmodel

import android.os.Parcelable
import androidx.lifecycle.viewModelScope
import com.dimi.moviedatabase.business.domain.model.Media
import com.dimi.moviedatabase.business.domain.model.Network
import com.dimi.moviedatabase.business.domain.state.MediaType
import com.dimi.moviedatabase.presentation.common.enums.SortFilter
import com.dimi.moviedatabase.presentation.common.enums.SortOrder
import com.dimi.moviedatabase.presentation.main.search.enums.MediaListType
import com.dimi.moviedatabase.presentation.main.search.enums.ViewType
import com.dimi.moviedatabase.util.GENRE_DEFAULT
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.setQueryExhausted(isExhausted: Boolean) {
    val update = getCurrentViewStateOrNew()
    update.isQueryExhausted = isExhausted
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.setLayoutSpanCount(spanCount: Int) {
    val update = getCurrentViewStateOrNew()
    update.layoutSpanCount = spanCount
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.saveLayoutMode(layoutSpanCount: Int) {
    viewModelScope.launch {
        userPreferencesRepository.saveLayoutMode(layoutSpanCount)
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.enableSortByDESC() {
    viewModelScope.launch {
        userPreferencesRepository.enableOrderDESC()
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.enableSortByASC() {
    viewModelScope.launch {
        userPreferencesRepository.enableOrderASC()
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.enableSortByVoteAverage() {
    viewModelScope.launch {
        userPreferencesRepository.enableFilterByVoteAverage()
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.enableSortByPopularity() {
    viewModelScope.launch {
        userPreferencesRepository.enableFilterByPopularity()
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.enableSortByFirstAirDate() {
    viewModelScope.launch {
        userPreferencesRepository.enableFilterByFirstAirDate()
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.setNetwork(network: Network) {
    val update = getCurrentViewStateOrNew()
    update.network = network
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.setViewType(viewType: ViewType) {
    val update = getCurrentViewStateOrNew()
    update.viewType = viewType
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.setSortOrder(sortOrder: SortOrder) {
    val update = getCurrentViewStateOrNew()
    update.sortOrder = sortOrder
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.setSortFilter(sortFilter: SortFilter) {
    val update = getCurrentViewStateOrNew()
    update.sortFilter = sortFilter
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.setMediaListType(mediaListType: MediaListType) {
    val update = getCurrentViewStateOrNew()
    update.mediaListType = mediaListType
    setViewState(update)
}


@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.setSelectedTab(position: Int) {
    val update = getCurrentViewStateOrNew()
    update.selectedTab = position
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.setGenre(genreId: Int) {
    val update = getCurrentViewStateOrNew()
    update.searchGenre = genreId
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.clearGenre() {
    val update = getCurrentViewStateOrNew()
    update.searchGenre = GENRE_DEFAULT
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.setQuery(query: String) {
    val update = getCurrentViewStateOrNew()
    update.searchQuery = query
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.setLayoutManagerState(layoutManagerState: Parcelable) {
    val update = getCurrentViewStateOrNew()
    update.layoutManagerState = layoutManagerState
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.clearLayoutManagerState() {
    val update = getCurrentViewStateOrNew()
    update.layoutManagerState = null
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.setDataList(list: List<Media>) {
    val update = getCurrentViewStateOrNew()
    update.mediaList = list
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.setMediaType(mediaType: MediaType) {
    val update = getCurrentViewStateOrNew()
    update.mediaType = mediaType
    setViewState(update)
}