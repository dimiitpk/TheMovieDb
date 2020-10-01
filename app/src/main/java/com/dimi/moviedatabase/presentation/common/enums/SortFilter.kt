package com.dimi.moviedatabase.presentation.common.enums

enum class SortFilter(val value: String, val upperCaseName: String) {
    BY_POPULARITY("popularity", "Popularity"),
    BY_FIRST_AIR_DATE("first_air_date", "Release date"),
    BY_VOTE_COUNT("vote_count", "Vote Count")
}