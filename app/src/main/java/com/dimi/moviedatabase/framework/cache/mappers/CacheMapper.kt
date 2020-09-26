package com.dimi.moviedatabase.framework.cache.mappers

import com.dimi.moviedatabase.business.domain.model.Movie
import com.dimi.moviedatabase.business.domain.model.TvShow
import com.dimi.moviedatabase.framework.cache.model.*
import com.dimi.moviedatabase.framework.cache.model.relations.*
import com.dimi.moviedatabase.util.cutList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CacheMapper
@Inject
constructor(
    val movieCacheMapper: MovieCacheMapper,
    val peopleCacheMapper: PeopleCacheMapper,
    val tvShowCacheMapper: TvShowCacheMapper,
    val seasonCacheMapper: SeasonCacheMapper,
    val networkCacheMapper: NetworkCacheMapper
) {

    fun mapFromEntityWithCastList(
        entity: TvShowCast,
        seasons: List<SeasonCacheEntity>,
        tvShowNetworks: TvShowNetworks
    ): TvShow {

        val tvShow = tvShowCacheMapper.mapFromEntity(entity.tvShow)
        tvShow.castList = if (entity.actors.isNotEmpty()) {
            entity.actors
                .map {
                    val actor = peopleCacheMapper.mapFromEntity(it)
                    for (item in entity.crossRef) {
                        if (item.actorId == actor.id) {
                            actor.character = item.character
                            actor.priority = item.priority
                            break
                        }
                    }
                    actor
                }
                .sortedBy { it.priority }
        } else emptyList()
        tvShow.seasons = seasonCacheMapper.mapFromEntityList(seasons)
        if (tvShow.id == tvShowNetworks.tvShow.id)
            tvShow.networks = networkCacheMapper.mapFromEntityList(tvShowNetworks.networks)
        return tvShow
    }

    fun mapFromTvShowActorCrossRef(
        personTvShowCast: PersonTvShowCast
    ): List<TvShow> {

        return if (personTvShowCast.tvShows.isNotEmpty())
            personTvShowCast.tvShows
                .map {
                    val tvShow = tvShowCacheMapper.mapFromEntity(it)
                    for (item in personTvShowCast.crossRef) {
                        if (item.tvShowId == tvShow.id) {
                            tvShow.character = item.character
                            break
                        }
                    }
                    tvShow
                }
        else emptyList()
    }

    fun mapFromMovieActorCrossRef(
        personMovieCast: PersonMovieCast
    ): List<Movie> {

        return if (personMovieCast.movies.isNotEmpty())
            personMovieCast.movies.map {
                val movie = movieCacheMapper.mapFromEntity(it)
                for (item in personMovieCast.crossRef) {
                    if (item.movieId == movie.id) {
                        movie.character = item.character
                        break
                    }
                }
                movie
            }
        else emptyList()
    }

    fun mapFromEntityWithCastList(entity: MovieCast): Movie {

        val movie = movieCacheMapper.mapFromEntity(entity.movie)
        movie.castList = if (entity.actors.isNotEmpty()) {
            entity.actors
                .map {
                    val actor = peopleCacheMapper.mapFromEntity(it)
                    for (item in entity.crossRef) {
                        if (item.actorId == actor.id) {
                            actor.character = item.character
                            actor.priority = item.priority
                            break
                        }
                    }
                    actor
                }
                .sortedBy { it.priority }
        } else emptyList()
        return movie
    }


    fun mapFromMoviesByList(
        moviesByList: MoviesByList?,
        page: Int
    ): List<Movie> {

        moviesByList?.let {
            return if (moviesByList.movies.isNotEmpty())

                moviesByList.movies
                    .map {
                        val movie = movieCacheMapper.mapFromEntity(it)
                        for (item in moviesByList.crossRef) {
                            if (item.movieId == movie.id) {
                                movie.order = item.order
                                break
                            }
                        }
                        movie
                    }
                    .sortedBy { it.order }
                    .cutList(page)
            else emptyList()
        } ?: return emptyList()
    }

    fun mapFromTvShowsByList(
        tvShowsByList: TvShowsByList?,
        page: Int
    ): List<TvShow> {

        tvShowsByList?.let {
            return if (tvShowsByList.tvShows.isNotEmpty())

                tvShowsByList.tvShows
                    .map {
                        val tvShow = tvShowCacheMapper.mapFromEntity(it)
                        for (item in tvShowsByList.crossRef) {
                            if (item.tvShowId == tvShow.id) {
                                tvShow.order = item.order
                                break
                            }
                        }
                        tvShow
                    }
                    .sortedBy { it.order }
                    .cutList(page)
            else emptyList()
        } ?: return emptyList()
    }
}