package com.dimi.moviedatabase.presentation.main.view.viewmodel

import com.dimi.moviedatabase.business.domain.model.*
import com.dimi.moviedatabase.business.domain.state.MediaType
import com.dimi.moviedatabase.presentation.main.view.state.SeasonContainerState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
fun ViewMediaViewModel.clearViewFields() {
    val update = getCurrentViewStateOrNew()
    update.media = null
    update.trailers = null
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ViewMediaViewModel.setViewItem(item: Media) {
    val update = getCurrentViewStateOrNew()
    update.media = item
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ViewMediaViewModel.setMediaType(type: MediaType) {
    val update = getCurrentViewStateOrNew()
    update.mediaType = type
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ViewMediaViewModel.setTrailers(list: List<Video>) {
    val update = getCurrentViewStateOrNew()
    update.trailers = list
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ViewMediaViewModel.setPosters(list: List<Image>) {
    val update = getCurrentViewStateOrNew()
    update.posters = list
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ViewMediaViewModel.setBackDrops(list: List<Image>) {
    val update = getCurrentViewStateOrNew()
    update.backdrops = list
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ViewMediaViewModel.setSeasonContainerState(state: SeasonContainerState) {
    val update = getCurrentViewStateOrNew()
    update.seasonContainerState = state
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ViewMediaViewModel.setSelectedSeason(season: Season?) {
    val update = getCurrentViewStateOrNew()
    update.selectedSeason = season
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ViewMediaViewModel.setSelectedEpisode(episode: Episode?) {
    val update = getCurrentViewStateOrNew()
    update.selectedEpisode = episode
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ViewMediaViewModel.setSeasonEpisodes(list: List<Episode>) {
    val update = getCurrentViewStateOrNew()
    update.seasonEpisodes = list
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ViewMediaViewModel.setRecommendedMedia(list: List<Media>) {
    val update = getCurrentViewStateOrNew()
    update.recommendedMedia = list
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ViewMediaViewModel.setSimilarMedia(list: List<Media>) {
    val update = getCurrentViewStateOrNew()
    update.similarMedia = list
    setViewState(update)
}