package com.dimi.moviedatabase.business.data.cache.implementation

import com.dimi.moviedatabase.business.data.cache.abstraction.MovieCacheDataSource
import com.dimi.moviedatabase.business.domain.model.Movie
import com.dimi.moviedatabase.business.domain.model.Person
import com.dimi.moviedatabase.presentation.main.search.enums.MediaListType

class MovieCacheDataSourceImpl(
    private val movieDaoService: MovieCacheDataSource
) : MovieCacheDataSource {

    override suspend fun insertMovie(movie: Movie, upsert: Boolean, mediaListType: MediaListType?, order: Int?): Long {
        return movieDaoService.insertMovie(movie, upsert, mediaListType, order)
    }

    override suspend fun updateMovie(movie: Movie) {
        movieDaoService.updateMovie(movie)
    }

    override suspend fun insertMovies(movies: List<Movie>) {
        movieDaoService.insertMovies(movies)
    }

    override suspend fun insertCast(person: Person, movieId: Long) {
        movieDaoService.insertCast(person, movieId)
    }

    override suspend fun getListOfMovies(
        query: String,
        page: Int,
        genre: Int,
        mediaListType: MediaListType?
    ): List<Movie> {
        return movieDaoService.getListOfMovies(query = query, page = page, genre = genre, mediaListType = mediaListType)
    }

    override suspend fun getMovie(movieId: Long): Movie {
        return movieDaoService.getMovie( movieId )
    }
}