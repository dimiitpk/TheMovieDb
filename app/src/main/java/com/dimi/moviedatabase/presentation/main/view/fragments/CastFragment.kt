package com.dimi.moviedatabase.presentation.main.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.request.RequestOptions
import com.dimi.moviedatabase.R
import com.dimi.moviedatabase.business.domain.model.Media
import com.dimi.moviedatabase.business.domain.model.Person
import com.dimi.moviedatabase.business.domain.state.MediaType
import com.dimi.moviedatabase.presentation.GlideApp
import com.dimi.moviedatabase.presentation.common.adapters.MediaListAdapter
import com.dimi.moviedatabase.presentation.common.invisible
import com.dimi.moviedatabase.presentation.common.visible
import com.dimi.moviedatabase.presentation.main.view.ViewMediaFragmentArgs
import com.dimi.moviedatabase.presentation.main.view.ViewMediaFragmentDirections
import com.dimi.moviedatabase.util.SpacesItemDecoration
import kotlinx.android.synthetic.main.layout_recycler_view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
class CastFragment :
    BaseViewMediaFragment(R.layout.layout_recycler_view, true),
    MediaListAdapter.Interaction {

    private lateinit var recyclerAdapter: MediaListAdapter<Person>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requestManager = GlideApp.with(this).setDefaultRequestOptions(
            RequestOptions
                .circleCropTransform()
                .error(R.drawable.ic_broken_image)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, { viewState ->
            if (viewState != null) {

                viewState.media?.let { media ->
                    media.castList?.let { list ->
                        if (list.isNotEmpty()) {

                            recyclerAdapter.apply {
                                preloadGlideImages(
                                    list = list
                                )
                                submitList(
                                    viewState.media!!.castList, false
                                )
                            }
                        }
                        empty_list_text.invisible()
                        info_text.text = resources.getString(R.string.persons_amount_format, list.size)
                        recycler_view_info_container.visible()
                    } ?: run {
                        empty_list_text.visible()
                    }
                }
            }
        })
    }

    private fun setupRecyclerView() {
        recycler_view.apply {

            layoutManager = StaggeredGridLayoutManager(1, RecyclerView.VERTICAL)

            val spaceDecoration = SpacesItemDecoration(15)
            removeItemDecoration(spaceDecoration)
            addItemDecoration(spaceDecoration)
            recyclerAdapter =
                MediaListAdapter(
                    layout = layoutManager as StaggeredGridLayoutManager,
                    requestManager = requestManager,
                    interaction = this@CastFragment
                )
            adapter = recyclerAdapter
        }
    }

    override fun onItemSelected(position: Int, item: Media) {
        findNavController().navigate(
            ViewMediaFragmentDirections.actionViewMediaFragmentSelf(MediaType.PERSON, item.id)
        )
    }
}