package com.dimi.moviedatabase.presentation.main.view.fragments

import android.os.Bundle
import android.view.View
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dimi.moviedatabase.R
import com.dimi.moviedatabase.databinding.LayoutEpisodeDetailsBinding
import com.dimi.moviedatabase.framework.network.NetworkConstants
import com.dimi.moviedatabase.presentation.main.view.viewmodel.getSelectedEpisode
import com.dimi.moviedatabase.util.toSimpleString
import kotlinx.android.synthetic.main.layout_episode_details.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
class EpisodeDetailsFragment : BaseViewMediaFragment<LayoutEpisodeDetailsBinding>(R.layout.layout_episode_details) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSelectedEpisode()?.let { episode ->

            binding.episode = episode
//            name.text = episode.name
//            overview.text = episode.overview
//            air_date.text = episode.airDate.toSimpleString()
//            season_and_episode_number.text = resources.getString(R.string.season_and_episode_number_format, episode.seasonNumber, episode.episodeNumber)

//            requestManager
//                .load(NetworkConstants.BIG_IMAGE_URL_PREFIX + episode.stillPath)
//                .transition(DrawableTransitionOptions.withCrossFade())
//                .into(image)
        }
    }
}