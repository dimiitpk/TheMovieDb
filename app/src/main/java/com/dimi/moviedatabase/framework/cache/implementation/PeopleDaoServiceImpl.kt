package com.dimi.moviedatabase.framework.cache.implementation

import com.dimi.moviedatabase.business.data.cache.abstraction.PeopleCacheDataSource
import com.dimi.moviedatabase.business.domain.model.Movie
import com.dimi.moviedatabase.business.domain.model.Person
import com.dimi.moviedatabase.business.domain.model.TvShow
import com.dimi.moviedatabase.framework.cache.database.MovieDao
import com.dimi.moviedatabase.framework.cache.database.PeopleDao
import com.dimi.moviedatabase.framework.cache.database.TvShowDao
import com.dimi.moviedatabase.framework.cache.mappers.CacheMapper
import com.dimi.moviedatabase.framework.cache.model.junction.MovieActorsCrossRef
import com.dimi.moviedatabase.framework.cache.model.junction.TvShowActorsCrossRef
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PeopleDaoServiceImpl(
    private val peopleDao: PeopleDao,
    private val movieDao: MovieDao,
    private val tvShowDao: TvShowDao,
    private val cacheMapper: CacheMapper
) : PeopleCacheDataSource {


    override suspend fun insertPerson(person: Person, upsert: Boolean): Long {
        return when {
            upsert -> peopleDao.upsert(cacheMapper.peopleCacheMapper.mapToEntity(person))
            else -> peopleDao.insert(cacheMapper.peopleCacheMapper.mapToEntity(person))
        }
    }

    override suspend fun updatePerson(person: Person) {
        peopleDao.update(cacheMapper.peopleCacheMapper.mapToEntity(person))
    }

    override suspend fun insertPeoples(peoples: List<Person>) {
        withContext(IO) {
            try {
                launch {
                    peopleDao.insert(*cacheMapper.peopleCacheMapper.mapToEntityList(peoples).toTypedArray())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun searchPeoples(query: String, page: Int): List<Person> {
        return cacheMapper.peopleCacheMapper.mapFromEntityList(
            peopleDao.getPopularPeoples(
                query, page
            )
        )
    }

    override suspend fun getPersonDetails(personId: Int): Person {
        return cacheMapper.peopleCacheMapper.mapFromEntity(
            peopleDao.getPerson(personId)
        )
    }

    override suspend fun insertMovieCast(movie: Movie, personId: Int) {

        val entity = cacheMapper.movieCacheMapper.mapToEntity(movie)
        movieDao.insert(entity).let {
            peopleDao.insert(
                MovieActorsCrossRef(
                    movieId = entity.id,
                    actorId = personId.toLong(),
                    character = movie.character ?: "",
                    priority = -1
                )
            )
        }
    }

    override suspend fun insertTvShowCast(tvShow: TvShow, personId: Long) {
        val entity = cacheMapper.tvShowCacheMapper.mapToEntity(tvShow)
        tvShowDao.insert(entity).let {
            peopleDao.insert(
                TvShowActorsCrossRef(
                    tvShowId = entity.id,
                    actorId = personId,
                    character = tvShow.character ?: "",
                    priority = -1
                )
            )
        }
    }


    override suspend fun getPersonMovieCast(personId: Long): List<Movie> {
        return cacheMapper.mapFromMovieActorCrossRef(
            peopleDao.getMovieCast(personId)
        )
    }

    override suspend fun getPersonTvShowCast(personId: Long): List<TvShow> {
        return cacheMapper.mapFromTvShowActorCrossRef(
            peopleDao.getTvShowCast(personId)
        )
    }
}