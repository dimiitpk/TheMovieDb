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
import com.dimi.moviedatabase.util.Constants.TEST_VALID_MOVIE_ID
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MovieImagesUseCaseTest {

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

    @InternalCoroutinesApi
    @Test
    fun validMovieId_Success() {
        runBlocking {

            val stateEvent = ViewMediaStateEvent.GetImages(
                mediaType = MediaType.MOVIE,
                mediaId = TEST_VALID_MOVIE_ID
            )
            MovieImagesUseCase(
                networkDataSource = dependencyInjection.networkDataSource
            ).getResults(
                viewState = ViewMediaViewState(),
                stateEvent = stateEvent,
                movieId = stateEvent.mediaId.toInt()
            ).collect(object : FlowCollector<DataState<ViewMediaViewState>?> {
                override suspend fun emit(value: DataState<ViewMediaViewState>?) {
                    assertThat(value?.stateMessage?.response?.message).isEqualTo(
                        SharedUseCasesKeys.USE_CASE_IMAGES
                    )
                }
            })
        }
    }

    @InternalCoroutinesApi
    @Test
    fun throwException_checkGenericError() {

        runBlocking {
            val stateEvent = ViewMediaStateEvent.GetImages(
                mediaType = MediaType.MOVIE,
                mediaId = TEST_FORCE_THROW_EXCEPTION.toLong(),
            )
            MovieImagesUseCase(
                networkDataSource = dependencyInjection.networkDataSource
            ).getResults(
                viewState = ViewMediaViewState(),
                stateEvent = stateEvent,
                movieId = stateEvent.mediaId.toInt()
            ).collect(object : FlowCollector<DataState<ViewMediaViewState>?> {
                override suspend fun emit(value: DataState<ViewMediaViewState>?) {
                    assertThat(value?.stateMessage?.response?.message).contains(
                        NetworkErrors.NETWORK_ERROR_UNKNOWN
                    )
                }
            })
        }
    }
}