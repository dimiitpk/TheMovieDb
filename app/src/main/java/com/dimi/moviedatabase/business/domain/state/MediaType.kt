package com.dimi.moviedatabase.business.domain.state

enum class MediaType(override val code: Int, val originalName: String, val pluralName: String) :
    Enumerable {
    MOVIE(0, "movie", "Movies"),
    TV_SHOW(1, "tv", "Tv Shows"),
    PERSON(2, "person", "People");

    companion object {
        fun parseFromCode(code: Int): MediaType = values()
            .firstOrNull { it.code == code } ?: MOVIE

        fun parseFromOriginalName(originalName: String): MediaType = values()
            .firstOrNull { it.originalName == originalName } ?: MOVIE
    }


}