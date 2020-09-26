package com.dimi.moviedatabase.presentation.main.home.viewmodel

import com.dimi.moviedatabase.business.domain.model.Media
import com.dimi.moviedatabase.business.domain.state.MediaType
import com.dimi.moviedatabase.presentation.main.search.enums.MediaListType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
fun HomeViewModel.getHomeList(mediaListType: MediaListType, mediaType: MediaType): List<Media>? {

    return when(mediaType) {
        MediaType.MOVIE -> when( mediaListType ) {
            MediaListType.POPULAR -> getCurrentViewStateOrNew().popularMovies
            MediaListType.TOP_RATED -> getCurrentViewStateOrNew().topRatedMovies
            MediaListType.TRENDING -> getCurrentViewStateOrNew().trendingMovies
            MediaListType.UPCOMING_OR_ON_THE_AIR -> getCurrentViewStateOrNew().upcomingMovies
        }
        MediaType.TV_SHOW -> when( mediaListType ) {
            MediaListType.POPULAR -> getCurrentViewStateOrNew().popularTvShows
            MediaListType.TOP_RATED -> getCurrentViewStateOrNew().topRatedTvShows
            MediaListType.TRENDING -> getCurrentViewStateOrNew().trendingTvShows
            MediaListType.UPCOMING_OR_ON_THE_AIR -> getCurrentViewStateOrNew().onTheAirTvShows
        }
        else -> getCurrentViewStateOrNew().popularMovies
    }
}