package com.dimi.moviedatabase.business.data.cache.implementation

import com.dimi.moviedatabase.business.data.cache.abstraction.PeopleCacheDataSource
import com.dimi.moviedatabase.business.domain.model.Movie
import com.dimi.moviedatabase.business.domain.model.Person
import com.dimi.moviedatabase.business.domain.model.TvShow

class PeopleCacheDataSourceImpl(
    private val peopleDaoService: PeopleCacheDataSource
) : PeopleCacheDataSource {

    override suspend fun insertPerson(person: Person, upsert: Boolean): Long {
        return peopleDaoService.insertPerson(person, upsert)
    }

    override suspend fun updatePerson(person: Person) {
        return peopleDaoService.updatePerson(person)
    }

    override suspend fun insertPeoples(peoples: List<Person>) {
        return peopleDaoService.insertPeoples(peoples)
    }

    override suspend fun searchPeoples(query: String, page: Int): List<Person> {
        return peopleDaoService.searchPeoples(query, page)
    }

    override suspend fun getPersonDetails(personId: Int): Person {
        return peopleDaoService.getPersonDetails(personId)
    }

    override suspend fun insertMovieCast(movie: Movie, personId: Int) {
        peopleDaoService.insertMovieCast(movie, personId)
    }

    override suspend fun insertTvShowCast(tvShow: TvShow, personId: Long) {
        peopleDaoService.insertTvShowCast(tvShow, personId)
    }

    override suspend fun getPersonMovieCast(personId: Long): List<Movie> {
        return peopleDaoService.getPersonMovieCast(personId)
    }

    override suspend fun getPersonTvShowCast(personId: Long): List<TvShow> {
        return peopleDaoService.getPersonTvShowCast(personId)
    }
}