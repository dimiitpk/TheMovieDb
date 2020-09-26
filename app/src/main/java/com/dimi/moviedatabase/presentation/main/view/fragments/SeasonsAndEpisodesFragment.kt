package com.dimi.moviedatabase.presentation.main.view.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.dimi.moviedatabase.R
import com.dimi.moviedatabase.business.domain.model.Season
import com.dimi.moviedatabase.business.domain.model.TvShow
import com.dimi.moviedatabase.framework.network.NetworkConstants.BIG_IMAGE_URL_PREFIX
import com.dimi.moviedatabase.presentation.common.gone
import com.dimi.moviedatabase.presentation.common.visible
import com.dimi.moviedatabase.presentation.main.view.state.SeasonContainerState
import com.dimi.moviedatabase.presentation.main.view.viewmodel.getSeasonContainerState
import com.dimi.moviedatabase.presentation.main.view.viewmodel.setSeasonContainerState
import com.dimi.moviedatabase.presentation.main.view.viewmodel.setSelectedEpisode
import com.dimi.moviedatabase.presentation.main.view.viewmodel.setSelectedSeason
import com.dimi.moviedatabase.util.toSimpleString
import kotlinx.android.synthetic.main.fragment_seasons_and_episodes.*
import kotlinx.android.synthetic.main.layout_season_detail_fragment.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
class SeasonsAndEpisodesFragment :
    BaseViewMediaFragment(R.layout.fragment_seasons_and_episodes) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()
        arrow_back.setOnClickListener {
            if (arrow_back.isVisible)
                when (viewModel.getSeasonContainerState()) {
                    SeasonContainerState.EPISODES_VIEW -> {
                        viewModel.setSelectedSeason(null)
                        viewModel.setSeasonContainerState(SeasonContainerState.SEASONS_VIEW)
                        arrow_back.gone()
                    }
                    SeasonContainerState.EPISODES_DETAIL_VIEW -> {
                        viewModel.setSelectedEpisode(null)
                        viewModel.setSeasonContainerState(SeasonContainerState.EPISODES_VIEW)
                        arrow_back.visible()
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
                        info_included_fragment.gone()
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.container_fragment, SeasonFragment()).commit()
                        arrow_back.gone()
                    }
                    SeasonContainerState.EPISODES_VIEW -> {
                        info_included_fragment.visible()
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.container_fragment, EpisodesFragment()).commit()
                        arrow_back.visible()
                    }
                    SeasonContainerState.EPISODES_DETAIL_VIEW -> {
                        info_included_fragment.gone()
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.container_fragment, EpisodeDetailsFragment()).commit()
                        arrow_back.visible()
                    }
                }

                viewState.media?.let { media ->
                    if (media is TvShow) {
                        when (viewState.seasonContainerState) {
                            SeasonContainerState.EPISODES_VIEW -> {
                                viewState.selectedSeason?.let { season ->
                                    setSeasonDetails(season)
                                }
                            }
                            SeasonContainerState.EPISODES_DETAIL_VIEW -> {
                                info_included_fragment.gone()
                            }
                            else -> {
                                info_included_fragment.gone()
                            }
                        }
                    }
                }
            }
        })
    }

    private fun setSeasonDetails(season: Season) {

        fragment_seasons_container.smoothScrollTo(0, 0)
        info_included_fragment.overview.text = season.overview
        info_included_fragment.air_date.text = season.airDate.toSimpleString()
        info_included_fragment.name.text = season.seasonName

        requestManager
            .load(BIG_IMAGE_URL_PREFIX + season.posterPath)
            .into(info_included_fragment.image)

        info_included_fragment.visible()
        focusable_view.requestFocus()
    }
}