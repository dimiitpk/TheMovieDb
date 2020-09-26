package com.dimi.moviedatabase.framework.network.mappers.movie

import com.dimi.moviedatabase.business.domain.model.Movie
import com.dimi.moviedatabase.business.domain.model.Video
import com.dimi.moviedatabase.business.domain.util.EntityMapper
import com.dimi.moviedatabase.framework.network.model.VideoNetworkEntity
import com.dimi.moviedatabase.framework.network.responses.common.CreditsResponse
import com.dimi.moviedatabase.framework.network.responses.common.GenreResponse
import com.dimi.moviedatabase.framework.network.responses.common.VideoListResponse
import com.dimi.moviedatabase.framework.network.responses.movie.MovieDetailsResponse
import com.dimi.moviedatabase.util.toDate
import javax.inject.Inject

class MovieDetailsNetworkMapper
@Inject
constructor(private val movieCastNetworkMapper: MovieCastNetworkMapper) :
    EntityMapper<MovieDetailsResponse, Movie> {

    override fun mapFromEntity(entity: MovieDetailsResponse): Movie {
        return Movie(
            id = entity.id,
            title = entity.title,
            genres = mapGenreResponseToList(entity.genres),
            overview = entity.overview,
            popularity = entity.popularity,
            posterPath = entity.posterPath,
            releaseDate = entity.releaseDate.toDate(),
            voteAverage = entity.voteAverage,
            backdropPath = entity.backdropPath,
            voteCount = entity.voteCount,
            tagLine = entity.tagLine,
            castList = movieCastNetworkMapper.mapFromEntityList(
                entities = entity.creditsResponse.cast
            ),
            status = entity.status,
            homepage = entity.homepage,
            runtime = entity.runtime,
            revenue = entity.revenue,
            originalTitle = entity.originalTitle,
            imdbId = entity.imdbId,
            budget = entity.budget
        )
    }

    override fun mapToEntity(domainModel: Movie): MovieDetailsResponse {
        return MovieDetailsResponse(
            id = 0,
            backdropPath = "",
            posterPath = "",
            title = "",
            genres = mapGenreResponseToAList(domainModel.genres!!),
            overview = "",
            popularity = 0.0,
            releaseDate = "",
            tagLine = "",
            voteAverage = 0f,
            voteCount = 0,
            creditsResponse = CreditsResponse(movieCastNetworkMapper.mapToEntityList(domainModel.castList!!)),
            budget = 0,
            homepage = "",
            imdbId = "",
            originalTitle = "",
            revenue = 0,
            runtime = 0,
            status = ""
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