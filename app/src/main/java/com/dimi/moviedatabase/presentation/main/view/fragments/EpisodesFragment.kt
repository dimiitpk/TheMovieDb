package com.dimi.moviedatabase.presentation.main.view.fragments

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dimi.moviedatabase.R
import com.dimi.moviedatabase.business.domain.model.Episode
import com.dimi.moviedatabase.databinding.LayoutRecyclerViewBinding
import com.dimi.moviedatabase.presentation.common.invisible
import com.dimi.moviedatabase.presentation.common.visible
import com.dimi.moviedatabase.presentation.main.view.adapter.EpisodeAdapter
import com.dimi.moviedatabase.presentation.main.view.state.SeasonContainerState
import com.dimi.moviedatabase.presentation.main.view.viewmodel.setSeasonContainerState
import com.dimi.moviedatabase.presentation.main.view.viewmodel.setSelectedEpisode
import com.dimi.moviedatabase.util.SpacesItemDecoration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
class EpisodesFragment :
    BaseViewMediaFragment<LayoutRecyclerViewBinding>(R.layout.layout_recycler_view),
    EpisodeAdapter.Interaction {

    private lateinit var recyclerAdapter: EpisodeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let { ContextCompat.getColor(it, R.color.colorBackground) }?.let {
            binding.layoutRvContainer.setBackgroundColor(
                it
            )
        }
        setupRecyclerView()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, { viewState ->
            if (viewState != null) {

                viewState.seasonEpisodes?.let { list ->
                    if (list.isNotEmpty()) {
                        recyclerAdapter.apply {
                            submitList(
                                viewState.seasonEpisodes
                            )
                        }
                    }
                    binding.emptyListText.invisible()
                    binding.infoText.text = resources.getQuantityString(R.plurals.episodes_amount_format, viewState.selectedSeason?.episodeCount ?: 2, viewState.selectedSeason?.episodeCount)
                    binding.recyclerViewInfoContainer.visible()
                } ?: run {
                    binding.emptyListText.visible()
                }
            }
        })
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {

            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

            val spaceDecoration = SpacesItemDecoration(15, true)
            removeItemDecoration(spaceDecoration)
            addItemDecoration(spaceDecoration)
            recyclerAdapter =
                EpisodeAdapter(
                    this@EpisodesFragment
                )
            adapter = recyclerAdapter
        }
    }

    override fun onItemSelected(position: Int, item: Episode) {

        viewModel.setSelectedEpisode(episode = item)
        viewModel.setSeasonContainerState(SeasonContainerState.EPISODES_DETAIL_VIEW)
    }

    override fun onDestroyView() {
        binding.recyclerView.adapter = null
        super.onDestroyView()
    }
}