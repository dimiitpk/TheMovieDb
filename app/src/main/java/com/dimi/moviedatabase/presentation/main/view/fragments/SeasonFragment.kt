package com.dimi.moviedatabase.presentation.main.view.fragments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dimi.moviedatabase.R
import com.dimi.moviedatabase.business.domain.model.Season
import com.dimi.moviedatabase.business.domain.model.TvShow
import com.dimi.moviedatabase.presentation.common.invisible
import com.dimi.moviedatabase.presentation.common.visible
import com.dimi.moviedatabase.presentation.main.view.adapter.SeasonAdapter
import com.dimi.moviedatabase.presentation.main.view.state.ViewMediaStateEvent
import com.dimi.moviedatabase.presentation.main.view.viewmodel.getMedia
import com.dimi.moviedatabase.presentation.main.view.viewmodel.setSelectedSeason
import com.dimi.moviedatabase.util.SpacesItemDecoration
import kotlinx.android.synthetic.main.layout_recycler_view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
class SeasonFragment :
    BaseViewMediaFragment(R.layout.layout_recycler_view),
    SeasonAdapter.Interaction{

    private lateinit var recyclerAdapter: SeasonAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, { viewState ->
            if (viewState != null) {

                viewState.media?.let { media ->
                    if (media is TvShow)
                        media.seasons?.let { list ->
                            if (list.isNotEmpty()) {
                                recyclerAdapter.apply {
                                    preloadGlideImages(
                                        requestManager = requestManager,
                                        list = list
                                    )
                                    submitList(
                                        (viewState.media as TvShow).seasons
                                    )
                                }
                            }
                            empty_list_text.invisible()
                        } ?: run {
                            empty_list_text.visible()
                        }
                }
            }
        })
    }

    private fun setupRecyclerView() {
        recycler_view.apply {

            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

            val spaceDecoration = SpacesItemDecoration(15)
            removeItemDecoration(spaceDecoration)
            addItemDecoration(spaceDecoration)
            recyclerAdapter =
                SeasonAdapter(
                    requestManager,
                    this@SeasonFragment
                )
            adapter = recyclerAdapter
        }
    }

    override fun onItemSelected(position: Int, item: Season) {
        viewModel.setSelectedSeason(season = item)
        viewModel.getMedia()?.let { media ->
            viewModel.setStateEvent(
                ViewMediaStateEvent.GetEpisodesPerSeason(
                    mediaId = media.id,
                    season = item
                )
            )
        }
    }
}