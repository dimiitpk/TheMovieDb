package com.dimi.moviedatabase.business.data.cache.abstraction

import com.dimi.moviedatabase.business.domain.model.Movie
import com.dimi.moviedatabase.business.domain.model.Person
import com.dimi.moviedatabase.business.domain.model.TvShow

interface PeopleCacheDataSource {

    suspend fun insertPerson(person: Person, upsert: Boolean = false): Long

    suspend fun updatePerson(person: Person)

    suspend fun insertPeoples(peoples: List<Person>)

    suspend fun searchPeoples(
        query: String,
        page: Int
    ): List<Person>

    suspend fun getPersonDetails(
        personId: Int
    ): Person

    suspend fun insertMovieCast(
        movie: Movie,
        personId: Int
    )

    suspend fun insertTvShowCast(
        tvShow: TvShow,
        personId: Long
    )

    suspend fun getPersonMovieCast(personId: Long) : List<Movie>

    suspend fun getPersonTvShowCast(personId: Long) : List<TvShow>
}