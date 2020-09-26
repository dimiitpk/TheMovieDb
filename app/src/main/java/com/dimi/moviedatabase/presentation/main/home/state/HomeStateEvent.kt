package com.dimi.moviedatabase.presentation.main.home.state

import com.dimi.moviedatabase.business.domain.state.StateEvent
import com.dimi.moviedatabase.business.domain.state.StateMessage


sealed class HomeStateEvent: StateEvent {

    object PopularMovies: HomeStateEvent() {

        override fun errorInfo(): String {
            return "Error retrieving list of popular movies."
        }

        override fun eventName(): String {
            return "PopularMovies"
        }

        override fun shouldDisplayProgressBar() = true
    }

    object TopRatedMovies : HomeStateEvent() {

        override fun errorInfo(): String {
            return "Error retrieving list of top rated movies."
        }

        override fun eventName(): String {
            return "TopRatedMovies"
        }

        override fun shouldDisplayProgressBar() = true
    }

    object UpcomingMovies : HomeStateEvent() {

        override fun errorInfo(): String {
            return "Error retrieving list of upcoming movies."
        }

        override fun eventName(): String {
            return "UpcomingMovies"
        }

        override fun shouldDisplayProgressBar() = true
    }

    object TrendingMovies : HomeStateEvent() {

        override fun errorInfo(): String {
            return "Error retrieving list of trending movies."
        }

        override fun eventName(): String {
            return "TrendingMovies"
        }

        override fun shouldDisplayProgressBar() = true
    }

    object TrendingTvShows : HomeStateEvent() {

        override fun errorInfo(): String {
            return "Error retrieving list of trending tv shows."
        }

        override fun eventName(): String {
            return "TrendingTvShows"
        }

        override fun shouldDisplayProgressBar() = true
    }

    object PopularTvShows : HomeStateEvent() {

        override fun errorInfo(): String {
            return "Error retrieving list of popular tv shows."
        }

        override fun eventName(): String {
            return "PopularTvShows"
        }

        override fun shouldDisplayProgressBar() = false
    }

    object TopRatedTvShows : HomeStateEvent() {

        override fun errorInfo(): String {
            return "Error retrieving list of top rated tv shows."
        }

        override fun eventName(): String {
            return "TopRatedTvShows"
        }

        override fun shouldDisplayProgressBar() = true
    }

    object OnTheAirTvShows : HomeStateEvent() {

        override fun errorInfo(): String {
            return "Error retrieving list of on the air tv shows."
        }

        override fun eventName(): String {
            return "OnTheAirTvShows"
        }

        override fun shouldDisplayProgressBar() = true
    }

    class CreateStateMessageEvent(
        val stateMessage: StateMessage
    ): HomeStateEvent(){

        override fun errorInfo(): String {
            return "Error creating a new state message."
        }

        override fun eventName(): String {
            return "CreateStateMessageEvent"
        }

        override fun shouldDisplayProgressBar() = false
    }

}




















