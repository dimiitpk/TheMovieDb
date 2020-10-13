package com.dimi.moviedatabase.presentation.main.view.fragments

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewTreeObserver
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dimi.moviedatabase.R
import com.dimi.moviedatabase.business.domain.model.Media
import com.dimi.moviedatabase.business.domain.model.TvShow
import com.dimi.moviedatabase.business.domain.model.Video
import com.dimi.moviedatabase.business.domain.state.MediaType
import com.dimi.moviedatabase.databinding.FragmentAboutBinding
import com.dimi.moviedatabase.presentation.common.VisibilityState.Displayed
import com.dimi.moviedatabase.presentation.common.VisibilityState.Hidden
import com.dimi.moviedatabase.presentation.common.adapters.VideoListAdapter
import com.dimi.moviedatabase.presentation.common.isEllipsized
import com.dimi.moviedatabase.presentation.common.visible
import com.dimi.moviedatabase.presentation.main.search.enums.ViewType
import com.dimi.moviedatabase.presentation.main.view.OnAboutFragmentDataBindingListener
import com.dimi.moviedatabase.presentation.main.view.ViewMediaFragmentDirections
import com.dimi.moviedatabase.presentation.main.view.viewmodel.*
import com.dimi.moviedatabase.util.Genre
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@ExperimentalCoroutinesApi
@FlowPreview
class AboutFragment :
    BaseViewMediaFragment<FragmentAboutBinding>(R.layout.fragment_about),
    OnAboutFragmentDataBindingListener {

    private var youtubePlayerView: YouTubePlayerView? = null
    private var youtubePlayer: YouTubePlayer? = null

    private lateinit var videoListAdapter: VideoListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            listener = this@AboutFragment
            lifecycleOwner = requireParentFragment()
            viewModel = this@AboutFragment.viewModel
        }

        youtubePlayerView = binding.trailer
        subscribeObservers()
    }


    private fun subscribeObservers() {

        viewModel.viewState.observe(viewLifecycleOwner, { viewState ->
            if (viewState != null) {
                viewState.media?.let { media ->
                    binding.aboutContainer.visible()
                    readMoreOrLessVisibility(media)
                }
                viewState.trailers?.let { trailers ->
                    if (trailers.isNotEmpty()) {
                        if (trailers.size > 1)
                            setupTrailerRecyclerView(trailers)
                    }
                }
            }
        })

        youtubePlayerView?.let {
            viewLifecycleOwner.lifecycle.addObserver(it)
        }

        youtubePlayerView?.addFullScreenListener(object : YouTubePlayerFullScreenListener {
            override fun onYouTubePlayerEnterFullScreen() {
                viewModel.createFullScreenInfo()
                youtubePlayerView!!.exitFullScreen()
            }

            override fun onYouTubePlayerExitFullScreen() {
                youtubePlayerView!!.exitFullScreen()
            }
        })

        youtubePlayerView?.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {

            override fun onReady(youTubePlayer: YouTubePlayer) {
                youtubePlayer = youTubePlayer

                viewModel.getTrailers()?.let { videoList ->
                    if (videoList.isNotEmpty()) {
                        videoList[0].key.let { videoId ->
                            youtubePlayer!!.cueVideo(videoId, 0f)
                        }
                    }
                }
            }
        })
    }

    private fun readMoreOrLessVisibility(media: Media) {

        if (media.overview.isNotBlank()) {
            binding.overview.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    binding.overview.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    if (binding.overview.layout.isEllipsized())
                        binding.readMoreText.visible()
                }
            })
        }
    }

    private fun setupTrailerRecyclerView(trailers: List<Video>) {

        binding.videosRecyclerView.apply {

            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            setHasFixedSize(true)
            videoListAdapter =
                VideoListAdapter(
                    trailers,
                    lifecycle
                )
            adapter = videoListAdapter
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.backdrop.id -> {
                if (viewModel.isThereAnyValidBackdrop())
                    viewModel.getMedia()?.let { media ->
                        findNavController().navigate(
                            ViewMediaFragmentDirections.actionViewMediaFragmentToPosterImagesActivity(
                                viewModel.getMediaType(),
                                media.id,
                                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                            )
                        )
                    }
            }
            binding.posters.id -> {
                if (viewModel.isThereAnyValidPoster())
                    viewModel.getMedia()?.let { media ->
                        findNavController().navigate(
                            ViewMediaFragmentDirections.actionViewMediaFragmentToPosterImagesActivity(
                                viewModel.getMediaType(),
                                media.id,
                                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                            )
                        )
                    }
            }
            binding.moreVideos.id -> {
                if (viewModel.isVideosPlayerDisplayed())
                    viewModel.setVideosPlayerState(Hidden)
                else
                    viewModel.setVideosPlayerState(Displayed)
            }
            binding.playTrailerButton.id -> {
                if (viewModel.isTrailerPlayerDisplayed())
                    viewModel.setTrailerPlayerState(Hidden)
                else
                    viewModel.setTrailerPlayerState(Displayed)
            }
            binding.readMoreText.id -> {
                if (binding.overview.layout.isEllipsized()) {
                    binding.overview.maxLines = Int.MAX_VALUE
                    binding.overview.ellipsize = null
                    binding.readMoreText.text = getString(R.string.read_less)
                } else {
                    binding.overview.maxLines = 3
                    binding.overview.ellipsize = TextUtils.TruncateAt.END
                    binding.readMoreText.text = getString(R.string.read_more)
                }
            }
        }
    }

    override fun onClickViewType(viewType: ViewType, value: String) {
        when (viewType) {
            ViewType.GENRE -> {
                viewModel.getMediaType().let { mediaType ->
                    if (mediaType != MediaType.PERSON)
                        findNavController().navigate(
                            ViewMediaFragmentDirections.actionViewMediaFragmentToSearchFragment(
                                tabId = Genre(mediaType).getGenreIdByName(value),
                                mediaType = mediaType,
                                viewType = ViewType.GENRE
                            )
                        )
                }
            }
            ViewType.NETWORK -> {
                viewModel.getMedia()?.let { media ->
                    (media as TvShow).networks?.single { it.name == value }?.let { network ->
                        findNavController().navigate(
                            ViewMediaFragmentDirections.actionViewMediaFragmentToSearchFragment(
                                network = network,
                                mediaType = viewModel.getMediaType(),
                                viewType = ViewType.NETWORK
                            )
                        )
                    }
                }
            }
            else -> {
            }
        }
    }

    override fun onDestroy() {
        youtubePlayerView?.release()
        youtubePlayerView = null
        youtubePlayer = null
        super.onDestroy()
    }
}
