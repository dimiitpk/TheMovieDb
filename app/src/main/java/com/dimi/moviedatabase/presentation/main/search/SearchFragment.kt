package com.dimi.moviedatabase.presentation.main.search

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
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
import com.dimi.moviedatabase.databinding.DrawerMenuItemLayoutBinding
import com.dimi.moviedatabase.databinding.FragmentSearchBinding
import com.dimi.moviedatabase.presentation.GlideApp
import com.dimi.moviedatabase.presentation.common.*
import com.dimi.moviedatabase.presentation.common.adapters.MediaListAdapter
import com.dimi.moviedatabase.presentation.common.enums.SortFilter
import com.dimi.moviedatabase.presentation.main.search.enums.MediaListType
import com.dimi.moviedatabase.presentation.main.search.enums.ViewType
import com.dimi.moviedatabase.presentation.main.search.viewmodel.*
import com.dimi.moviedatabase.presentation.common.BaseDBDialogFragment
import com.dimi.moviedatabase.util.Genre
import com.dimi.moviedatabase.util.SpacesItemDecoration
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.apply
import kotlin.getValue
import kotlin.let


@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SearchFragment : BaseDBDialogFragment<FragmentSearchBinding>(R.layout.fragment_search),
    MediaListAdapter.Interaction,
    MediaListAdapter.Restoration,
    TabLayout.OnTabSelectedListener,
    OnDataBindingClickListener,
    SwipeRefreshLayout.OnRefreshListener {

    val viewModel: SearchViewModel by viewModels()

    private val drawerMenuSortItems = ArrayList<DrawerMenuItemLayoutBinding>()

    private lateinit var genre: Genre
    private lateinit var recyclerAdapter: MediaListAdapter<Media>

    private val args: SearchFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.listener = this
        binding.viewModel = this.viewModel
        binding.lifecycleOwner = this
        binding.swipeRefresh.setOnRefreshListener(this)

        handleNavArgs()

        setupActionBar()
        setupFilterDrawer()

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
                        if( viewState.page == 1 ) this@SearchFragment.resetUI(false)
                    }
                    submitList(
                        list = viewState.mediaList
                    )
                }
            }

            setupSortFilter(viewState.sortFilter)
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

    override fun onPause() {
        super.onPause()
        saveListPosition()
    }

    override fun saveListPosition() {
        binding.recyclerView.layoutManager?.onSaveInstanceState()?.let { lmState ->
            viewModel.setLayoutManagerState(lmState)
        }
    }

    override fun restoreListPosition() {
        viewModel.getLayoutManagerState()?.let { lmState ->
            _binding?.let {
                it.recyclerView.layoutManager?.onRestoreInstanceState(lmState)
            }
        }
    }

    private fun initRecyclerView() {

        binding.recyclerView.apply {

            layoutManager = StaggeredGridLayoutManager(
                viewModel.getLayoutSpanCount(),
                LinearLayoutManager.VERTICAL
            )
            (layoutManager as StaggeredGridLayoutManager).gapStrategy =
                StaggeredGridLayoutManager.GAP_HANDLING_NONE

            val spaceDecoration = SpacesItemDecoration(15)
            removeItemDecoration(spaceDecoration) // does nothing if not applied already
            addItemDecoration(spaceDecoration)
            recyclerAdapter =
                MediaListAdapter(
                    layout = layoutManager as StaggeredGridLayoutManager,
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

    override fun onDestroyView() {
        binding.recyclerView.adapter = null
        super.onDestroyView()
    }

    private fun setupTabs(forceSelect: Int) {
        when (viewModel.getViewType()) {
            ViewType.NONE -> {
                for (item in MediaListType.values()) {
                    binding.tabs.addTab(
                        binding.tabs.newTab().apply {
                            text =
                                if (item == MediaListType.UPCOMING_OR_ON_THE_AIR && viewModel.getMediaType() == MediaType.TV_SHOW) "On The Air"
                                else item.originalName
                        },
                        false
                    )
                    viewModel.setSelectedTab(forceSelect)
                    viewModel.setMediaListType(MediaListType.parseFromCode(forceSelect))
                }
            }
            ViewType.NETWORK -> {
                binding.tabs.gone()
            }
            ViewType.GENRE -> {
                for ((index, g) in genre.getAllGenreIds().withIndex()) {
                    binding.tabs.addTab(
                        binding.tabs.newTab().apply {
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
                    binding.tabs.addTab(
                        binding.tabs.newTab().apply {
                            text = item.pluralName
                        },
                        false
                    )
                }
                viewModel.setSelectedTab(0)
            }
        }

        if (binding.tabs.isVisible) {
            // add listener after first tab is selected and this way we will not trigger movieSearch 2 or more times
            // and back from viewModel will we properly be restored to right list
            // handler used bcs selected view is not scrolled
            Handler().postDelayed({
                binding.tabs.getTabAt(viewModel.getSelectedTab())?.select()
                binding.tabs.addOnTabSelectedListener(this)
            }, 1)
        }
    }

    override fun onRefresh() {
        onMovieSearch()
        binding.swipeRefresh.isRefreshing = false
    }

    private fun onMovieSearch() {
        viewModel.loadFirstPage()
        resetUI()
    }

    private fun resetUI(leaveSearchView: Boolean = true) {
        binding.recyclerView.smoothScrollToPosition(0)
        if (leaveSearchView) {
            uiController.hideSoftKeyboard()
            binding.focusableView.requestFocus()
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
        if (viewModel.getViewType() == ViewType.SEARCH )
            initSearchView()
    }

    private fun setupSortFilter(sortFilter: SortFilter) {

        for (item in drawerMenuSortItems) {
            item.checked = item.itemName.text == sortFilter.upperCaseName
        }
    }


    private fun setupFilterDrawer() {

        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)


        if (binding.drawerHeader.drawerMenuContainer.childCount < SortFilter.values().size)
            for (item in SortFilter.values()) {

                if (item == SortFilter.BY_VOTE_COUNT && viewModel.getMediaType() != MediaType.MOVIE)
                    continue

                val drawerItemBinding = DrawerMenuItemLayoutBinding.inflate(
                    layoutInflater,
                    binding.drawerHeader.drawerMenuContainer,
                    false
                )
                drawerMenuSortItems.add(drawerItemBinding)
                drawerItemBinding.root.apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(0, 0, 0, 12)
                    }
                }
                drawerItemBinding.itemName.text = item.upperCaseName
                drawerItemBinding.itemName.setOnClickListener {
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
                binding.drawerHeader.drawerMenuContainer.addView(drawerItemBinding.root)
            }
    }

    private fun initSearchView() {
        binding.toolbarContentContainer.searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    viewModel.setSearchQuery(newText)
                    return true
                }
            })
            visible()
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.drawerHeader.ascButton.id -> {
                viewModel.enableSortByASC()
                resetUI()
            }
            binding.drawerHeader.descButton.id -> {
                viewModel.enableSortByDESC()
                resetUI()
            }
            binding.drawerHeader.drawerDone.id -> {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.END))
                    binding.drawerLayout.closeDrawer(GravityCompat.END)
            }
            binding.toolbarContentContainer.layoutChange.id -> {
                recyclerAdapter.changeViewLayout()?.let { layout ->
                    viewModel.saveLayoutMode(layout.spanCount)
                }
            }
            binding.toolbarContentContainer.arrowBack.id -> {
                findNavController().popBackStack()
            }
            binding.toolbarContentContainer.filter.id -> {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.END))
                    binding.drawerLayout.closeDrawer(
                        GravityCompat.END
                    )
                else
                    binding.drawerLayout.openDrawer(GravityCompat.END)
            }
        }
    }
}