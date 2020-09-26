package com.dimi.moviedatabase.framework.cache.implementation

import com.dimi.moviedatabase.business.data.cache.abstraction.TvShowCacheDataSource
import com.dimi.moviedatabase.business.domain.model.*
import com.dimi.moviedatabase.framework.cache.database.PeopleDao
import com.dimi.moviedatabase.framework.cache.database.TvShowDao
import com.dimi.moviedatabase.framework.cache.mappers.CacheMapper
import com.dimi.moviedatabase.framework.cache.model.junction.TvShowActorsCrossRef
import com.dimi.moviedatabase.framework.cache.model.junction.TvShowListCrossRef
import com.dimi.moviedatabase.framework.cache.model.junction.TvShowNetworkCrossRef
import com.dimi.moviedatabase.presentation.main.search.SortFilter
import com.dimi.moviedatabase.presentation.main.search.enums.MediaListType
import com.dimi.moviedatabase.util.GENRE_DEFAULT

class TvShowDaoServiceImpl(
    private val tvShowDao: TvShowDao,
    private val peopleDao: PeopleDao,
    private val cacheMapper: CacheMapper
) : TvShowCacheDataSource {

    override suspend fun insertTvShow(
        tvShow: TvShow,
        networkId: Long?,
        upsert: Boolean,
        mediaListType: MediaListType?,
        order: Int?
    ): Long {

        return when {
            upsert -> tvShowDao.upsert(cacheMapper.tvShowCacheMapper.mapToEntity(tvShow))
            mediaListType != null -> tvShowDao.insert(
                cacheMapper.tvShowCacheMapper.mapToEntity(
                    tvShow
                )
            ).let {
                tvShowDao.insert(
                    TvShowListCrossRef(
                        mediaListId = mediaListType.code,
                        tvShowId = tvShow.id,
                        order = order ?: -1
                    )
                )
            }
            networkId != null -> {
                tvShowDao.insert(
                    cacheMapper.tvShowCacheMapper.mapToEntity(
                        tvShow
                    )
                ).let {
                    tvShowDao.insert(
                        TvShowNetworkCrossRef(
                            tvShowId = tvShow.id,
                            networkId = networkId
                        )
                    )
                }
            }
            else -> tvShowDao.insert(cacheMapper.tvShowCacheMapper.mapToEntity(tvShow))
        }
    }

    override suspend fun updateTvShow(tvShow: TvShow) {
        tvShowDao.update(cacheMapper.tvShowCacheMapper.mapToEntity(tvShow))
    }

    override suspend fun insertTvShows(tvShows: List<TvShow>) {
        tvShowDao.insert(*cacheMapper.tvShowCacheMapper.mapToEntityList(tvShows).toTypedArray())
    }

    override suspend fun insertCast(person: Person, tvShowId: Long) {

        val entity = cacheMapper.peopleCacheMapper.mapToEntity(person)
        peopleDao.insert(entity).let {
            peopleDao.insert(
                TvShowActorsCrossRef(
                    tvShowId = tvShowId,
                    actorId = entity.id,
                    character = person.character ?: "",
                    priority = person.priority ?: -1
                )
            )
        }
    }

    override suspend fun insertNetwork(network: Network, tvShowId: Long) {
        tvShowDao.insert(cacheMapper.networkCacheMapper.mapToEntity(network)).let {
            tvShowDao.insert(
                TvShowNetworkCrossRef(
                    tvShowId = tvShowId,
                    networkId = network.id
                )
            )
        }
    }

    override suspend fun insertSeason(season: Season, tvShowId: Long): Long {
        season.tvShowId = tvShowId
        return tvShowDao.upsert(cacheMapper.seasonCacheMapper.mapToEntity(season))
    }

    override suspend fun getListOfTvShows(
        query: String,
        page: Int,
        genre: Int,
        mediaListType: MediaListType?,
        network: Network?
    ): List<TvShow> {

        return if (mediaListType != null) {
            cacheMapper.mapFromTvShowsByList(tvShowDao.getTvShowsByList(mediaListType.code), page)
        } else {
            val result =
                if (genre == GENRE_DEFAULT)
                    if (network != null) tvShowDao.getNetworkTvShows(networkId = network.id).tvShows
                    else tvShowDao.getPopularTvShows(query, page)
                else
                    tvShowDao.getTvShowsByGenre(genre = genre.toString())
            cacheMapper.tvShowCacheMapper.mapFromEntityList(result)
        }
    }

    override suspend fun getNetworkTvShows(page: Int, networkId: Long): List<TvShow> {
        return cacheMapper.tvShowCacheMapper.mapFromEntityList(
            tvShowDao.getNetworkTvShows(
                networkId
            ).tvShows
        )
    }

    override suspend fun getTvShowDetails(tvShowId: Long): TvShow {
        return cacheMapper.mapFromEntityWithCastList(
            tvShowDao.getTvShowWithCast(tvShowId),
            tvShowDao.getSeasons(tvShowId),
            tvShowDao.getTvShowNetworks(tvShowId)
        )
    }

    override suspend fun insertEpisode(episode: Episode, season: Season): Long {
        episode.seasonId = season.id
        return tvShowDao.insert(
            cacheMapper.seasonCacheMapper.episodeCacheMapper.mapToEntity(
                episode
            )
        )
    }

    override suspend fun getEpisodesPerSeason(tvShowId: Long, season: Season): List<Episode> {
        return cacheMapper.seasonCacheMapper.episodeCacheMapper.mapFromEntityList(
            tvShowDao.getEpisodesPerSeason(tvShowId, season.id)
        )
    }
}