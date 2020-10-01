package com.dimi.moviedatabase.presentation.main.view.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dimi.moviedatabase.R
import com.dimi.moviedatabase.business.domain.model.Media
import com.dimi.moviedatabase.business.domain.state.MediaType
import com.dimi.moviedatabase.databinding.LayoutRecyclerViewBinding
import com.dimi.moviedatabase.presentation.common.gone
import com.dimi.moviedatabase.presentation.common.invisible
import com.dimi.moviedatabase.presentation.common.visible
import com.dimi.moviedatabase.presentation.common.adapters.MediaListAdapter
import com.dimi.moviedatabase.presentation.main.view.ViewMediaFragmentDirections
import com.dimi.moviedatabase.util.SpacesItemDecoration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
class SimilarMediaFragment :
    BaseViewMediaFragment<LayoutRecyclerViewBinding>(R.layout.layout_recycler_view),
    MediaListAdapter.Interaction {

    private lateinit var recyclerAdapter: MediaListAdapter<Media>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, { viewState ->
            if (viewState != null) {

                viewState.similarMedia?.let { list ->
                    recyclerAdapter.apply {
                        preloadGlideImages(
                            list = list
                        )
                        submitList(
                            viewState.similarMedia, false
                        )
                    }
                    binding.emptyListText.invisible()
                    if (viewState.mediaType == MediaType.PERSON)
                        binding.infoText.text = resources.getString(R.string.tv_shows_amount_format, list.size)
                    else
                        binding.infoText.invisible()
                    binding.viewLayoutChange.visible()
                    binding.recyclerViewInfoContainer.visible()
                } ?: run {
                    binding.emptyListText.visible()
                    binding.recyclerViewInfoContainer.gone()
                }
            }
        })
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {

            layoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL)

            val spaceDecoration = SpacesItemDecoration(15)
            removeItemDecoration(spaceDecoration)
            addItemDecoration(spaceDecoration)
            recyclerAdapter =
                MediaListAdapter(
                    layout = layoutManager as StaggeredGridLayoutManager,
                    requestManager = requestManager,
                    interaction = this@SimilarMediaFragment
                )
            adapter = recyclerAdapter
        }

        binding.viewLayoutChange.setOnClickListener {
            recyclerAdapter.changeViewLayout()?.let { layout ->
                if( layout.spanCount == 1 ) {
                    binding.viewLayoutChange.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view_module, 0)
                } else {
                    binding.viewLayoutChange.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view_list, 0)
                }
            }
        }
    }

    override fun onDestroyView() {
        binding.recyclerView.adapter = null
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadSimilarMedia()
    }

    override fun onItemSelected(position: Int, item: Media) {
        findNavController().navigate(
            ViewMediaFragmentDirections.actionViewMediaFragmentSelf(item.mediaType, item.id)
        )
    }
}