package com.dimi.moviedatabase.framework.cache.implementation

import com.dimi.moviedatabase.business.data.cache.abstraction.MovieCacheDataSource
import com.dimi.moviedatabase.business.domain.model.Movie
import com.dimi.moviedatabase.business.domain.model.Person
import com.dimi.moviedatabase.framework.cache.database.MovieDao
import com.dimi.moviedatabase.framework.cache.database.PeopleDao
import com.dimi.moviedatabase.framework.cache.mappers.CacheMapper
import com.dimi.moviedatabase.framework.cache.model.junction.MovieListCrossRef
import com.dimi.moviedatabase.framework.cache.model.junction.MovieActorsCrossRef
import com.dimi.moviedatabase.presentation.main.search.enums.MediaListType
import com.dimi.moviedatabase.util.GENRE_DEFAULT

class MovieDaoServiceImpl(
    private val movieDao: MovieDao,
    private val peopleDao: PeopleDao,
    private val cacheMapper: CacheMapper
) : MovieCacheDataSource {

    override suspend fun insertMovie(
        movie: Movie,
        upsert: Boolean,
        mediaListType: MediaListType?,
        order: Int?
    ): Long {

        return when {
            upsert -> movieDao.upsert(cacheMapper.movieCacheMapper.mapToEntity(movie))
            mediaListType != null -> {
                println("VAZISEDA")
                movieDao.insert(
                    cacheMapper.movieCacheMapper.mapToEntity(
                        movie
                    )
                ).let {
                    println("VAZISEDA2")
                    movieDao.insert(
                        MovieListCrossRef(
                            mediaListId = mediaListType.code,
                            movieId = movie.id,
                            order = order ?: -1
                        )
                    )
                }
            }
            else -> movieDao.insert(cacheMapper.movieCacheMapper.mapToEntity(movie))
        }
    }

    override suspend fun insertMovies(movies: List<Movie>) {
        try {
            movieDao.insert(
                *cacheMapper.movieCacheMapper.mapToEntityList(movies).toTypedArray()
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun updateMovie(movie: Movie) {
        return movieDao.update(cacheMapper.movieCacheMapper.mapToEntity(movie))
    }

    override suspend fun insertCast(person: Person, movieId: Long) {

        val entity = cacheMapper.peopleCacheMapper.mapToEntity(person)
        peopleDao.insert(entity).let {
            peopleDao.upsert(
                MovieActorsCrossRef(
                    movieId = movieId,
                    actorId = entity.id,
                    character = person.character ?: "",
                    priority = person.priority ?: -1
                )
            )
        }
    }

    override suspend fun getListOfMovies(
        query: String,
        page: Int,
        genre: Int,
        mediaListType: MediaListType?
    ): List<Movie> {
        return if (mediaListType != null)
            cacheMapper.mapFromMoviesByList(movieDao.getMoviesByList(mediaListType.code), page)
        else {
            val result = if (genre == GENRE_DEFAULT)
                movieDao.getPopularMovies(
                    query = query,
                    page = page
                )
            else movieDao.getMoviesByGenre(genre = genre.toString())
            cacheMapper.movieCacheMapper.mapFromEntityList(result)
        }
    }

    override suspend fun getMovie(movieId: Long): Movie {
        return cacheMapper.mapFromEntityWithCastList(
            movieDao.getMovieWithCast(movieId = movieId)
        )
    }
}