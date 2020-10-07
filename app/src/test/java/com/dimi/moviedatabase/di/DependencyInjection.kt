package com.dimi.moviedatabase.di

import com.dimi.moviedatabase.business.data.MovieFactory
import com.dimi.moviedatabase.business.data.cache.FakeMovieCacheDataSourceImpl
import com.dimi.moviedatabase.business.data.cache.abstraction.MovieCacheDataSource
import com.dimi.moviedatabase.business.data.network.FakeMovieNetworkDataSourceImpl
import com.dimi.moviedatabase.business.data.network.abstraction.MovieNetworkDataSource
import com.dimi.moviedatabase.business.interactors.movie.MovieDetailsUseCase
import com.dimi.moviedatabase.business.interactors.movie.SearchMoviesUseCaseTest
import com.dimi.moviedatabase.framework.network.mappers.NetworkMapper
import com.dimi.moviedatabase.util.isUnitTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class DependencyInjection {

    lateinit var cacheDataSource: MovieCacheDataSource
    lateinit var networkDataSource: MovieNetworkDataSource

    lateinit var movieFactory: MovieFactory

    init {
        isUnitTest = true
    }

    fun inject(className: String) {
        this.javaClass.classLoader?.let { classLoader ->
            movieFactory = MovieFactory(classLoader)
        }

        val movieSearchResponse = movieFactory.produceListOfMovies()

        cacheDataSource = FakeMovieCacheDataSourceImpl(
            if (className == MovieDetailsUseCase::class.java.name) {
                ArrayList(movieSearchResponse.results)
            } else ArrayList()
        )
        networkDataSource = FakeMovieNetworkDataSourceImpl(
            movieSearchResponse = movieSearchResponse,
            movieGenreSearchResponse = movieFactory.produceListOfMoviesByGenreId(),
            movieQuerySearchResponse = movieFactory.produceListOfMoviesByQuery(),
            movieTrailers = movieFactory.produceListOfMovieTrailers(),
            movieImages = movieFactory.produceListOfMovieImages(),
            movieDetails = movieFactory.produceMovieDetails()
        )
    }
}