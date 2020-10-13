package com.dimi.moviedatabase.business.data.cache.abstraction

import com.dimi.moviedatabase.business.domain.model.*
import com.dimi.moviedatabase.presentation.main.search.enums.MediaListType
import com.dimi.moviedatabase.util.GENRE_DEFAULT

interface TvShowCacheDataSource {

    suspend fun insertTvShow(
        tvShow: TvShow,
        networkId: Long? = null,
        upsert: Boolean = false,
        mediaListType: MediaListType? = null,
        order: Int? = null): Long

    suspend fun updateTvShow(tvShow: TvShow)

    suspend fun insertTvShows(tvShows: List<TvShow>)

    suspend fun insertCast(person: Person, tvShowId: Long)

    suspend fun insertNetwork(network: Network, tvShowId: Long)

    suspend fun insertSeason(season: Season, tvShowId: Long): Long

    suspend fun getListOfTvShows(
        query: String = "",
        page: Int = 1,
        genre: Int = GENRE_DEFAULT,
        mediaListType: MediaListType? = null,
        network: Network? = null
    ): List<TvShow>

    suspend fun getNetworkTvShows(
        page: Int,
        networkId: Long
    ): List<TvShow>

    suspend fun getTvShowDetails(
        tvShowId: Long
    ) : TvShow

    suspend fun insertEpisode(episode: Episode, season: Season): Long

    suspend fun getEpisodesPerSeason( tvShowId: Long, season: Season): List<Episode>
}