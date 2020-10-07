package com.dimi.moviedatabase.business.data.cache.abstraction

import com.dimi.moviedatabase.business.domain.model.Movie
import com.dimi.moviedatabase.business.domain.model.Person
import com.dimi.moviedatabase.presentation.main.search.enums.MediaListType
import com.dimi.moviedatabase.util.GENRE_DEFAULT

interface MovieCacheDataSource {

    suspend fun insertMovie(
        movie: Movie,
        upsert: Boolean = false,
        mediaListType: MediaListType? = null,
        order: Int? = null
    ): Long

    suspend fun updateMovie(movie: Movie)

    suspend fun insertMovies(movies: List<Movie>)

    suspend fun insertCast(person: Person, movieId: Long)

    suspend fun getListOfMovies(
        query: String = "",
        page: Int,
        genre: Int = GENRE_DEFAULT,
        mediaListType: MediaListType? = null
    ): List<Movie>

    suspend fun getMovie(
        movieId: Long
    ): Movie
}