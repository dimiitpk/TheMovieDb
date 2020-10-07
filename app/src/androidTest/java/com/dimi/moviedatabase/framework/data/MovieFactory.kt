package com.dimi.moviedatabase.framework.data

import com.dimi.moviedatabase.business.domain.model.Movie
import com.dimi.moviedatabase.business.domain.state.MediaType
import com.dimi.moviedatabase.util.Genre
import javax.inject.Inject

class MovieFactory @Inject constructor() {

    fun createMovie(id: Long): Movie {
        return Movie(
            id = id,
            title = "Title",
            popularity = 3.2,
            voteCount = 424,
            voteAverage = 4.5f,
            overview = "Overview",
            posterPath = "random url",
            tagLine = "empty",
            backdropPath = "random url",
            genres = Genre(MediaType.MOVIE).getAllGenreIds().shuffled().slice(0..2)
        )
    }

    fun createMovieList(size: Int): List<Movie> {
        val list = ArrayList<Movie>()
        for (i in 0..size) {
            list.add(createMovie(i.toLong()))
        }
        return list
    }

}