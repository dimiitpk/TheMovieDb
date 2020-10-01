package com.dimi.moviedatabase.presentation.common

sealed class VisibilityState {

    object Hidden : VisibilityState() {
        override fun toString(): String {
            return "Hidden"
        }
    }

    object Displayed : VisibilityState() {
        override fun toString(): String {
            return "Displayed"
        }
    }
}