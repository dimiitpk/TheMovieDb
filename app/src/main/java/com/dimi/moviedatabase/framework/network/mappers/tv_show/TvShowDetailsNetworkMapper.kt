package com.dimi.moviedatabase.framework.network.mappers.tv_show

import com.dimi.moviedatabase.business.domain.model.TvShow
import com.dimi.moviedatabase.business.domain.model.Video
import com.dimi.moviedatabase.business.domain.util.EntityMapper
import com.dimi.moviedatabase.framework.network.model.VideoNetworkEntity
import com.dimi.moviedatabase.framework.network.responses.common.CreditsResponse
import com.dimi.moviedatabase.framework.network.responses.common.GenreResponse
import com.dimi.moviedatabase.framework.network.responses.common.VideoListResponse
import com.dimi.moviedatabase.framework.network.responses.tv_show.TvShowDetailsResponse
import com.dimi.moviedatabase.util.toDate
import javax.inject.Inject

class TvShowDetailsNetworkMapper
@Inject
constructor(
    private val tvShowCastNetworkMapper: TvShowCastNetworkMapper,
    private val tvNetworkMapper: TvNetworkMapper,
    val tvShowSeasonNetworkMapper: TvShowSeasonNetworkMapper
) :
    EntityMapper<TvShowDetailsResponse, TvShow> {

    override fun mapFromEntity(entity: TvShowDetailsResponse): TvShow {
        return TvShow(
            id = entity.id,
            title = entity.title,
            genres = mapGenreResponseToList(entity.genres),
            overview = entity.overview,
            popularity = entity.popularity,
            numberOfEpisodes = entity.numberOfEpisodes,
            numberOfSeasons = entity.numberOfSeasons,
            posterPath = entity.posterPath,
            firstAirDate = entity.firstAirDate.toDate(),
            voteAverage = entity.voteAverage,
            backdropPath = entity.backdropPath,
            voteCount = entity.voteCount,
            castList = tvShowCastNetworkMapper.mapFromEntityList(
                entities = entity.creditsResponse.cast
            ),
            seasons = tvShowSeasonNetworkMapper.mapFromEntityList(
                entities = entity.seasons,
                tvShowId = entity.id
            ),
            networks = tvNetworkMapper.mapFromEntityList(
                entity.networks
            ),
            runtime = if (entity.runtime.isNotEmpty()) entity.runtime[0] else null,
            homepage = entity.homepage,
            originalTitle = entity.originalName,
            type = entity.type,
            status = entity.status,
            lastAirDate = entity.lastAirDate.toDate()
        )
    }

    override fun mapToEntity(domainModel: TvShow): TvShowDetailsResponse {
        return TvShowDetailsResponse(
            id = 0,
            backdropPath = "",
            posterPath = "",
            title = "",
            genres = mapGenreResponseToAList(domainModel.genres!!),
            overview = "",
            popularity = 0.0,
            numberOfSeasons = 0,
            numberOfEpisodes = 0,
            firstAirDate = "",
            voteAverage = 0f,
            voteCount = 0,
            creditsResponse = CreditsResponse(tvShowCastNetworkMapper.mapToEntityList(domainModel.castList!!)),
            status = "",
            runtime = arrayListOf(),
            homepage = "",
            type = "",
            lastAirDate = "",
            networks = arrayListOf(),
            originalName = "",
            seasons = arrayListOf()
        )
    }

    fun toYoutubeTrailersList(response: VideoListResponse): ArrayList<Video> {
        val trailerList: ArrayList<Video> = ArrayList()
        for (video in response.results) {
            if (video.site == "YouTube")
                trailerList.add(mapToYoutubeTrailer(video, response.id))

        }
        return trailerList
    }

    private fun mapToYoutubeTrailer(video: VideoNetworkEntity, movieId: Int): Video {
        return Video(
            id = video.id,
            movieId = movieId,
            key = video.key,
            name = video.name,
            site = video.site,
            type = video.type
        )
    }

    private fun mapGenreResponseToList(genreList: List<GenreResponse>): ArrayList<Int> {
        val genresIds = ArrayList<Int>()
        for (genre in genreList) {
            genresIds.add(genre.id)
        }
        return genresIds
    }

    private fun mapGenreResponseToAList(genreList: List<Int>): ArrayList<GenreResponse> {
        val genresIds = ArrayList<GenreResponse>()
        for (genre in genreList) {
            genresIds.add(GenreResponse(id = genre, name = ""))
        }
        return genresIds
    }
}