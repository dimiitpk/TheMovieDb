package com.dimi.moviedatabase.presentation.main.home

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dimi.moviedatabase.R
import com.dimi.moviedatabase.business.domain.model.*
import com.dimi.moviedatabase.business.domain.state.MediaType
import com.dimi.moviedatabase.business.domain.state.MessageType
import com.dimi.moviedatabase.business.domain.state.StateMessageCallback
import com.dimi.moviedatabase.business.domain.state.UIComponentType
import com.dimi.moviedatabase.presentation.GlideApp
import com.dimi.moviedatabase.presentation.main.BaseMainFragment
import com.dimi.moviedatabase.presentation.main.MainActivity
import com.dimi.moviedatabase.presentation.common.UIController
import com.dimi.moviedatabase.presentation.main.home.adapter.HomeModel
import com.dimi.moviedatabase.presentation.main.home.adapter.HomeRecyclerAdapter
import com.dimi.moviedatabase.presentation.main.home.state.HomeStateEvent
import com.dimi.moviedatabase.presentation.main.home.viewmodel.*
import com.dimi.moviedatabase.presentation.main.search.SearchFragmentArgs
import com.dimi.moviedatabase.presentation.main.search.enums.MediaListType
import com.dimi.moviedatabase.presentation.main.search.enums.ViewType
import com.dimi.moviedatabase.presentation.main.view.ViewMediaFragmentArgs
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_search.recycler_view
import kotlinx.android.synthetic.main.layout_home_toolbar.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.lang.ClassCastException

@FlowPreview
@ExperimentalCoroutinesApi
class HomeFragment : BaseMainFragment(R.layout.fragment_home), HomeRecyclerAdapter.Interaction {

    val viewModel: HomeViewModel by viewModels()

    private var loadingProgress: Int = 0

    private lateinit var recyclerAdapter: HomeRecyclerAdapter

    private var _homeList: List<HomeModel> = listOf(
        HomeModel(
            list = ArrayList(),
            MediaListType.POPULAR.originalName,
            MediaType.MOVIE,
            MediaType.MOVIE.pluralName,
            MediaListType.POPULAR
        ),
        HomeModel(
            list = ArrayList(),
            MediaListType.POPULAR.originalName,
            MediaType.TV_SHOW,
            MediaType.TV_SHOW.pluralName,
            MediaListType.POPULAR
        ),
        HomeModel(
            list = ArrayList(),
            MediaListType.TOP_RATED.originalName,
            MediaType.MOVIE,
            MediaType.MOVIE.pluralName,
            MediaListType.TOP_RATED
        ),
        HomeModel(
            list = ArrayList(),
            MediaListType.TOP_RATED.originalName,
            MediaType.TV_SHOW,
            MediaType.TV_SHOW.pluralName,
            MediaListType.TOP_RATED
        ),
        HomeModel(
            list = ArrayList(),
            MediaListType.TRENDING.originalName,
            MediaType.MOVIE,
            MediaType.MOVIE.pluralName,
            MediaListType.TRENDING
        ),
        HomeModel(
            list = ArrayList(),
            MediaListType.TRENDING.originalName,
            MediaType.TV_SHOW,
            MediaType.TV_SHOW.pluralName,
            MediaListType.TRENDING
        ),
        HomeModel(
            list = ArrayList(),
            MediaListType.UPCOMING_OR_ON_THE_AIR.originalName,
            MediaType.MOVIE,
            MediaType.MOVIE.pluralName,
            MediaListType.UPCOMING_OR_ON_THE_AIR
        ),
        HomeModel(
            list = ArrayList(),
            "On The Air",
            MediaType.TV_SHOW,
            MediaType.TV_SHOW.pluralName,
            MediaListType.UPCOMING_OR_ON_THE_AIR
        )
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupActionBar()

        subscribeObservers()
        initRecyclerView()
    }

    private fun setupActionBar() {
        view?.let { v ->

            val view = View.inflate(
                v.context,
                R.layout.layout_home_toolbar,
                null
            )
            view.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            toolbar_content_container.addView(view)
            toolbar_content_container.search_button.setOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToSearchFragment(
                        viewType = ViewType.SEARCH
                    )
                )
            }
            toolbar_content_container.movie_genres.setOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToSearchFragment(
                        mediaType = MediaType.MOVIE,
                        viewType = ViewType.GENRE
                    )
                )
            }
            toolbar_content_container.tv_show_genres.setOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToSearchFragment(
                        mediaType = MediaType.TV_SHOW,
                        viewType = ViewType.GENRE
                    )
                )
            }
        }
    }


    private fun initRecyclerView() {

        recycler_view.apply {

            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

            recyclerAdapter =
                HomeRecyclerAdapter(
                    GlideApp.with(this@HomeFragment),
                    this@HomeFragment
                )
            adapter = recyclerAdapter
        }
    }

    private fun calculateLoad() {
        if (_homeList.isNotEmpty())
            if (viewModel.getHomeList(
                    _homeList[loadingProgress].mediaListType,
                    _homeList[loadingProgress].mediaType
                ).isNullOrEmpty()
            )
                loadMedia(
                    _homeList[loadingProgress].mediaListType,
                    _homeList[loadingProgress].mediaType
                )
    }

    override fun onResume() {
        super.onResume()
        loadingProgress = 0
        calculateLoad()
    }

    private fun subscribeObservers() {


        viewModel.viewState.observe(viewLifecycleOwner, { viewState ->

            if (viewState != null) {
                viewState.popularMovies?.let { list ->
                    _homeList.single { it.mediaListType == MediaListType.POPULAR && it.mediaType == MediaType.MOVIE }.list =
                        list
                    recyclerAdapter.submitList(_homeList)
                }
                viewState.topRatedMovies?.let { list ->
                    _homeList.single { it.mediaListType == MediaListType.TOP_RATED && it.mediaType == MediaType.MOVIE }.list =
                        list
                    recyclerAdapter.submitList(_homeList)
                }
                viewState.popularTvShows?.let { list ->
                    _homeList.single { it.mediaListType == MediaListType.POPULAR && it.mediaType == MediaType.TV_SHOW }.list =
                        list
                    recyclerAdapter.submitList(_homeList)
                }
                viewState.topRatedTvShows?.let { list ->
                    _homeList.single { it.mediaListType == MediaListType.TOP_RATED && it.mediaType == MediaType.TV_SHOW }.list =
                        list
                    recyclerAdapter.submitList(_homeList)
                }
                viewState.upcomingMovies?.let { list ->
                    _homeList.single { it.mediaListType == MediaListType.UPCOMING_OR_ON_THE_AIR && it.mediaType == MediaType.MOVIE }.list =
                        list
                    recyclerAdapter.submitList(_homeList)
                }
                viewState.onTheAirTvShows?.let { list ->
                    _homeList.single { it.mediaListType == MediaListType.UPCOMING_OR_ON_THE_AIR && it.mediaType == MediaType.TV_SHOW }.list =
                        list
                    recyclerAdapter.submitList(_homeList)
                }
                viewState.trendingMovies?.let { list ->
                    _homeList.single { it.mediaListType == MediaListType.TRENDING && it.mediaType == MediaType.MOVIE }.list =
                        list
                    recyclerAdapter.submitList(_homeList)
                }
                viewState.trendingTvShows?.let { list ->
                    _homeList.single { it.mediaListType == MediaListType.TRENDING && it.mediaType == MediaType.TV_SHOW }.list =
                        list
                    recyclerAdapter.submitList(_homeList)
                }
            }
        })

        viewModel.shouldDisplayProgressBar.observe(viewLifecycleOwner, {
            uiController.displayProgressBar(it)
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, { stateMessage ->
            stateMessage?.let { message ->
                if (message.response.messageType == MessageType.Success && message.response.uiComponentType == UIComponentType.None) {
                    viewModel.clearStateMessage()
                    recyclerAdapter.notifyItemChanged(loadingProgress)
                    loadingProgress++
                    if (loadingProgress < _homeList.size) {
                        calculateLoad()
                    }
                } else {
                    uiController.onResponseReceived(
                        response = message.response,
                        stateMessageCallback = object : StateMessageCallback {
                            override fun removeMessageFromStack() {
                                viewModel.clearStateMessage()
                            }
                        }
                    )
                }
            }
        })
    }

    private fun loadMedia(mediaListType: MediaListType, mediaType: MediaType) {

        when (mediaType) {
            MediaType.MOVIE -> when (mediaListType) {
                MediaListType.POPULAR -> viewModel.setStateEvent(HomeStateEvent.PopularMovies)
                MediaListType.TOP_RATED -> viewModel.setStateEvent(HomeStateEvent.TopRatedMovies)
                MediaListType.TRENDING -> viewModel.setStateEvent(HomeStateEvent.TrendingMovies)
                MediaListType.UPCOMING_OR_ON_THE_AIR -> viewModel.setStateEvent(HomeStateEvent.UpcomingMovies)
            }
            MediaType.TV_SHOW -> when (mediaListType) {
                MediaListType.POPULAR -> viewModel.setStateEvent(HomeStateEvent.PopularTvShows)
                MediaListType.TOP_RATED -> viewModel.setStateEvent(HomeStateEvent.TopRatedTvShows)
                MediaListType.TRENDING -> viewModel.setStateEvent(HomeStateEvent.TrendingTvShows)
                MediaListType.UPCOMING_OR_ON_THE_AIR -> viewModel.setStateEvent(HomeStateEvent.OnTheAirTvShows)
            }
            else -> viewModel.setStateEvent(HomeStateEvent.PopularMovies)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.let {
            if (it is MainActivity) {
                try {
                    uiController = context as UIController
                } catch (e: ClassCastException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onItemSelected(position: Int, item: HomeModel) {

        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToSearchFragment(
                tabId = item.mediaListType.code,
                mediaType = item.mediaType,
                viewType = ViewType.NONE
            )
        )
    }

    override fun onMediaSelected(position: Int, item: Media) {

        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToViewMediaFragment(
                item.mediaType, item.id
            )
        )
    }
}