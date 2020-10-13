package com.dimi.moviedatabase.presentation.main.home

import android.widget.ImageView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.dimi.moviedatabase.R
import com.dimi.moviedatabase.business.domain.model.Media
import com.dimi.moviedatabase.business.domain.state.MediaType
import com.dimi.moviedatabase.framework.data.MovieFactory
import com.dimi.moviedatabase.presentation.common.UIController
import com.dimi.moviedatabase.util.launchFragmentInHiltContainer
import com.dimi.moviedatabase.presentation.common.adapters.MediaListAdapter
import com.dimi.moviedatabase.presentation.main.MainFragmentFactoryTest
import com.dimi.moviedatabase.presentation.main.home.adapter.HomeModel
import com.dimi.moviedatabase.presentation.main.home.adapter.HomeRecyclerAdapter
import com.dimi.moviedatabase.presentation.main.search.enums.ViewType
import com.dimi.moviedatabase.util.recyclerChildAction
import com.dimi.moviedatabase.util.withIndex
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject


@FlowPreview
@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class HomeFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var movieFactory: MovieFactory

    @Inject
    lateinit var fragmentFactory: MainFragmentFactoryTest

    @Before
    fun setup() {
        hiltRule.inject()
        fragmentFactory.uiController = mock(UIController::class.java)
    }

    @Test
    fun clickMovieGenresTextButton_navigateToSearchFragment() {
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<HomeFragment>(fragmentFactory = fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.movie_genres)).perform(click())

        verify(navController).navigate(
            HomeFragmentDirections.actionHomeFragmentToSearchFragment(
                mediaType = MediaType.MOVIE,
                viewType = ViewType.GENRE
            )
        )
    }

    @Test
    fun clickTvShowGenresTextButton_navigateToSearchFragment() {
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<HomeFragment>(fragmentFactory = fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.tv_show_genres)).perform(click())

        verify(navController).navigate(
            HomeFragmentDirections.actionHomeFragmentToSearchFragment(
                mediaType = MediaType.TV_SHOW,
                viewType = ViewType.GENRE
            )
        )
    }

    @FlowPreview
    @Test
    fun clickHomeModeListButton_navigateToSearchFragment() = runBlocking {

        val position = 1
        lateinit var homeModel: HomeModel
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<HomeFragment>(fragmentFactory = fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
            homeModel = this.homeList[position]
        }

        delay(1000)

        val recyclerView = onView(withId((R.id.home_fragment_recycler_view)))

        recyclerView.check(matches(isDisplayed()))

        recyclerView.perform(
            actionOnItemAtPosition<HomeRecyclerAdapter.HomeViewHolder>(
                position,
                recyclerChildAction<ImageView>(R.id.home_arrow_right){ performClick() }
            )
        )

        verify(navController).navigate(
            HomeFragmentDirections.actionHomeFragmentToSearchFragment(
                tabId = homeModel.mediaListType.code,
                mediaType = homeModel.mediaType,
                viewType = ViewType.NONE
            )
        )
    }

    @FlowPreview
    @Test
    fun clickMediaInRecyclerViewItem_navigateToViewMediaFragment() = runBlocking {

        val position = 1
        lateinit var media: Media
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<HomeFragment>(fragmentFactory = fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
            this.homeList[position].list = movieFactory.produceListOfMovies().results.also {
                media = it[position]
            }
        }

        delay(1000)

        onView(withIndex(withId(R.id.home_list_recycler_view), position))
            .perform(
                actionOnItemAtPosition<MediaListAdapter.MediaViewHolder>(position, click())
            )

        verify(navController).navigate(
            HomeFragmentDirections.actionHomeFragmentToViewMediaFragment(
                media.mediaType, media.id
            )
        )
    }
}
