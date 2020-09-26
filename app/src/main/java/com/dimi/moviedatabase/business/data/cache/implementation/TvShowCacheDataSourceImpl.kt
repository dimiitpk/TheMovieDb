package com.dimi.moviedatabase.business.data.cache.implementation

import com.dimi.moviedatabase.business.data.cache.abstraction.TvShowCacheDataSource
import com.dimi.moviedatabase.business.domain.model.*
import com.dimi.moviedatabase.presentation.main.search.enums.MediaListType

class TvShowCacheDataSourceImpl(
    private val tvShowDaoService: TvShowCacheDataSource
) : TvShowCacheDataSource {

    override suspend fun insertTvShow(
        tvShow: TvShow,
        networkId: Long?,
        upsert: Boolean,
        mediaListType: MediaListType?,
        order: Int?
    ): Long {
        return tvShowDaoService.insertTvShow(
            tvShow,
            networkId,
            upsert,
            mediaListType,
            order
        )
    }

    override suspend fun updateTvShow(tvShow: TvShow) {
        tvShowDaoService.updateTvShow(tvShow)
    }

    override suspend fun insertTvShows(tvShows: List<TvShow>) {
        tvShowDaoService.insertTvShows(tvShows)
    }

    override suspend fun insertCast(person: Person, tvShowId: Long) {
        tvShowDaoService.insertCast(person, tvShowId)
    }

    override suspend fun insertNetwork(network: Network, tvShowId: Long) {
        tvShowDaoService.insertNetwork(network, tvShowId)
    }

    override suspend fun insertSeason(season: Season, tvShowId: Long): Long {
        return tvShowDaoService.insertSeason(season, tvShowId)
    }

    override suspend fun getListOfTvShows(
        query: String,
        page: Int,
        genre: Int,
        mediaListType: MediaListType?,
        network: Network?
    ): List<TvShow> {
        return tvShowDaoService.getListOfTvShows(
            query,
            page,
            genre,
            mediaListType,
            network
        )
    }

    override suspend fun getNetworkTvShows(page: Int, networkId: Long): List<TvShow> {
        return tvShowDaoService.getNetworkTvShows(page, networkId)
    }

    override suspend fun getTvShowDetails(tvShowId: Long): TvShow {
        return tvShowDaoService.getTvShowDetails(tvShowId)
    }

    override suspend fun insertEpisode(episode: Episode, season: Season): Long {
        return tvShowDaoService.insertEpisode(episode, season)
    }

    override suspend fun getEpisodesPerSeason(tvShowId: Long, season: Season): List<Episode> {
        return tvShowDaoService.getEpisodesPerSeason(tvShowId, season)
    }
}