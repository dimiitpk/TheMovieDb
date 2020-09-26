package com.dimi.moviedatabase.presentation.main.search

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dimi.moviedatabase.R
import com.dimi.moviedatabase.business.data.network.NetworkErrors.isPaginationDone
import com.dimi.moviedatabase.business.domain.model.Media
import com.dimi.moviedatabase.business.domain.state.MediaType
import com.dimi.moviedatabase.business.domain.state.StateMessageCallback
import com.dimi.moviedatabase.presentation.GlideApp
import com.dimi.moviedatabase.presentation.common.*
import com.dimi.moviedatabase.presentation.common.adapters.MediaListAdapter
import com.dimi.moviedatabase.presentation.main.search.enums.MediaListType
import com.dimi.moviedatabase.presentation.main.search.enums.ViewType
import com.dimi.moviedatabase.presentation.main.search.viewmodel.*
import com.dimi.moviedatabase.presentation.main.view.ViewMediaFragmentArgs
import com.dimi.moviedatabase.util.Constants.LAYOUT_LIST_SPAN_COUNT
import com.dimi.moviedatabase.util.Genre
import com.dimi.moviedatabase.util.SpacesItemDecoration
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.drawer_header_sort_by.*
import kotlinx.android.synthetic.main.drawer_menu_item_layout.view.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.layout_search_toolbar.*
import kotlinx.android.synthetic.main.layout_search_toolbar.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.apply
import kotlin.getValue
import kotlin.let


@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SearchFragment : BaseDialogFragment(R.layout.fragment_search),
    MediaListAdapter.Interaction,
    MediaListAdapter.Restoration,
    TabLayout.OnTabSelectedListener,
    SwipeRefreshLayout.OnRefreshListener {

    val viewModel: SearchViewModel by viewModels()

    private lateinit var genre: Genre
    private lateinit var searchView: SearchView
    private lateinit var recyclerAdapter: MediaListAdapter<Media>

    private lateinit var searchQueryJob: Job
    val searchQueryFlow = MutableStateFlow("")

    private val args: SearchFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipe_refresh.setOnRefreshListener(this)

        handleNavArgs()

        setupActionBar()

        setupFlowCollectors()
        subscribeObservers()
    }

    private fun handleNavArgs() {
        args.network?.let { network ->
            viewModel.setNetwork(network)
        }

        viewModel.setViewType(args.viewType)

        viewModel.setMediaType(args.mediaType)

        genre = Genre(viewModel.getMediaType())

        setupTabs(args.tabId)
    }

    private fun setupFlowCollectors() {
        searchQueryJob = Job()
        CoroutineScope(Dispatchers.Main + searchQueryJob).launch {
            searchQueryFlow
                .debounce(300)
                .filter {
                    return@filter (viewModel.isValidQuery(it) && (it != viewModel.getSearchQuery()))
                }
                .distinctUntilChanged()
                .flowOn(Dispatchers.Default)
                .collect {
                    handleSearchConfirmed(it)
                }
        }
    }

    private fun isRecyclerAdapterInitializer() = ::recyclerAdapter.isInitialized

    private fun subscribeObservers() {

        viewModel.userPreferencesFlowLiveData.observe(viewLifecycleOwner, { value ->

            if (value.sortFilter == SortFilter.BY_VOTE_COUNT && viewModel.getMediaType() == MediaType.TV_SHOW)
                viewModel.setSortFilter(SortFilter.BY_POPULARITY)
            else
                viewModel.setSortFilter(value.sortFilter)

            viewModel.setSortOrder(value.sortOrder)
            viewModel.setLayoutSpanCount(value.layoutSpanCount)

            if (!isRecyclerAdapterInitializer()) {
                initRecyclerView()
            }

            viewModel.loadFirstPage()
        })

        viewModel.viewState.observe(viewLifecycleOwner, { viewState ->

            if (isRecyclerAdapterInitializer()) {
                recyclerAdapter.apply {
                    viewState.mediaList?.let {
                        preloadGlideImages(
                            list = it
                        )
                    }
                    submitList(
                        list = viewState.mediaList,
                        isQueryExhausted = viewState.isQueryExhausted ?: true
                    )
                }
            }

            setupSortFilter(viewState.sortFilter)
            setupSortOrder(viewState.sortOrder)
            setupLayoutMode(viewState.layoutSpanCount)
        })

        viewModel.shouldDisplayProgressBar.observe(viewLifecycleOwner, {
            uiController.displayProgressBar(it)
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, { stateMessage ->
            stateMessage?.let { message ->
                if (isPaginationDone(message.response.message)) {
                    viewModel.setQueryExhausted(true)
                    viewModel.clearStateMessage()
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

    private fun setupLayoutMode(layoutSpanCount: Int) {
        if (layoutSpanCount == LAYOUT_LIST_SPAN_COUNT) {
            layout_change.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_view_module
                )
            )
        } else {
            layout_change.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_view_list
                )
            )
        }
    }

    override fun onPause() {
        super.onPause()
        saveListPosition()
    }

    override fun saveListPosition() {
        recycler_view.layoutManager?.onSaveInstanceState()?.let { lmState ->
            viewModel.setLayoutManagerState(lmState)
        }
    }

    override fun restoreListPosition() {
        viewModel.getLayoutManagerState()?.let { lmState ->
            recycler_view?.layoutManager?.onRestoreInstanceState(lmState)
        }
    }


    private fun initRecyclerView() {

        recycler_view.apply {

            layoutManager = StaggeredGridLayoutManager(
                viewModel.getLayoutSpanCount(),
                LinearLayoutManager.VERTICAL
            )
            // setting gapStrategy so the spans don't mix up
            (layoutManager as StaggeredGridLayoutManager).gapStrategy =
                StaggeredGridLayoutManager.GAP_HANDLING_NONE

            val spaceDecoration = SpacesItemDecoration(15)
            removeItemDecoration(spaceDecoration) // does nothing if not applied already
            addItemDecoration(spaceDecoration)
            recyclerAdapter =
                MediaListAdapter(
                    layout = layoutManager as StaggeredGridLayoutManager,
                    requestManager = GlideApp.with(this@SearchFragment),
                    interaction = this@SearchFragment,
                    restoration = this@SearchFragment
                )
            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!canScrollVertically(1)) {
                        viewModel.nextPage()
                    }
                }
            })
            adapter = recyclerAdapter
        }
    }

    private fun handleSearchConfirmed(searchQuery: String) {

        viewModel.setQuery(searchQuery).let {
            onMovieSearch(false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchQueryJob.cancel()
        recycler_view.adapter = null
    }

    private fun setupTabs(forceSelect: Int) {
        when (viewModel.getViewType()) {
            ViewType.NONE -> {
                for (item in MediaListType.values()) {
                    tabs.addTab(
                        tabs.newTab().apply {
                            text = item.name
                        },
                        false
                    )
                    viewModel.setSelectedTab(forceSelect)
                    viewModel.setMediaListType(MediaListType.parseFromCode(forceSelect))
                }
            }
            ViewType.NETWORK -> {
                tabs.gone()
            }
            ViewType.GENRE -> {
                for ((index, g) in genre.getAllGenreIds().withIndex()) {
                    tabs.addTab(
                        tabs.newTab().apply {
                            text = genre.getGenreName(g)
                        },
                        false
                    )
                    if (forceSelect != -1 && g == forceSelect) {
                        viewModel.setGenre(forceSelect)
                        viewModel.setSelectedTab(index)
                    }
                }
            }
            ViewType.SEARCH -> {
                for (item in MediaType.values()) {
                    tabs.addTab(
                        tabs.newTab().apply {
                            text = item.pluralName
                        },
                        false
                    )
                }
                viewModel.setSelectedTab(0)
            }
        }

        if (tabs.isVisible) {
            // add listener after first tab is selected and this way we will not trigger movieSearch 2 or more times
            // and back from viewModel will we properly be restored to right list
            // handler used bcs selected view is not scrolled
            Handler().postDelayed({
                tabs.getTabAt(viewModel.getSelectedTab())?.select()
                tabs.addOnTabSelectedListener(this)
            }, 1)
        }
    }

    override fun onRefresh() {
        onMovieSearch()
        swipe_refresh.isRefreshing = false
    }

    private fun onMovieSearch(leaveSearchView: Boolean = true) {
        viewModel.loadFirstPage()
        resetUI(leaveSearchView)
    }

    private fun resetUI(leaveSearchView: Boolean = true) {
        recycler_view.smoothScrollToPosition(0)
        if (leaveSearchView) {
            uiController.hideSoftKeyboard()
            focusable_view.requestFocus()
        }
    }

    override fun onItemSelected(position: Int, item: Media) {
        uiController.hideSoftKeyboard()
        findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToViewMediaFragment(
                viewModel.getMediaType(), item.id
            )
        )
    }

    override fun onTabReselected(tab: TabLayout.Tab) {
        resetUI()
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
        when (viewModel.getViewType()) {
            ViewType.SEARCH -> {
            }
            ViewType.NONE -> {
            }
            ViewType.GENRE -> {
            }
            ViewType.NETWORK -> {
            }
        }
    }

    override fun onTabSelected(tab: TabLayout.Tab) {

        viewModel.setSelectedTab(tab.position)
        when (viewModel.getViewType()) {
            ViewType.SEARCH -> {
                viewModel.setMediaType(MediaType.parseFromCode(tab.position))
            }
            ViewType.NONE -> {
                viewModel.setMediaListType(MediaListType.parseFromCode(tab.position))
            }
            ViewType.GENRE -> {
                viewModel.setGenre(genre.getGenreIdByName(tab.text as String?))
            }
            ViewType.NETWORK -> {
                // no tabs
            }
        }
        onMovieSearch()
    }

    private fun setupActionBar() {

        view?.let { v ->

            val view = View.inflate(
                v.context,
                R.layout.layout_search_toolbar,
                null
            )
            view.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            toolbar_content_container.addView(view)
            when (viewModel.getViewType()) {
                ViewType.SEARCH -> {
                    initSearchView()
                    toolbar_content_container.filter.gone()
                }
                ViewType.GENRE -> {
                    toolbar_content_container.toolbar_name.apply {
                        text = viewModel.getMediaType().pluralName
                        visible()
                    }
                    setupFilterDrawer()
                    toolbar_content_container.filter.setOnClickListener {
                        if (drawer_layout.isDrawerOpen(GravityCompat.END))
                            drawer_layout.closeDrawer(
                                GravityCompat.END
                            )
                        else
                            drawer_layout.openDrawer(GravityCompat.END)
                    }
                }
                ViewType.NONE -> {
                    toolbar_content_container.toolbar_name.apply {
                        text = viewModel.getMediaType().pluralName
                        visible()
                    }
                    toolbar_content_container.filter.gone()
                }
                ViewType.NETWORK -> {
                    toolbar_content_container.toolbar_name.apply {
                        text = viewModel.getNetwork()?.name
                        visible()
                    }
                    setupFilterDrawer()
                    toolbar_content_container.filter.setOnClickListener {
                        if (drawer_layout.isDrawerOpen(GravityCompat.END))
                            drawer_layout.closeDrawer(
                                GravityCompat.END
                            )
                        else
                            drawer_layout.openDrawer(GravityCompat.END)
                    }
                }
            }

            toolbar_content_container.layout_change.setOnClickListener {
                recyclerAdapter.changeViewLayout()?.let { layout ->
                    viewModel.saveLayoutMode(layout.spanCount)
                }
            }

            toolbar_content_container.arrow_back.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun setupSortOrder(sortOrder: SortOrder) {
        val colorHoloLightRed = ContextCompat.getColor(
            requireContext(),
            android.R.color.holo_red_light
        )
        val colorSecondaryText = ContextCompat.getColor(
            requireContext(),
            R.color.colorSecondaryText
        )

        when (sortOrder) {
            SortOrder.ASCENDING -> {
                asc_button.setTextAndCompoundDrawablesColor(colorHoloLightRed)
                desc_button.setTextAndCompoundDrawablesColor(colorSecondaryText)
            }
            SortOrder.DESCENDING -> {
                desc_button.setTextAndCompoundDrawablesColor(colorHoloLightRed)
                asc_button.setTextAndCompoundDrawablesColor(colorSecondaryText)
            }
        }
    }

    private fun setupSortFilter(sortFilter: SortFilter) {
        for (index in 0..drawer_menu_container.childCount) {
            drawer_menu_container.getChildAt(index)?.let {
                it as LinearLayout
                if (it.item_name.text != sortFilter.upperCaseName)
                    it.check.invisible()
                else it.check.visible()
            }
        }
    }


    private fun setupFilterDrawer() {

        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)


        drawer_done.setOnClickListener {
            if (drawer_layout.isDrawerOpen(GravityCompat.END))
                drawer_layout.closeDrawer(GravityCompat.END)
        }

        asc_button.setOnClickListener {
            viewModel.enableSortByASC()
            resetUI()
        }

        desc_button.setOnClickListener {
            viewModel.enableSortByDESC()
            resetUI()
        }

        if (drawer_menu_container.childCount < SortFilter.values().size)
            for (item in SortFilter.values()) {

                if (item == SortFilter.BY_VOTE_COUNT && viewModel.getMediaType() != MediaType.MOVIE)
                    continue

                val newView = layoutInflater.inflate(
                    R.layout.drawer_menu_item_layout,
                    drawer_menu_container,
                    false
                ).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(0, 0, 0, 12)
                    }
                    item_name.text = item.upperCaseName
                    item_name.setOnClickListener {
                        when (item) {
                            SortFilter.BY_POPULARITY -> {
                                viewModel.enableSortByPopularity()
                            }
                            SortFilter.BY_FIRST_AIR_DATE -> {
                                viewModel.enableSortByFirstAirDate()
                            }
                            SortFilter.BY_VOTE_COUNT -> {
                                viewModel.enableSortByVoteAverage()
                            }
                        }
                        resetUI()
                    }
                }
                drawer_menu_container.addView(newView)
            }
    }

    private fun initSearchView() {

        val searchViewToolbar: Toolbar? = toolbar_content_container.search_toolbar
        searchViewToolbar?.let { toolbar ->
            searchView = toolbar.search_view
            searchView.apply {

                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return true
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        searchQueryFlow.value = newText
                        return true
                    }
                })
                visible()
            }
        }
    }
}