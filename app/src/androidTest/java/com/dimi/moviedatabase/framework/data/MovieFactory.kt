package com.dimi.moviedatabase.framework.data

import android.app.Application
import android.content.res.AssetManager
import com.dimi.moviedatabase.business.data.network.responses.MovieSearchResponse
import com.dimi.moviedatabase.business.domain.model.Movie
import com.dimi.moviedatabase.business.domain.state.MediaType
import com.dimi.moviedatabase.framework.network.mappers.movie.MovieCastNetworkMapper
import com.dimi.moviedatabase.framework.network.mappers.movie.MovieDetailsNetworkMapper
import com.dimi.moviedatabase.framework.network.mappers.movie.MovieListNetworkMapper
import com.dimi.moviedatabase.framework.network.mappers.movie.MovieNetworkMapper
import com.dimi.moviedatabase.framework.network.responses.movie.MovieDetailsResponse
import com.dimi.moviedatabase.framework.network.responses.movie.MovieListResponse
import com.dimi.moviedatabase.util.Genre
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

class MovieFactory @Inject constructor(
    private val application: Application,
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

        fun createMovieList(size: Int): List<Movie> {
            return ArrayList<Movie>().apply {
                for (i in 1..size) {
                    this.add(createMovie(i.toLong()))
                }
            }
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

    fun produceMovieDetails(): Movie {
        val mapper = MovieDetailsNetworkMapper(MovieCastNetworkMapper())
        val result: MovieDetailsResponse = Gson()
            .fromJson(
                getDataFromFile("movie_details.json"),
                object : TypeToken<MovieDetailsResponse>() {}.type
            )
        return mapper.mapFromEntity(result)
    }

    private fun getDataFromFile(fileName: String): String? {
        return readJSONFromAsset(fileName)
    }

    private fun readJSONFromAsset(fileName: String): String? {
        var json: String? = null
        json = try {
            val inputStream: InputStream = (application.assets as AssetManager).open(fileName)
            inputStream.bufferedReader().use { it.readText() }
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}