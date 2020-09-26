package com.dimi.moviedatabase.presentation.main.view.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.Layout
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dimi.moviedatabase.R
import com.dimi.moviedatabase.business.domain.model.*
import com.dimi.moviedatabase.business.domain.state.MediaType
import com.dimi.moviedatabase.framework.network.NetworkConstants
import com.dimi.moviedatabase.presentation.GlideApp
import com.dimi.moviedatabase.presentation.common.*
import com.dimi.moviedatabase.presentation.common.YoutubePlayerState.Displayed
import com.dimi.moviedatabase.presentation.common.YoutubePlayerState.Hidden
import com.dimi.moviedatabase.presentation.common.adapters.VideoListAdapter
import com.dimi.moviedatabase.presentation.main.search.SearchFragmentArgs
import com.dimi.moviedatabase.presentation.main.search.enums.ViewType
import com.dimi.moviedatabase.presentation.main.view.ViewMediaFragmentDirections
import com.dimi.moviedatabase.presentation.main.view.viewmodel.getMedia
import com.dimi.moviedatabase.presentation.main.view.viewmodel.getMediaType
import com.dimi.moviedatabase.util.Genre
import com.dimi.moviedatabase.util.toHoursAndMinutesText
import com.dimi.moviedatabase.util.toSimpleString
import com.google.android.material.button.MaterialButton
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.android.synthetic.main.fragment_about.*
import kotlinx.android.synthetic.main.info_details_table_row.view.*
import kotlinx.coroutines.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


@ExperimentalCoroutinesApi
@FlowPreview
class AboutFragment :
    BaseViewMediaFragment(R.layout.fragment_about) {

    private var videoList: ArrayList<Video> = ArrayList()
    private var youtubePlayerView: YouTubePlayerView? = null
    private var youtubePlayer: YouTubePlayer? = null

    private lateinit var videoListAdapter: VideoListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setTrailerPlayerState(Hidden)
        viewModel.setVideosPlayerState(Hidden)

        youtubePlayerView = view.findViewById(R.id.trailer)
        subscribeObservers()
        setupUI()
    }

    private fun setMediaDetailInfo(media: Media) {

        details_info_container.apply {

            if (childCount > 0)
                removeAllViews()

            when (media.mediaType) {
                MediaType.MOVIE, MediaType.TV_SHOW -> {
                    addView(
                        createDetailInfoLine(
                            context.getString(R.string.original_title),
                            media.originalTitle ?: "-"
                        )
                    )
                    addView(
                        createDetailInfoLine(
                            context.getString(R.string.status),
                            media.status ?: "-"
                        )
                    )
                    addView(
                        createDetailInfoLine(
                            context.getString(R.string.runtime),
                            media.runtime?.toHoursAndMinutesText() ?: "-"
                        )
                    )
                    when (media) {
                        is Movie -> {
                            addView(
                                createDetailInfoLine(
                                    context.getString(R.string.release_date),
                                    media.releaseDate.toSimpleString()
                                )
                            )
                            addView(
                                createDetailInfoLine(
                                    context.getString(R.string.budget),
                                    NumberFormat.getCurrencyInstance(Locale.US).apply {
                                        maximumFractionDigits = 0
                                        minimumFractionDigits = 0
                                    }.format(media.budget ?: 0) ?: "-"
                                )
                            )
                            addView(
                                createDetailInfoLine(
                                    resources.getString(R.string.revenue),
                                    NumberFormat.getCurrencyInstance(Locale.US).apply {
                                        maximumFractionDigits = 0
                                        minimumFractionDigits = 0
                                    }.format(media.revenue ?: 0) ?: "-"
                                )
                            )
                        }
                        is TvShow -> {
                            addView(
                                createDetailInfoLine(
                                    context.getString(R.string.first_air_date),
                                    media.firstAirDate.toSimpleString()
                                )
                            )
                            addView(
                                createDetailInfoLine(
                                    context.getString(R.string.last_air_date),
                                    media.lastAirDate.toSimpleString()
                                )
                            )
                        }
                    }
                }
                MediaType.PERSON -> {
                    addView(
                        createDetailInfoLine(
                            context.getString(R.string.birthday),
                            (media as Person).birthday.toSimpleString()
                        )
                    )
                    addView(
                        createDetailInfoLine(
                            context.getString(R.string.place_of_birth),
                            media.placeOfBirth ?: "-"
                        )
                    )
                    addView(
                        createDetailInfoLine(
                            context.getString(R.string.death_day),
                            media.deathDay.toSimpleString()
                        )
                    )
                    addView(
                        createDetailInfoLine(
                            context.getString(R.string.gender),
                            if (media.gender == 2) context.getString(R.string.male) else context.getString(
                                R.string.female
                            )
                        )
                    )
                    addView(
                        createDetailInfoLine(
                            context.getString(R.string.known_for_department),
                            media.department ?: "-"
                        )
                    )
                }
            }
        }
    }

    private fun createButton(
        root: ViewGroup,
    ): MaterialButton {

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(10, 0, 0, 0)

        val button = layoutInflater.inflate(
            R.layout.layout_genre_button,
            root,
            false
        ) as MaterialButton
        root.addView(button, layoutParams)
        return button
    }

    private fun setMediaProperties(media: Media) {

        when (media.mediaType) {
            MediaType.MOVIE, MediaType.TV_SHOW -> {
                media.genres?.let { genres ->
                    if (genre_container.childCount < genres.size)
                        for (g in genres) {
                            createButton(
                                genre_container,
                            ).apply {
                                text = Genre(viewModel.getMediaType()).getGenreName(g)
                                setOnClickListener {
                                    findNavController().navigate(
                                        R.id.action_viewMediaFragment_to_searchFragment,
                                        SearchFragmentArgs(
                                            tabId = g,
                                            mediaType = viewModel.getMediaType(),
                                            viewType = ViewType.GENRE
                                        ).toBundle()
                                    )
                                }
                            }
                        }

                    if (genre_container.childCount > 0) {
                        genres_info.visible()
                        genre_scroll_container.visible()
                    }
                }
                if (media is TvShow) {
                    media.networks?.let { networks ->

                        if (network_container.childCount < networks.size)
                            for (item in networks)
                                createButton(
                                    network_container,
                                ).apply {
                                    text = item.name
                                    setOnClickListener {
                                        findNavController().navigate(
                                            R.id.action_viewMediaFragment_to_searchFragment,
                                            SearchFragmentArgs(
                                                mediaType = viewModel.getMediaType(),
                                                network = item,
                                                viewType = ViewType.NETWORK
                                            ).toBundle()
                                        )
                                    }
                                }

                        if (network_container.childCount > 0) {
                            network_infos.visible()
                            network_scroll_container.visible()
                        }

                    }
                }
            }
            MediaType.PERSON ->
                (media as Person).alsoKnownAs?.let { list ->
                    genres_info.text = getString(R.string.also_known_as)
                    if (genre_container.childCount < list.size)
                        for (item in list) {
                            if (item.isNotEmpty()) {
                                createButton(
                                    genre_container,
                                ).apply {
                                    text = item
                                }
                            }
                        }
                    if (genre_container.childCount > 0) {
                        genres_info.visible()
                        genre_scroll_container.visible()
                    }
                }

        }
        if( media.overview.isNotBlank() ) {
            overview.text = media.overview

            overview.visible()

            overview.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    overview.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    if (isTextViewEllipsized(overview.layout))
                        read_more_text.visible()
                }
            })

            read_more_text.setOnClickListener {
                if (isTextViewEllipsized(overview.layout)) {
                    overview.maxLines = Int.MAX_VALUE
                    overview.ellipsize = null
                    read_more_text.text = getString(R.string.read_less)
                } else {
                    overview.maxLines = 3
                    overview.ellipsize = TextUtils.TruncateAt.END
                    read_more_text.text = getString(R.string.read_more)
                }
            }
        }

        setMediaDetailInfo(media)

        about_container.visible()
    }

    private fun createDetailInfoLine(key: String, value: String): View {
        return layoutInflater.inflate(R.layout.info_details_table_row, null).apply {
            info_line_key.text = key
            info_line_value.text = value
        }
    }

    private fun isTextViewEllipsized(layout: Layout?): Boolean {
        layout?.let {
            val lines: Int = it.lineCount
            return if (lines > 0)
                it.getEllipsisCount(lines - 1) > 0
            else false
        } ?: return false
    }

    private fun subscribeObservers() {

        viewModel.viewState.observe(viewLifecycleOwner, { viewState ->
            if (viewState != null) {
                viewState.media?.let { media ->
                    setMediaProperties(media)
                }
                viewState.trailers?.let { trailers ->
                    if (trailers.isNotEmpty() && videoList.isEmpty()) {
                        for (trailer in trailers)
                            videoList.add(trailer)

                        if (trailers.size > 1)
                            setupTrailerRecyclerView(videoList)
                    }
                }
                viewState.posters?.let { images ->
                    if (images.isNotEmpty()) {
                        GlideApp.with(this)
                            .load(NetworkConstants.ORIGINAL_IMAGE_URL_PREFIX + images[0].filePath)
                            .into(posters)
                        posters_amount_text.text =
                            resources.getString(R.string.posters_amount_format, images.size)

                        posters.visible()
                        posters_amount_text.visible()
                        media_text.visible()
                    }
                }
                viewState.backdrops?.let { images ->
                    if (images.isNotEmpty()) {
                        GlideApp.with(this)
                            .load(NetworkConstants.ORIGINAL_IMAGE_URL_PREFIX + images[0].filePath)
                            .into(backdrop)
                        backdrop_amount_text.text =
                            resources.getString(R.string.backdrop_amount_format, images.size)

                        backdrop.visible()
                        backdrop_amount_text.visible()
                        media_text.visible()
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

                if (videoList.isNotEmpty()) {
                    videoList[0].key.let { videoId ->
                        showPlayTrailerButton(true)
                        youtubePlayer!!.cueVideo(videoId, 0f)
                    }
                }
            }
        })


        viewModel.trailerPlayerState.observe(viewLifecycleOwner, { state ->

            when (state) {
                is Hidden -> {
                    hideYoutubePlayer()
                }
                is Displayed -> {
                    displayYoutubePlayer()
                }
            }
        })

        viewModel.videosPlayerState.observe(viewLifecycleOwner, { state ->
            when (state) {
                is Hidden -> {
                    hideVideoRecyclerView()
                }
                is Displayed -> {
                    showVideoRecyclerView()
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        youtubePlayerView?.release()
        youtubePlayerView = null
        youtubePlayer = null
    }

    private fun setupTrailerRecyclerView(trailers: ArrayList<Video>) {

        more_videos.text = getString(R.string.videos_with_length, trailers.size)
        more_videos.visible()
        videos_recycler_view.apply {

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

    private fun showPlayTrailerButton(show: Boolean) {
        if (show) {
            play_trailer_button.visible()
        } else {
            play_trailer_button.invisible()
        }
    }

    private fun displayYoutubePlayer() {
        trailer.fadeIn(
            object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(p0: Animator?) {
                    trailer.visible()
                }
            }
        )
        play_trailer_button.text = resources.getString(R.string.hide_trailer)
    }

    private fun hideYoutubePlayer() {
        trailer.fadeOut(
            object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(p0: Animator?) {
                    trailer.gone()
                }
            })
        play_trailer_button.text = resources.getString(R.string.play_trailer)
    }

    private fun hideVideoRecyclerView() {
        videos_container.gone()
        videos_recycler_view.gone()
        videos_recycler_view_container.gone()
        more_videos.iconGravity = MaterialButton.ICON_GRAVITY_END
        more_videos.text = getString(R.string.videos_with_length, videoList.size)
    }

    private fun setupUI() {

        play_trailer_button.setOnClickListener {
            if (viewModel.isTrailerPlayerDisplayed())
                viewModel.setTrailerPlayerState(Hidden)
            else
                viewModel.setTrailerPlayerState(Displayed)
        }

        more_videos.setOnClickListener {
            if (viewModel.isVideosPlayerDisplayed())
                viewModel.setVideosPlayerState(Hidden)
            else
                viewModel.setVideosPlayerState(Displayed)
        }

        posters.setOnClickListener {
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

        backdrop.setOnClickListener {
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
    }

    private fun showVideoRecyclerView() {
        videos_container.visible()
        videos_recycler_view.visible()
        videos_recycler_view_container.visible()
        more_videos.text = getString(R.string.hide_videos)
        more_videos.iconGravity = MaterialButton.ICON_GRAVITY_START
    }
}
