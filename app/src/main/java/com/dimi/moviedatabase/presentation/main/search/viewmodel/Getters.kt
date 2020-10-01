package com.dimi.moviedatabase.presentation.main.search.viewmodel

import android.os.Parcelable
import com.dimi.moviedatabase.business.domain.model.Network
import com.dimi.moviedatabase.business.domain.state.MediaType
import com.dimi.moviedatabase.presentation.common.enums.SortFilter
import com.dimi.moviedatabase.presentation.common.enums.SortOrder
import com.dimi.moviedatabase.presentation.main.search.enums.ViewType
import com.dimi.moviedatabase.presentation.main.search.enums.MediaListType
import com.dimi.moviedatabase.util.GENRE_DEFAULT
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.getNetwork(): Network? {
    return getCurrentViewStateOrNew().network
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.getLayoutSpanCount(): Int {
    return getCurrentViewStateOrNew().layoutSpanCount
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.getSearchQuery(): String {
    return getCurrentViewStateOrNew().searchQuery
        ?: ""
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.getViewType(): ViewType {
    return getCurrentViewStateOrNew().viewType
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.getSortFilter(): SortFilter {
    return getCurrentViewStateOrNew().sortFilter
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.getMediaListType(): MediaListType? {
    return getCurrentViewStateOrNew().mediaListType
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.getSortOrder(): SortOrder {
    return getCurrentViewStateOrNew().sortOrder
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.isQueryExhausted(): Boolean {
    return getCurrentViewStateOrNew().isQueryExhausted
        ?: false
}


@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.getPage(): Int {
    return getCurrentViewStateOrNew().page
        ?: 1
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.getGenre(): Int {
    return getCurrentViewStateOrNew().searchGenre
        ?: GENRE_DEFAULT
}


@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.getMediaType(): MediaType {
    return getCurrentViewStateOrNew().mediaType
        ?: return MediaType.MOVIE
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.getLayoutManagerState(): Parcelable? {
    getCurrentViewStateOrNew().let {
        return it.layoutManagerState
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun SearchViewModel.getSelectedTab(): Int {
    return getCurrentViewStateOrNew().selectedTab
        ?: return 0
}