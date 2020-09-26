package com.dimi.moviedatabase.presentation.common

sealed class YoutubePlayerState {

    object Hidden : YoutubePlayerState() {
        override fun toString(): String {
            return "Hidden"
        }
    }

    object Displayed : YoutubePlayerState() {
        override fun toString(): String {
            return "Displayed"
        }
    }
}