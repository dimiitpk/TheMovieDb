package com.dimi.moviedatabase.business.interactors.movie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dimi.moviedatabase.MainCoroutineRule
import com.dimi.moviedatabase.business.data.network.NetworkErrors
import com.dimi.moviedatabase.business.domain.state.DataState
import com.dimi.moviedatabase.business.domain.state.MediaType
import com.dimi.moviedatabase.business.interactors.SharedUseCasesKeys
import com.dimi.moviedatabase.di.DependencyInjection
import com.dimi.moviedatabase.presentation.main.view.state.ViewMediaStateEvent
import com.dimi.moviedatabase.presentation.main.view.state.ViewMediaViewState
import com.dimi.moviedatabase.util.Constants.TEST_FORCE_THROW_EXCEPTION
import com.dimi.moviedatabase.util.Constants.TEST_INVALID_MOVIE_ID
import com.dimi.moviedatabase.util.Constants.TEST_VALID_MOVIE_ID
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.FlowCollector
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MovieDetailsUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var dependencyInjection: DependencyInjection

    @Before
    fun setup() {
        dependencyInjection = DependencyInjection()
        dependencyInjection.inject(this::class.java.name)
    }

    @FlowPreview
    @InternalCoroutinesApi
    @Test
    fun getMovieDetails_success_confirmCacheUpdate() {
        runBlocking {

            val movieFromNetwork = dependencyInjection.movieFactory.produceMovieDetails()

            val stateEvent = ViewMediaStateEvent.GetDetails(
                mediaType = MediaType.MOVIE,
                mediaId = TEST_VALID_MOVIE_ID
            )
            MovieDetailsUseCase(
                cacheDataSource = dependencyInjection.cacheDataSource,
                networkDataSource = dependencyInjection.networkDataSource
            ).getResults(
                viewState = ViewMediaViewState(),
                viewStateKey = ViewMediaViewState.MEDIA_DETAILS,
                movieId = stateEvent.mediaId,
                stateEvent = stateEvent
            ).collect(object : FlowCollector<DataState<ViewMediaViewState>?> {
                override suspend fun emit(value: DataState<ViewMediaViewState>?) {
                   if (value?.stateEvent == stateEvent) {
                        assertThat(value.stateMessage?.response?.message).isEqualTo(
                            SharedUseCasesKeys.MESSAGE_DETAILS_QUERY_SUCCESSFUL
                        )
                   }
                }
            })

            // confirm cache was updated
            val movieFromCache = dependencyInjection.cacheDataSource.getMovie(TEST_VALID_MOVIE_ID)
            assertThat(movieFromCache).isEqualTo(movieFromNetwork)
        }
    }

    @FlowPreview
    @InternalCoroutinesApi
    @Test
    fun throwException_checkGenericError_confirmCacheUnchanged() {
        runBlocking {

            var haveResult = false
            val movieFromNetwork = dependencyInjection.movieFactory.produceMovieDetails()

            val stateEvent = ViewMediaStateEvent.GetDetails(
                mediaType = MediaType.MOVIE,
                mediaId = TEST_FORCE_THROW_EXCEPTION.toLong()
            )
            MovieDetailsUseCase(
                cacheDataSource = dependencyInjection.cacheDataSource,
                networkDataSource = dependencyInjection.networkDataSource
            ).getResults(
                viewState = ViewMediaViewState(),
                viewStateKey = ViewMediaViewState.MEDIA_DETAILS,
                movieId = stateEvent.mediaId,
                stateEvent = stateEvent
            ).collect(object : FlowCollector<DataState<ViewMediaViewState>?> {
                override suspend fun emit(value: DataState<ViewMediaViewState>?) {
                    if (value?.stateEvent == stateEvent && !haveResult) {
                        haveResult = true
                        assertThat(value.stateMessage?.response?.message).contains(
                            NetworkErrors.NETWORK_ERROR_UNKNOWN
                        )
                    }
                }
            })


            val movieFromCache = dependencyInjection.cacheDataSource.getMovie(TEST_VALID_MOVIE_ID)
            assertThat(movieFromCache).isNotEqualTo(movieFromNetwork)
        }
    }

    @FlowPreview
    @InternalCoroutinesApi
    @Test
    fun getMovieDetailsInvalidMovieId_fail_confirmCacheUnchanged() {
        runBlocking {

            val movieFromNetwork = dependencyInjection.movieFactory.produceMovieDetails()

            val stateEvent = ViewMediaStateEvent.GetDetails(
                mediaType = MediaType.MOVIE,
                mediaId = TEST_INVALID_MOVIE_ID
            )
            MovieDetailsUseCase(
                cacheDataSource = dependencyInjection.cacheDataSource,
                networkDataSource = dependencyInjection.networkDataSource
            ).getResults(
                viewState = ViewMediaViewState(),
                viewStateKey = ViewMediaViewState.MEDIA_DETAILS,
                movieId = stateEvent.mediaId,
                stateEvent = stateEvent
            ).collect(object : FlowCollector<DataState<ViewMediaViewState>?> {
                override suspend fun emit(value: DataState<ViewMediaViewState>?) {
                    if (value?.stateEvent == stateEvent) {
                        assertThat(value.stateMessage?.response?.message).isEqualTo(
                            MovieDetailsUseCase.DETAILS_FAILED
                        )
                    }
                }
            })

            // confirm cache was updated
            val movieFromCache = dependencyInjection.cacheDataSource.getMovie(TEST_VALID_MOVIE_ID)
            assertThat(movieFromCache).isNotEqualTo(movieFromNetwork)
        }
    }
}