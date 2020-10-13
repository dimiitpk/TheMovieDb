package com.dimi.moviedatabase.business.data

import com.dimi.moviedatabase.business.data.network.responses.MovieSearchResponse
import com.dimi.moviedatabase.business.domain.model.Image
import com.dimi.moviedatabase.business.domain.model.Movie
import com.dimi.moviedatabase.business.domain.model.Video
import com.dimi.moviedatabase.business.domain.state.MediaType
import com.dimi.moviedatabase.framework.network.mappers.ImageMapper
import com.dimi.moviedatabase.framework.network.mappers.movie.MovieCastNetworkMapper
import com.dimi.moviedatabase.framework.network.mappers.movie.MovieDetailsNetworkMapper
import com.dimi.moviedatabase.framework.network.mappers.movie.MovieListNetworkMapper
import com.dimi.moviedatabase.framework.network.mappers.movie.MovieNetworkMapper
import com.dimi.moviedatabase.framework.network.responses.common.ImagesResponse
import com.dimi.moviedatabase.framework.network.responses.common.VideoListResponse
import com.dimi.moviedatabase.framework.network.responses.movie.MovieDetailsResponse
import com.dimi.moviedatabase.framework.network.responses.movie.MovieListResponse
import com.dimi.moviedatabase.util.Genre
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class MovieFactory(
    private val testClassLoader: ClassLoader
) {

    companion object {
        fun createMovie(id: Long): Movie {
            return Movie(
                id = id,
                title = "Title",
                popularity = 3.2,
                voteCount = 424,
                voteAverage = 4.5f,
                overview = "Overview",
                posterPath = "random url",
                tagLine = "empty",
                backdropPath = "random url",
                genres = Genre(MediaType.MOVIE).getAllGenreIds().shuffled().slice(0..2)
            )
        }
    }

    fun produceListOfMovies(): MovieSearchResponse {
        val mapper = MovieListNetworkMapper(MovieNetworkMapper())
        val result: MovieListResponse = Gson()
            .fromJson(
                getDataFromFile("popular_movies.json"),
                object : TypeToken<MovieListResponse>() {}.type
            )
        return mapper.mapFromEntity(result)
    }

    fun produceListOfMoviesByQuery(): MovieSearchResponse {
        val mapper = MovieListNetworkMapper(MovieNetworkMapper())
        val result: MovieListResponse = Gson()
            .fromJson(
                getDataFromFile("movie_search.json"),
                object : TypeToken<MovieListResponse>() {}.type
            )
        return mapper.mapFromEntity(result)
    }

    fun produceListOfMovieTrailers(): List<Video> {
        val mapper = MovieDetailsNetworkMapper(MovieCastNetworkMapper())
        val result: VideoListResponse = Gson()
            .fromJson(
                getDataFromFile("movie_trailers.json"),
                object : TypeToken<VideoListResponse>() {}.type
            )
        return mapper.toYoutubeTrailersList(result)
    }

    fun produceListOfMovieImages(): List<Image> {
        val mapper = ImageMapper()
        val result: ImagesResponse = Gson()
            .fromJson(
                getDataFromFile("movie_images.json"),
                object : TypeToken<ImagesResponse>() {}.type
            )
        return mapper.mapFromEntityList(result.posters)
    }


    fun produceListOfMoviesByGenreId(): MovieSearchResponse {
        val mapper = MovieListNetworkMapper(MovieNetworkMapper())
        val result: MovieListResponse = Gson()
            .fromJson(
                getDataFromFile("movie_by_genre.json"),
                object : TypeToken<MovieListResponse>() {}.type
            )
        return mapper.mapFromEntity(result)
    }

    fun produceMovieDetails(): Movie {
        val mapper = MovieDetailsNetworkMapper(MovieCastNetworkMapper())
        val result: MovieDetailsResponse = Gson()
            .fromJson(
                getDataFromFile("movie_details.json"),
                object : TypeToken<MovieDetailsResponse>() {}.type
            )
        return mapper.mapFromEntity(result)
    }

    private fun getDataFromFile(fileName: String): String {
        return testClassLoader.getResource(fileName).readText()
    }
}