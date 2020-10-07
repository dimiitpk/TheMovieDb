package com.dimi.moviedatabase.business.data.cache

import com.dimi.moviedatabase.business.data.MovieFactory
import com.dimi.moviedatabase.business.data.cache.abstraction.MovieCacheDataSource
import com.dimi.moviedatabase.business.domain.model.Movie
import com.dimi.moviedatabase.business.domain.model.Person
import com.dimi.moviedatabase.framework.network.NetworkConstants.PAGE_SIZE
import com.dimi.moviedatabase.presentation.main.search.enums.MediaListType
import com.dimi.moviedatabase.util.Constants.TEST_FORCE_FAIL
import com.dimi.moviedatabase.util.Constants.TEST_SUCCESS
import com.dimi.moviedatabase.util.GENRE_DEFAULT
import java.lang.Exception


class FakeMovieCacheDataSourceImpl(
    private val movieList: MutableList<Movie> = ArrayList()
) : MovieCacheDataSource {

    private val personList = mutableListOf<Person>()

    override suspend fun insertMovie(
        movie: Movie,
        upsert: Boolean,
        mediaListType: MediaListType?,
        order: Int?
    ): Long {
        if (upsert) {
            try {
                val index = movieList.indexOfFirst { it.id == movie.id }
                movieList[index] = movie
            } catch (e: Exception) {
                movieList.add(movie)
            }
        } else
            if (!movieList.contains(movie))
                movieList.add(movie)
        return TEST_SUCCESS
    }

    override suspend fun updateMovie(movie: Movie) {
        try {
            val index = movieList.indexOfFirst { it.id == movie.id }
            movieList[index] = movie
        } catch (e: Exception) {

        }
    }

    override suspend fun insertMovies(movies: List<Movie>) {
        movieList.addAll(movies)
    }

    override suspend fun insertCast(person: Person, movieId: Long) {
        personList.add(person)
    }

    override suspend fun getListOfMovies(
        query: String,
        page: Int,
        genre: Int,
        mediaListType: MediaListType?
    ): List<Movie> {

        val newList = ArrayList<Movie>()

        for (item in movieList) {

            if (newList.size > (page * PAGE_SIZE)) {
                break
            }

            if (genre == GENRE_DEFAULT) {
                if (query.isNotBlank()) {
                    if (item.title.contains(query))
                        newList.add(item)
                } else
                    newList.add(item)
            } else {
                item.genres?.contains(genre)?.let {
                    if (it)
                        newList.add(item)
                }
            }
        }
        return newList
    }

    override suspend fun getMovie(movieId: Long): Movie {
        if (movieId == TEST_FORCE_FAIL)
            return MovieFactory.createMovie(TEST_FORCE_FAIL)

        return try {
            val movie = movieList.first { it.id == movieId }
            movie.castList = personList.sortedBy { it.priority }
            movie
        } catch (e: Exception) {
            MovieFactory.createMovie(TEST_FORCE_FAIL)
        }
    }

}