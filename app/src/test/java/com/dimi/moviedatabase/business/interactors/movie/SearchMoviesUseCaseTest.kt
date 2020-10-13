package com.dimi.moviedatabase.business.interactors.movie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dimi.moviedatabase.MainCoroutineRule
import com.dimi.moviedatabase.business.data.network.NetworkErrors
import com.dimi.moviedatabase.business.domain.model.Movie
import com.dimi.moviedatabase.business.domain.state.DataState
import com.dimi.moviedatabase.business.domain.state.MediaType
import com.dimi.moviedatabase.di.DependencyInjection
import com.dimi.moviedatabase.presentation.main.search.state.SearchStateEvent
import com.dimi.moviedatabase.presentation.main.search.state.SearchViewState
import com.dimi.moviedatabase.util.Constants.TEST_FORCE_THROW_EXCEPTION
import com.dimi.moviedatabase.util.Constants.TEST_INVALID_GENRE_ID
import com.dimi.moviedatabase.util.Constants.TEST_INVALID_PAGE
import com.dimi.moviedatabase.util.Constants.TEST_INVALID_SEARCH_QUERY
import com.dimi.moviedatabase.util.Constants.TEST_VALID_GENRE_ID
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchMoviesUseCaseTest {

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
    fun defaultParameters_success_confirmCacheUpdated() {

        runBlocking {

            var returnedList = emptyList<Movie>()
            val stateEvent = SearchStateEvent.SearchMedia()
            SearchMoviesUseCase(
                networkDataSource = dependencyInjection.networkDataSource,
                cacheDataSource = dependencyInjection.cacheDataSource
            ).getResults(
                viewStateKey = MediaType.MOVIE.name,
                stateEvent = stateEvent,
                viewState = SearchViewState()
            ).collect(object : FlowCollector<DataState<SearchViewState>?> {

                override suspend fun emit(value: DataState<SearchViewState>?) {
                    if (value?.stateEvent == stateEvent) {
                        assertThat(value.stateMessage?.response?.message).isEqualTo(
                            SearchMoviesUseCase.SEARCH_SUCCESSFUL
                        )
                        value.data?.mediaList?.let { list ->
                            returnedList = list.map { it as Movie }
                        }
                    }
                }
            })

            assertThat(dependencyInjection.cacheDataSource.getListOfMovies())
                .isEqualTo(
                    returnedList
                )
        }

    }

    @FlowPreview
    @InternalCoroutinesApi
    @Test
    fun invalidPageNumber_fail_confirmCacheUnchanged() {

        runBlocking {

            val stateEvent = SearchStateEvent.SearchMedia()
            SearchMoviesUseCase(
                networkDataSource = dependencyInjection.networkDataSource,
                cacheDataSource = dependencyInjection.cacheDataSource
            ).getResults(
                viewStateKey = MediaType.MOVIE.name,
                stateEvent = stateEvent,
                page = TEST_INVALID_PAGE,
                viewState = SearchViewState()
            ).collect(object : FlowCollector<DataState<SearchViewState>?> {
                override suspend fun emit(value: DataState<SearchViewState>?) {
                    if (value?.stateEvent == stateEvent) {
                        assertThat(value.stateMessage?.response?.message).isEqualTo(
                            SearchMoviesUseCase.SEARCH_FAILED
                        )
                    }
                }
            })

            assertThat(dependencyInjection.cacheDataSource.getListOfMovies(page = TEST_INVALID_PAGE)).isEmpty()
        }
    }

    @FlowPreview
    @InternalCoroutinesApi
    @Test
    fun invalidQuery_fail_confirmCacheUnchanged() {

        runBlocking {

            val stateEvent = SearchStateEvent.SearchMedia()
            SearchMoviesUseCase(
                networkDataSource = dependencyInjection.networkDataSource,
                cacheDataSource = dependencyInjection.cacheDataSource
            ).getResults(
                viewStateKey = MediaType.MOVIE.name,
                stateEvent = stateEvent,
                query = TEST_INVALID_SEARCH_QUERY,
                viewState = SearchViewState()
            ).collect(object : FlowCollector<DataState<SearchViewState>?> {
                override suspend fun emit(value: DataState<SearchViewState>?) {
                    if (value?.stateEvent == stateEvent) {
                        assertThat(value.stateMessage?.response?.message).contains(
                            SearchMoviesUseCase.SEARCH_NO_MATCHING_RESULTS
                        )
                    }
                }
            })

            assertThat(dependencyInjection.cacheDataSource.getListOfMovies()).isEmpty()
        }
    }


    @FlowPreview
    @InternalCoroutinesApi
    @Test
    fun validGenre_success_confirmCacheUpdated() {

        runBlocking {

            var returnedList = emptyList<Movie>()
            val stateEvent = SearchStateEvent.SearchMedia()
            SearchMoviesUseCase(
                networkDataSource = dependencyInjection.networkDataSource,
                cacheDataSource = dependencyInjection.cacheDataSource
            ).getResults(
                viewStateKey = MediaType.MOVIE.name,
                stateEvent = stateEvent,
                genre = TEST_VALID_GENRE_ID,
                viewState = SearchViewState()
            ).collect(object : FlowCollector<DataState<SearchViewState>?> {
                override suspend fun emit(value: DataState<SearchViewState>?) {
                    if (value?.stateEvent == stateEvent) {
                        assertThat(value.stateMessage?.response?.message).contains(
                            SearchMoviesUseCase.SEARCH_SUCCESSFUL
                        )
                        value.data?.mediaList?.let { list ->
                            returnedList = list.map { it as Movie }
                        }
                    }
                }
            })

            assertThat(dependencyInjection.cacheDataSource.getListOfMovies()).isEqualTo(returnedList)
        }
    }

    @FlowPreview
    @InternalCoroutinesApi
    @Test
    fun invalidGenre_fail_confirmCacheUnchanged() {

        runBlocking {

            val stateEvent = SearchStateEvent.SearchMedia()
            SearchMoviesUseCase(
                networkDataSource = dependencyInjection.networkDataSource,
                cacheDataSource = dependencyInjection.cacheDataSource
            ).getResults(
                viewStateKey = MediaType.MOVIE.name,
                stateEvent = stateEvent,
                genre = TEST_INVALID_GENRE_ID,
                viewState = SearchViewState()
            ).collect(object : FlowCollector<DataState<SearchViewState>?> {
                override suspend fun emit(value: DataState<SearchViewState>?) {
                    if (value?.stateEvent == stateEvent) {
                        assertThat(value.stateMessage?.response?.message).contains(
                            SearchMoviesUseCase.SEARCH_FAILED
                        )
                    }
                }
            })

            assertThat(dependencyInjection.cacheDataSource.getListOfMovies()).isEmpty()
        }
    }

    @FlowPreview
    @InternalCoroutinesApi
    @Test
    fun throwException_checkGenericError_confirmCacheUnchanged() {

        runBlocking {

            var haveResult = false
            val stateEvent = SearchStateEvent.SearchMedia()
            SearchMoviesUseCase(
                networkDataSource = dependencyInjection.networkDataSource,
                cacheDataSource = dependencyInjection.cacheDataSource
            ).getResults(
                viewStateKey = MediaType.MOVIE.name,
                stateEvent = stateEvent,
                query = TEST_FORCE_THROW_EXCEPTION.toString(),
                viewState = SearchViewState()
            ).collect(object : FlowCollector<DataState<SearchViewState>?> {
                override suspend fun emit(value: DataState<SearchViewState>?) {
                    if (value?.stateEvent == stateEvent && !haveResult) {
                        haveResult = true
                        assertThat(value.stateMessage?.response?.message).contains(
                            NetworkErrors.NETWORK_ERROR_UNKNOWN
                        )
                    }
                }
            })

            assertThat(dependencyInjection.cacheDataSource.getListOfMovies()).isEmpty()
        }
    }
}