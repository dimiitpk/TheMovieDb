package com.dimi.moviedatabase.framework.cache

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.dimi.moviedatabase.business.data.cache.abstraction.MovieCacheDataSource
import com.dimi.moviedatabase.framework.cache.database.AppDatabase
import com.dimi.moviedatabase.framework.cache.implementation.MovieDaoServiceImpl
import com.dimi.moviedatabase.framework.cache.mappers.CacheMapper
import com.dimi.moviedatabase.framework.data.MovieFactory
import com.dimi.moviedatabase.framework.data.PeopleFactory
import com.dimi.moviedatabase.presentation.main.search.enums.MediaListType
import com.dimi.moviedatabase.util.AndroidTestConstants.TEST_INVALID_MOVIE_ID
import com.dimi.moviedatabase.util.AndroidTestConstants.TEST_VALID_MOVIE_ID
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class MovieDaoServiceTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: AppDatabase

    @Inject
    lateinit var cacheMapper: CacheMapper

    @Inject
    lateinit var peopleFactory: PeopleFactory

    private lateinit var movieDaoService: MovieCacheDataSource

    @Before
    fun setup() {
        hiltRule.inject()
        movieDaoService = MovieDaoServiceImpl(
            database.movieDao(), database.peopleDao(), cacheMapper
        )
    }

    @Test
    fun insertMovie() = runBlocking {

        val movie = MovieFactory.createMovie(TEST_VALID_MOVIE_ID)
        movieDaoService.insertMovie(movie)

        val movieFromCache =
            movieDaoService.getMovie(TEST_VALID_MOVIE_ID)

        assertThat(movieFromCache).isEqualTo(movie)
    }

    @Test
    fun insertMovieInMediaList() {
        runBlocking {

            val movie = MovieFactory.createMovie(TEST_VALID_MOVIE_ID)
            val movie1 = MovieFactory.createMovie(12)
            val movie2 = MovieFactory.createMovie(23)
            val movie3 = MovieFactory.createMovie(44)
            movieDaoService.insertMovie(
                movie = movie,
                upsert = false,
                mediaListType = MediaListType.POPULAR,
                order = 0
            )
            movieDaoService.insertMovie(
                movie = movie1,
                upsert = false,
                mediaListType = MediaListType.POPULAR,
                order = 1
            )
            movieDaoService.insertMovie(
                movie = movie2,
                upsert = false,
                mediaListType = MediaListType.POPULAR,
                order = 2
            )
            movieDaoService.insertMovie(
                movie = movie3,
                upsert = false,
                mediaListType = MediaListType.POPULAR,
                order = 3
            )

            val movieFromCache =
                movieDaoService.getListOfMovies(mediaListType = MediaListType.POPULAR)

            assertThat(movieFromCache).hasSize(4)
        }
    }

    @Test
    fun insertCast()  {
        runBlocking {

            val movie = MovieFactory.createMovie(TEST_VALID_MOVIE_ID)
            movieDaoService.insertMovie(movie)
            val person = peopleFactory.createPerson(1)
            movieDaoService.insertCast(person, movie.id)

            val newMovie = movieDaoService.getMovie(movie.id)
            assertThat(newMovie.castList).contains(person)
        }
    }

    @Test
    fun insertMovies() {
        runBlocking {

            val movie = MovieFactory.createMovie(TEST_VALID_MOVIE_ID)
            val movie2 = MovieFactory.createMovie(TEST_INVALID_MOVIE_ID)

            movieDaoService.insertMovies(listOf(movie, movie2))


            val moviesFromCache = movieDaoService.getListOfMovies()

            assertThat(moviesFromCache).containsExactly(movie, movie2)
        }
    }

    @Test
    fun updateMovie() {
        runBlocking {
            val movie = MovieFactory.createMovie(TEST_VALID_MOVIE_ID)
            movieDaoService.insertMovie(movie)

            movie.tagLine = "Test From this Method"
            movieDaoService.updateMovie(movie)

            assertThat(movie.tagLine).isEqualTo(movieDaoService.getMovie(TEST_VALID_MOVIE_ID).tagLine)
        }
    }


    @Test
    fun getMovie() {
        runBlocking {
            val movie = MovieFactory.createMovie(TEST_VALID_MOVIE_ID)
            movieDaoService.insertMovie(movie)

            assertThat(movie).isEqualTo(movieDaoService.getMovie(TEST_VALID_MOVIE_ID))
        }
    }

    @Test
    fun getListOfMovies() {

        runBlocking {
            val movies = MovieFactory.createMovieList(100)
            movieDaoService.insertMovies(movies)

            assertThat(movies).containsAtLeastElementsIn(movieDaoService.getListOfMovies())
        }
    }
}