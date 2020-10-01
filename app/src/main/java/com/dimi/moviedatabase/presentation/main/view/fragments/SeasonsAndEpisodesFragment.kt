package com.dimi.moviedatabase.presentation.main.view.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.dimi.moviedatabase.R
import com.dimi.moviedatabase.business.domain.model.TvShow
import com.dimi.moviedatabase.databinding.FragmentSeasonsAndEpisodesBinding
import com.dimi.moviedatabase.presentation.common.gone
import com.dimi.moviedatabase.presentation.common.visible
import com.dimi.moviedatabase.presentation.main.view.state.SeasonContainerState
import com.dimi.moviedatabase.presentation.main.view.viewmodel.getSeasonContainerState
import com.dimi.moviedatabase.presentation.main.view.viewmodel.setSeasonContainerState
import com.dimi.moviedatabase.presentation.main.view.viewmodel.setSelectedEpisode
import com.dimi.moviedatabase.presentation.main.view.viewmodel.setSelectedSeason
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
class SeasonsAndEpisodesFragment :
    BaseViewMediaFragment<FragmentSeasonsAndEpisodesBinding>(R.layout.fragment_seasons_and_episodes) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()
        binding.arrowBack.setOnClickListener {
            if (binding.arrowBack.isVisible)
                when (viewModel.getSeasonContainerState()) {
                    SeasonContainerState.EPISODES_VIEW -> {
                        viewModel.setSelectedSeason(null)
                        viewModel.setSeasonContainerState(SeasonContainerState.SEASONS_VIEW)
                        binding.arrowBack.gone()
                    }
                    SeasonContainerState.EPISODES_DETAIL_VIEW -> {
                        viewModel.setSelectedEpisode(null)
                        viewModel.setSeasonContainerState(SeasonContainerState.EPISODES_VIEW)
                        binding.arrowBack.visible()
                    }
                    else -> {

                    }
                }
        }
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, { viewState ->
            if (viewState != null) {
                when (viewState.seasonContainerState) {
                    SeasonContainerState.SEASONS_VIEW -> {
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.container_fragment, SeasonFragment()).commit()
                        binding.arrowBack.gone()
                    }
                    SeasonContainerState.EPISODES_VIEW -> {
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.container_fragment, EpisodesFragment()).commit()
                        binding.arrowBack.visible()
                    }
                    SeasonContainerState.EPISODES_DETAIL_VIEW -> {
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.container_fragment, EpisodeDetailsFragment()).commit()
                        binding.arrowBack.visible()
                    }
                }

                viewState.media?.let { media ->
                    if (media is TvShow) {
                        when (viewState.seasonContainerState) {
                            SeasonContainerState.EPISODES_VIEW -> {
                                viewState.selectedSeason?.let { season ->
                                    binding.fragmentSeasonsContainer.smoothScrollTo(0, 0)
                                    binding.season = season
                                    binding.focusableView.requestFocus()
                                }
                            }
                            else -> {
                                binding.season = null
                            }
                        }
                    }
                }
            }
        })
    }
}