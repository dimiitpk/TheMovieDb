package com.dimi.moviedatabase.util

import com.dimi.moviedatabase.business.domain.state.MediaType

const val GENRE_DEFAULT = -1

class Genre constructor(
    private val mediaType: MediaType
) {

    fun getAllGenreIds() =
        if (mediaType == MediaType.MOVIE) MovieGenres.getAllGenreIds() else TvShowGenres.getAllGenreIds()

    fun getGenreIdByName(genreName: String?) =
        if (mediaType == MediaType.MOVIE) MovieGenres.getGenreIdByName(genreName) else TvShowGenres.getGenreIdByName(
            genreName
        )

    fun getGenreName(genreId: Int) =
        if (mediaType == MediaType.MOVIE) MovieGenres.getGenreName(genreId) else TvShowGenres.getGenreName(
            genreId
        )
}