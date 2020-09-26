package com.dimi.moviedatabase.presentation.images.state

import com.dimi.moviedatabase.business.domain.state.MediaType
import com.dimi.moviedatabase.business.domain.state.StateEvent
import com.dimi.moviedatabase.business.domain.state.StateMessage

sealed class FullScreenImagesStateEvent: StateEvent {

    class GetImages(
        val mediaId: Long,
        val mediaType: MediaType
    ): FullScreenImagesStateEvent() {

        override fun errorInfo(): String {
            return "Error retrieving movie images."
        }

        override fun eventName(): String {
            return "GetMovieImages"
        }

        override fun shouldDisplayProgressBar() = false
    }

    class CreateStateMessageEvent(
        val stateMessage: StateMessage
    ): FullScreenImagesStateEvent(){

        override fun errorInfo(): String {
            return "Error creating a new state message."
        }

        override fun eventName(): String {
            return "CreateStateMessageEvent"
        }

        override fun shouldDisplayProgressBar() = false
    }

}




















