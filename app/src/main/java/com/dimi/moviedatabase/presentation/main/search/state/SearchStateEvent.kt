package com.dimi.moviedatabase.presentation.main.search.state

import com.dimi.moviedatabase.business.domain.state.MediaType
import com.dimi.moviedatabase.business.domain.state.StateEvent
import com.dimi.moviedatabase.business.domain.state.StateMessage


sealed class SearchStateEvent: StateEvent {

    class SearchMedia(
        val mediaType: MediaType = MediaType.MOVIE,
        val clearLayoutManagerState: Boolean = true
    ): SearchStateEvent() {

        override fun errorInfo(): String {
            return "Error searching media."
        }

        override fun eventName(): String {
            return "SearchMedia"
        }

        override fun shouldDisplayProgressBar() = true
    }

    class CreateStateMessageEvent(
        val stateMessage: StateMessage
    ): SearchStateEvent(){

        override fun errorInfo(): String {
            return "Error creating a new state message."
        }

        override fun eventName(): String {
            return "CreateStateMessageEvent"
        }

        override fun shouldDisplayProgressBar() = false
    }

}




















