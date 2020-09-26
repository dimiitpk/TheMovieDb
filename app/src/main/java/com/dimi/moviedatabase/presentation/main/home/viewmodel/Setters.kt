package com.dimi.moviedatabase.presentation.main.home.viewmodel

import com.dimi.moviedatabase.business.domain.model.Movie
import com.dimi.moviedatabase.business.domain.model.TvShow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
fun HomeViewModel.setPopularMoviesList(list: List<Movie>) {
    val update = getCurrentViewStateOrNew()
    update.popularMovies = list
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun HomeViewModel.setUpcomingMovies(list: List<Movie>) {
    val update = getCurrentViewStateOrNew()
    update.upcomingMovies = list
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun HomeViewModel.setPopularTvShows(list: List<TvShow>) {
    val update = getCurrentViewStateOrNew()
    update.popularTvShows = list
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun HomeViewModel.setTopRatedMovies(list: List<Movie>) {
    val update = getCurrentViewStateOrNew()
    update.topRatedMovies = list
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun HomeViewModel.setTopRatedTvShows(list: List<TvShow>) {
    val update = getCurrentViewStateOrNew()
    update.topRatedTvShows = list
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun HomeViewModel.setTrendingTvShows(list: List<TvShow>) {
    val update = getCurrentViewStateOrNew()
    update.trendingTvShows = list
    setViewState(update)
}
@FlowPreview
@ExperimentalCoroutinesApi
fun HomeViewModel.setOnTheAirTvShows(list: List<TvShow>) {
    val update = getCurrentViewStateOrNew()
    update.onTheAirTvShows = list
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun HomeViewModel.setTrendingMovies(list: List<Movie>) {
    val update = getCurrentViewStateOrNew()
    update.trendingMovies = list
    setViewState(update)
}