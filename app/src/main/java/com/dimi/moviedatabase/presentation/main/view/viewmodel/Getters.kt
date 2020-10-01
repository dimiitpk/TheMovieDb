package com.dimi.moviedatabase.presentation.main.view.viewmodel

import com.dimi.moviedatabase.business.domain.model.*
import com.dimi.moviedatabase.business.domain.state.MediaType
import com.dimi.moviedatabase.presentation.main.view.state.SeasonContainerState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
fun ViewMediaViewModel.getMedia(): Media? {
    getCurrentViewStateOrNew().let {
        return it.media
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ViewMediaViewModel.getMediaType(): MediaType {
    getCurrentViewStateOrNew().let {
        return it.mediaType ?: MediaType.MOVIE
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ViewMediaViewModel.getTrailers(): List<Video>? {
    getCurrentViewStateOrNew().let {
        return it.trailers
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ViewMediaViewModel.isThereAnyValidPoster() : Boolean {
    getCurrentViewStateOrNew().let {
        return !it.posters.isNullOrEmpty()
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ViewMediaViewModel.isThereAnyValidBackdrop() : Boolean {
    getCurrentViewStateOrNew().let {
        return !it.backdrops.isNullOrEmpty()
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ViewMediaViewModel.getSelectedSeason(): Season? {
    getCurrentViewStateOrNew().let {
        return it.selectedSeason
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun ViewMediaViewModel.getSelectedEpisode(): Episode? {
    getCurrentViewStateOrNew().let {
        return it.selectedEpisode
    }
}
@FlowPreview
@ExperimentalCoroutinesApi
fun ViewMediaViewModel.getSeasonContainerState(): SeasonContainerState {
    getCurrentViewStateOrNew().let {
        return it.seasonContainerState
    }
}