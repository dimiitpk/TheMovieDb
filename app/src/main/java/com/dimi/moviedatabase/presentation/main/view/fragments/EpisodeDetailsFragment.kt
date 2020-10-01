package com.dimi.moviedatabase.presentation.main.view.fragments

import android.os.Bundle
import android.view.View
import com.dimi.moviedatabase.R
import com.dimi.moviedatabase.databinding.LayoutEpisodeDetailsBinding
import com.dimi.moviedatabase.presentation.main.view.viewmodel.getSelectedEpisode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
class EpisodeDetailsFragment : BaseViewMediaFragment<LayoutEpisodeDetailsBinding>(R.layout.layout_episode_details) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSelectedEpisode()?.let { episode ->
            binding.episode = episode
        }
    }
}