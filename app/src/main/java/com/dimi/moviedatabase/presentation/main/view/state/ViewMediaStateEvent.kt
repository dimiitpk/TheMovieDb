package com.dimi.moviedatabase.presentation.main.view.state

import com.dimi.moviedatabase.business.domain.model.Season
import com.dimi.moviedatabase.business.domain.state.MediaType
import com.dimi.moviedatabase.business.domain.state.StateEvent
import com.dimi.moviedatabase.business.domain.state.StateMessage


sealed class ViewMediaStateEvent: StateEvent {

    class GetDetails(
        val mediaId: Long,
        val mediaType: MediaType
    ): ViewMediaStateEvent() {

        override fun errorInfo(): String {
            return "Error retrieving media details."
        }

        override fun eventName(): String {
            return "GetMovieDetails"
        }

        override fun shouldDisplayProgressBar() = true
    }

    class GetPersonMovieCast(
        val mediaId: Long
    ): ViewMediaStateEvent() {

        override fun errorInfo(): String {
            return "Error retrieving person movie cast."
        }

        override fun eventName(): String {
            return "GetPersonMovieCast"
        }

        override fun shouldDisplayProgressBar() = true
    }

    class GetPersonTvShowCast(
        val mediaId: Long
    ): ViewMediaStateEvent() {

        override fun errorInfo(): String {
            return "Error retrieving person tv show cast."
        }

        override fun eventName(): String {
            return "GetPersonTvShowCast"
        }

        override fun shouldDisplayProgressBar() = true
    }

    class GetImages(
        val mediaId: Long,
        val mediaType: MediaType
    ): ViewMediaStateEvent() {

        override fun errorInfo(): String {
            return "Error retrieving media images."
        }

        override fun eventName(): String {
            return "GetMovieImages"
        }

        override fun shouldDisplayProgressBar() = false
    }

    class GetMovieTrailers(
        val mediaId: Long
    ): ViewMediaStateEvent() {

        override fun errorInfo(): String {
            return "Error retrieving movie trailers."
        }

        override fun eventName(): String {
            return "GetMovieTrailers"
        }

        override fun shouldDisplayProgressBar() = false
    }

    class GetSimilarMovies(
        val mediaId: Long
    ): ViewMediaStateEvent() {

        override fun errorInfo(): String {
            return "Error retrieving similar movies."
        }

        override fun eventName(): String {
            return "GetSimilarMovies"
        }

        override fun shouldDisplayProgressBar() = false
    }

    class GetMovieRecommendations(
        val mediaId: Long
    ): ViewMediaStateEvent() {

        override fun errorInfo(): String {
            return "Error retrieving movies recommendation."
        }

        override fun eventName(): String {
            return "GetMovieRecommendations"
        }

        override fun shouldDisplayProgressBar() = false
    }

    class GetTvShowTrailers(
        val mediaId: Long
    ): ViewMediaStateEvent() {

        override fun errorInfo(): String {
            return "Error retrieving tv shows trailers."
        }

        override fun eventName(): String {
            return "GetTvShowTrailers"
        }

        override fun shouldDisplayProgressBar() = false
    }

    class GetSimilarTvShows(
        val mediaId: Long
    ): ViewMediaStateEvent() {

        override fun errorInfo(): String {
            return "Error retrieving similar tv shows."
        }

        override fun eventName(): String {
            return "GetSimilarTvShows"
        }

        override fun shouldDisplayProgressBar() = false
    }

    class GetTvShowRecommendations(
        val mediaId: Long
    ): ViewMediaStateEvent() {

        override fun errorInfo(): String {
            return "Error retrieving tv shows recommendation."
        }

        override fun eventName(): String {
            return "GetTvShowRecommendations"
        }

        override fun shouldDisplayProgressBar() = false
    }

    class GetEpisodesPerSeason(
        val mediaId: Long,
        val season: Season
    ): ViewMediaStateEvent() {

        override fun errorInfo(): String {
            return "Error retrieving tv show episodes."
        }

        override fun eventName(): String {
            return "GetEpisodesPerSeason"
        }

        override fun shouldDisplayProgressBar() = true
    }

    class CreateStateMessageEvent(
        val stateMessage: StateMessage
    ): ViewMediaStateEvent(){

        override fun errorInfo(): String {
            return "Error creating a new state message."
        }

        override fun eventName(): String {
            return "CreateStateMessageEvent"
        }

        override fun shouldDisplayProgressBar() = false
    }

}




















