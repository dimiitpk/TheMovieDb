package com.dimi.moviedatabase.framework.network.implementation

import com.dimi.moviedatabase.business.data.network.abstraction.TvShowNetworkDataSource
import com.dimi.moviedatabase.business.data.network.responses.TvShowSearchResponse
import com.dimi.moviedatabase.business.domain.model.*
import com.dimi.moviedatabase.framework.network.NetworkConstants
import com.dimi.moviedatabase.framework.network.api.TvShowApi
import com.dimi.moviedatabase.framework.network.mappers.NetworkMapper
import com.dimi.moviedatabase.presentation.common.enums.SortFilter
import com.dimi.moviedatabase.presentation.common.enums.SortOrder
import com.dimi.moviedatabase.presentation.main.search.enums.MediaListType
import com.dimi.moviedatabase.util.GENRE_DEFAULT
import com.dimi.moviedatabase.util.Keys
import kotlinx.coroutines.FlowPreview

@FlowPreview
class TvShowApiServiceImpl(
    private val tvShowApi: TvShowApi,
    private val networkMapper: NetworkMapper
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

        val request = if (mediaListType != null) {
            when (mediaListType) {
                MediaListType.POPULAR -> tvShowApi.getPopularTvShows(
                    apiKey = Keys.apiKey(),
                    page = page,
                    language = NetworkConstants.LANGUAGE_DEFAULT
                )
                MediaListType.UPCOMING_OR_ON_THE_AIR -> tvShowApi.getOnTheAirTvShows(
                    apiKey = Keys.apiKey(),
                    page = page,
                    language = NetworkConstants.LANGUAGE_DEFAULT
                )
                MediaListType.TRENDING -> tvShowApi.getTrendingTvShows(
                    page = page,
                    apiKey = Keys.apiKey(),
                )
                MediaListType.TOP_RATED -> tvShowApi.getTopRatedTvShows(
                    page = page,
                    apiKey = Keys.apiKey(),
                    language = NetworkConstants.LANGUAGE_DEFAULT
                )
            }
        } else
            if (genre == GENRE_DEFAULT) {
                if (query.isBlank())
                    if (network != null)
                        tvShowApi.getTvShowsByNetwork(
                            apiKey = Keys.apiKey(),
                            language = NetworkConstants.LANGUAGE_DEFAULT,
                            sortBy = "${if (sortFilter == SortFilter.BY_FIRST_AIR_DATE) "first_air_date" else sortFilter.value}.${sortOrder.value}",
                            firstAirDates = NetworkConstants.FIRST_AIR_DEFAULT,
                            timeZone = NetworkConstants.TIMEZONE_DEFAULT,
                            page = page,
                            networkId = network.id
                        )
                    else
                        tvShowApi.getPopularTvShows(
                            page = page,
                            apiKey = Keys.apiKey(),
                            language = NetworkConstants.LANGUAGE_DEFAULT
                        )
                else
                    tvShowApi.searchTvShowQuery(
                        apiKey = Keys.apiKey(),
                        language = NetworkConstants.LANGUAGE_DEFAULT,
                        page = page,
                        include_adult = NetworkConstants.ADULT_DEFAULT,
                        query = query
                    )
            } else
                tvShowApi.getTvShowsByGenre(
                    apiKey = Keys.apiKey(),
                    language = NetworkConstants.LANGUAGE_DEFAULT,
                    sortBy = "${if (sortFilter == SortFilter.BY_FIRST_AIR_DATE) "first_air_date" else sortFilter.value}.${sortOrder.value}",
                    firstAirDates = NetworkConstants.FIRST_AIR_DEFAULT,
                    timeZone = NetworkConstants.TIMEZONE_DEFAULT,
                    page = page,
                    genre = genre
                )

        return networkMapper.tvShowListNetworkMapper.mapFromEntity(request)
    }

    override suspend fun getSimilarTvShows(tvShowId: Long, page: Int): TvShowSearchResponse {
        return networkMapper.tvShowListNetworkMapper.mapFromEntity(
            tvShowApi.getSimilarTvShows(
                tvShowId = tvShowId,
                page = page,
                apiKey = Keys.apiKey()
            )
        )
    }

    override suspend fun getTvShowRecommendations(tvShowId: Long, page: Int): TvShowSearchResponse {
        return networkMapper.tvShowListNetworkMapper.mapFromEntity(
            tvShowApi.getTvShowRecommendations(
                tvShowId = tvShowId,
                page = page,
                apiKey = Keys.apiKey()
            )
        )
    }

    override suspend fun getTvShowDetails(tvShowId: Long): TvShow {
        return networkMapper.tvShowDetailsNetworkMapper.mapFromEntity(
            tvShowApi.getTVShowDetails(
                tvShowId = tvShowId,
                language = NetworkConstants.LANGUAGE_DEFAULT,
                apiKey = Keys.apiKey(),
                appendTo = "credits"
            )
        )
    }

    override suspend fun getTvShowVideos(tvShowId: Long): List<Video> {
        return networkMapper.tvShowDetailsNetworkMapper.toYoutubeTrailersList(
            tvShowApi.getVideosByTvShowId(
                tvShowId = tvShowId,
                apiKey = Keys.apiKey(),
                language = NetworkConstants.LANGUAGE_DEFAULT
            )
        )
    }

    override suspend fun getTvShowImages(tvShowId: Long): List<Image> {
        val response = tvShowApi.getTvShowImages(
            tvShowId = tvShowId,
            apiKey = Keys.apiKey(),
            includeImage = NetworkConstants.INCLUDE_IMAGE_DEFAULT
        )
        return networkMapper.imageMapper.mapFromEntityList(
            response.backdrops + response.posters
        )
    }

    override suspend fun getEpisodesPerSeason(tvShowId: Long, season: Int): List<Episode> {
        return networkMapper.tvShowDetailsNetworkMapper.tvShowSeasonNetworkMapper.mapToEpisodeList(
            tvShowApi.getTvSeasonEpisodes(
                tvShowId = tvShowId,
                apiKey = Keys.apiKey(),
                seasonNumber = season,
                language = NetworkConstants.LANGUAGE_DEFAULT
            )
        )
    }
}