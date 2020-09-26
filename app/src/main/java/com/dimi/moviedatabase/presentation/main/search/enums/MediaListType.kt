package com.dimi.moviedatabase.presentation.main.search.enums

import com.dimi.moviedatabase.business.domain.state.Enumerable

enum class MediaListType( override val code: Int, val originalName: String) : Enumerable {
    POPULAR(0, "Popular"),
    TOP_RATED(1, "Top Rated"),
    TRENDING(2, "Trending"),
    UPCOMING_OR_ON_THE_AIR(3, "Upcoming");

    companion object {
        fun parseFromCode(code: Int): MediaListType = values()
            .firstOrNull { it.code == code } ?: POPULAR
    }
}