package com.dimi.moviedatabase.business.data.network.abstraction

import com.dimi.moviedatabase.business.data.network.responses.TvShowSearchResponse
import com.dimi.moviedatabase.business.domain.model.*
import com.dimi.moviedatabase.presentation.common.enums.SortFilter
import com.dimi.moviedatabase.presentation.common.enums.SortOrder
import com.dimi.moviedatabase.presentation.main.search.enums.MediaListType

interface TvShowNetworkDataSource {

    suspend fun getListOfTvShows(
        query: String,
        page: Int,
        genre: Int,
        mediaListType: MediaListType? = null,
        network: Network?,
        sortFilter: SortFilter,
        sortOrder: SortOrder
    ): TvShowSearchResponse

    suspend fun getSimilarTvShows(
        tvShowId: Long,
        page: Int
    ): TvShowSearchResponse

    suspend fun getTvShowRecommendations(
        tvShowId: Long,
        page: Int
    ): TvShowSearchResponse

    suspend fun getTvShowDetails(
        tvShowId: Long
    ): TvShow

    suspend fun getTvShowVideos(
        tvShowId: Long
    ): List<Video>

    suspend fun getTvShowImages(
        tvShowId: Long
    ): List<Image>

    suspend fun getEpisodesPerSeason(
        tvShowId: Long,
        season: Int
    ): List<Episode>
}
