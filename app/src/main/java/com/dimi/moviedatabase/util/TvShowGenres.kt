package com.dimi.moviedatabase.util

import java.util.LinkedHashMap

object TvShowGenres {

    private const val GENRE_ACTION_AND_ADVENTURE = 10759
    private const val GENRE_ANIMATION = 16
    private const val GENRE_COMEDY = 35
    private const val GENRE_CRIME = 80
    private const val GENRE_DOCUMENTARY = 99
    private const val GENRE_DRAMA = 18
    private const val GENRE_FAMILY = 10751
    private const val GENRE_KIDS = 10762
    private const val GENRE_MYSTERY = 9648
    private const val GENRE_NEWS = 10763
    private const val GENRE_REALITY = 10764
    private const val GENRE_SCI_FI_AND_FANTASY = 10765
    private const val GENRE_SOAP = 10766
    private const val GENRE_TALK = 10767
    private const val GENRE_WAR_AND_POLITICS = 10768
    private const val GENRE_WESTERN = 37

    private val genreHash: LinkedHashMap<Int, String> = linkedMapOf(
        GENRE_ACTION_AND_ADVENTURE to "Action & Adventure",
        GENRE_ANIMATION to "Animation",
        GENRE_COMEDY to "Comedy",
        GENRE_CRIME to "Crime",
        GENRE_DOCUMENTARY to "Documentary",
        GENRE_DRAMA to "Drama",
        GENRE_FAMILY to "Family",
        GENRE_KIDS to "Kids",
        GENRE_MYSTERY to "Mystery",
        GENRE_NEWS to "News",
        GENRE_REALITY to "Reality",
        GENRE_SCI_FI_AND_FANTASY to "Sci-Fi & Fantasy",
        GENRE_SOAP to "Soap",
        GENRE_TALK to "Talk",
        GENRE_WAR_AND_POLITICS to "War & Politics",
        GENRE_WESTERN to "Western",
    )

    fun getAllGenreIds() = genreHash.keys

    fun getGenreName(genreID: Int): String? {
        return genreHash[genreID]
    }

    fun getGenreIdByName(genreName: String?): Int {
        val listOfValues = genreHash.filterValues { it == genreName }.keys
        return if( listOfValues.isNotEmpty() )
            listOfValues.first()
        else GENRE_ACTION_AND_ADVENTURE
    }
}