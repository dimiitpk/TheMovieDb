package com.dimi.moviedatabase.business.data.network.implemention

import com.dimi.moviedatabase.business.data.network.abstraction.TvShowNetworkDataSource
import com.dimi.moviedatabase.business.data.network.responses.TvShowSearchResponse
import com.dimi.moviedatabase.business.domain.model.*
import com.dimi.moviedatabase.presentation.main.search.SortFilter
import com.dimi.moviedatabase.presentation.main.search.SortOrder
import com.dimi.moviedatabase.presentation.main.search.enums.MediaListType

class TvShowNetworkDataSourceImpl(
    private val mainApiService: TvShowNetworkDataSource
) : TvShowNetworkDataSource {

    override suspend fun getListOfTvShows(
        query: String,
        page: Int,
        genre: Int,
        mediaListType: MediaListType?,
        network: Network?,
        sortFilter: SortFilter,
        sortOrder: SortOrder
    ): TvShowSearchResponse {
        return mainApiService.getListOfTvShows(query, page, genre, mediaListType, network, sortFilter, sortOrder)
    }

    override suspend fun getSimilarTvShows(tvShowId: Long, page: Int): TvShowSearchResponse {
        return mainApiService.getSimilarTvShows(tvShowId, page)
    }

    override suspend fun getTvShowRecommendations(tvShowId: Long, page: Int): TvShowSearchResponse {
        return mainApiService.getTvShowRecommendations(tvShowId, page)
    }

    override suspend fun getTvShowDetails(tvShowId: Long): TvShow {
        return mainApiService.getTvShowDetails(tvShowId)
    }

    override suspend fun getTvShowVideos(tvShowId: Long): List<Video> {
        return mainApiService.getTvShowVideos(tvShowId)
    }

    override suspend fun getTvShowImages(tvShowId: Long): List<Image> {
        return mainApiService.getTvShowImages(tvShowId)
    }

    override suspend fun getEpisodesPerSeason(tvShowId: Long, season: Int): List<Episode> {
        return mainApiService.getEpisodesPerSeason(tvShowId, season)
    }
}





























