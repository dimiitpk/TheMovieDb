package com.dimi.moviedatabase.presentation.main.view.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dimi.moviedatabase.R
import com.dimi.moviedatabase.business.domain.model.Media
import com.dimi.moviedatabase.business.domain.state.MediaType
import com.dimi.moviedatabase.presentation.common.gone
import com.dimi.moviedatabase.presentation.common.invisible
import com.dimi.moviedatabase.presentation.common.visible
import com.dimi.moviedatabase.presentation.common.adapters.MediaListAdapter
import com.dimi.moviedatabase.presentation.main.view.ViewMediaFragmentArgs
import com.dimi.moviedatabase.presentation.main.view.ViewMediaFragmentDirections
import com.dimi.moviedatabase.util.SpacesItemDecoration
import kotlinx.android.synthetic.main.layout_recycler_view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
class RecommendedMediaFragment :
    BaseViewMediaFragment(R.layout.layout_recycler_view),
    MediaListAdapter.Interaction {

    private lateinit var recyclerAdapter: MediaListAdapter<Media>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        subscribeObservers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadMediaRecommendations()
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, { viewState ->
            if (viewState != null) {

                viewState.recommendedMedia?.let { list ->
                    recyclerAdapter.apply {
                        preloadGlideImages(
                            list = list
                        )
                        submitList(
                            viewState.recommendedMedia, false
                        )
                    }
                    empty_list_text.invisible()
                    if (viewState.mediaType == MediaType.PERSON)
                        info_text.text = resources.getString(R.string.movies_amount_format, list.size)
                    else
                        info_text.invisible()
                    view_layout_change.visible()
                    recycler_view_info_container.visible()
                } ?: run {
                    empty_list_text.visible()
                    recycler_view_info_container.gone()
                }
            }
        })
    }

    private fun setupRecyclerView() {
        recycler_view.apply {

            layoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL)

            val spaceDecoration = SpacesItemDecoration(15)
            removeItemDecoration(spaceDecoration)
            addItemDecoration(spaceDecoration)
            recyclerAdapter =
                MediaListAdapter(
                    layout = layoutManager as StaggeredGridLayoutManager,
                    requestManager = requestManager,
                    interaction = this@RecommendedMediaFragment
                )
            adapter = recyclerAdapter
        }

        view_layout_change.setOnClickListener {
            recyclerAdapter.changeViewLayout()?.let { layout ->
                if (layout.spanCount == 1) {
                    view_layout_change.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_view_module,
                        0
                    )
                } else {
                    view_layout_change.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_view_list,
                        0
                    )
                }
            }
        }
    }


    override fun onItemSelected(position: Int, item: Media) {
        findNavController().navigate(
            ViewMediaFragmentDirections.actionViewMediaFragmentSelf(item.mediaType, item.id)
        )
    }
}