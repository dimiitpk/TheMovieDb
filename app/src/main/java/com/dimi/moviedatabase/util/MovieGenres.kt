package com.dimi.moviedatabase.util

import java.util.LinkedHashMap

object MovieGenres {

    private const val GENRE_ACTION = 28
    private const val GENRE_ADVENTURE = 12
    private const val GENRE_ANIMATION = 16
    private const val GENRE_COMEDY = 35
    private const val GENRE_CRIME = 80
    private const val GENRE_DOCUMENTARY = 99
    private const val GENRE_DRAMA = 18
    private const val GENRE_FAMILY = 10751
    private const val GENRE_FANTASY = 14
    private const val GENRE_HISTORY = 36
    private const val GENRE_HORROR = 27
    private const val GENRE_MUSIC = 10402
    private const val GENRE_MYSTERY = 9648
    private const val GENRE_ROMANCE = 10749
    private const val GENRE_SCI_FI = 878
    private const val GENRE_TV_MOVIE = 10770
    private const val GENRE_THRILLER = 53
    private const val GENRE_WAR = 10752
    private const val GENRE_WESTERN = 37

    private val genreHash: LinkedHashMap<Int, String> = linkedMapOf(
        GENRE_ACTION to "Action",
        GENRE_ADVENTURE to "Adventure",
        GENRE_ANIMATION to "Animation",
        GENRE_COMEDY to "Comedy",
        GENRE_CRIME to "Crime",
        GENRE_DOCUMENTARY to "Documentary",
        GENRE_DRAMA to "Drama",
        GENRE_FAMILY to "Family",
        GENRE_FANTASY to "Fantasy",
        GENRE_HISTORY to "History",
        GENRE_HORROR to "Horror",
        GENRE_MUSIC to "Music",
        GENRE_MYSTERY to "Mystery",
        GENRE_ROMANCE to "Romance",
        GENRE_SCI_FI to "Sci-Fi",
        GENRE_TV_MOVIE to "TV Movie",
        GENRE_THRILLER to "Thriller",
        GENRE_WAR to "War",
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
        else GENRE_ACTION
    }
}