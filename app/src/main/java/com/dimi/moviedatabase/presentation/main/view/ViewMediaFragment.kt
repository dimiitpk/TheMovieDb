package com.dimi.moviedatabase.presentation.main.view

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.request.RequestOptions
import com.dimi.moviedatabase.R
import com.dimi.moviedatabase.business.domain.model.Image
import com.dimi.moviedatabase.business.domain.model.Media
import com.dimi.moviedatabase.business.domain.model.Movie
import com.dimi.moviedatabase.business.domain.state.MediaType
import com.dimi.moviedatabase.business.domain.state.StateMessageCallback
import com.dimi.moviedatabase.business.interactors.SharedUseCasesKeys.MESSAGE_DETAILS_QUERY_SUCCESSFUL
import com.dimi.moviedatabase.business.interactors.tv_show.TvShowEpisodesUseCase.Companion.RETRIEVING_TV_SHOWS_EPISODES_SUCCESSFUL
import com.dimi.moviedatabase.framework.network.NetworkConstants
import com.dimi.moviedatabase.presentation.GlideApp
import com.dimi.moviedatabase.presentation.common.BaseDialogFragment
import com.dimi.moviedatabase.presentation.common.gone
import com.dimi.moviedatabase.presentation.common.visible
import com.dimi.moviedatabase.presentation.images.FullScreenOrientation
import com.dimi.moviedatabase.presentation.main.view.adapter.BackDropSlider
import com.dimi.moviedatabase.presentation.main.view.adapter.ViewMediaPagerAdapter
import com.dimi.moviedatabase.presentation.main.view.fragments.*
import com.dimi.moviedatabase.presentation.main.view.state.SeasonContainerState
import com.dimi.moviedatabase.presentation.main.view.viewmodel.*
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_view_media.*
import kotlinx.android.synthetic.main.layout_media_detail_fragment.*
import kotlinx.android.synthetic.main.layout_media_detail_fragment.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@ExperimentalCoroutinesApi
@FlowPreview
@AndroidEntryPoint
class ViewMediaFragment(
) : BaseDialogFragment(R.layout.fragment_view_media) {

    val viewModel: ViewMediaViewModel by viewModels()

    private lateinit var imageSliderAdapter: BackDropSlider

    private val args: ViewMediaFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.clearViewFields()

        args.mediaTypeName?.let { originalName ->
            viewModel.setMediaType(MediaType.parseFromOriginalName(originalName))
        } ?: viewModel.setMediaType(args.mediaType)

        setupViewPagerAndTabs()

        subscribeObservers()
        setupUI()
    }

    private fun sendMediaAsMessage(media: Media) {

        context?.let {
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    "https://www.themoviedb.org/${media.mediaType.originalName}/${media.id}"
                )
            }
            if (intent.resolveActivity(it.packageManager) != null)
                startActivity(
                    Intent.createChooser(intent, "Share media..")
                )
        }
    }


    override fun onResume() {
        super.onResume()
        loadImages()
        viewModel.getMedia()?.let {
            resetUI()
            if (viewModel.getTrailers().isNullOrEmpty()) {
                loadTrailers()
            }
        } ?: loadDetails()
    }

    private fun setupViewPagerAndTabs() {

        swipe_fragments_container.adapter = ViewMediaPagerAdapter(
            this.childFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ).apply {
            addFragment(AboutFragment(), "About")

            if (viewModel.getMediaType() != MediaType.PERSON) addFragment(CastFragment(), "Cast")

            if (viewModel.getMediaType() == MediaType.TV_SHOW) addFragment(
                SeasonsAndEpisodesFragment(),
                "Seasons"
            )

            if (viewModel.getMediaType() != MediaType.PERSON) addFragment(
                RecommendedMediaFragment(),
                "Recommended"
            ) else addFragment(
                RecommendedMediaFragment(),
                "Movies"
            )

            if (viewModel.getMediaType() != MediaType.PERSON) addFragment(
                SimilarMediaFragment(),
                "Similar"
            ) else addFragment(
                SimilarMediaFragment(),
                "Tv Shows"
            )
        }
        tab_layout.setupWithViewPager(swipe_fragments_container)
    }

    private fun initBackdropsSlider(list: List<Image>) {
        if (backdrop.adapter == null) {
            val newList = if (list.size > 10) list.slice(0..9) else list
            imageSliderAdapter = BackDropSlider(
                GlideApp.with(this).setDefaultRequestOptions(
                    RequestOptions().placeholder(R.drawable.ic_image_place_holder)
                        .error(R.drawable.ic_broken_image)
                )
            ).apply {
                //preloadGlideImages(newList)
                setList(newList)
            }
            backdrop.adapter = imageSliderAdapter
            TabLayoutMediator(backdrop_tabs, backdrop) { _, _ -> }.attach()
        }
    }

    private fun subscribeObservers() {

        viewModel.viewState.observe(viewLifecycleOwner, { viewState ->
            if (viewState != null) {

                viewState.backdrops?.let { list ->
                    if (list.isNotEmpty()) {
                        initBackdropsSlider(list)
                    }
                }
                viewState.media?.let { media ->
                    setMediaProperties(media)
                }
            }
        })

        viewModel.shouldDisplayProgressBar.observe(viewLifecycleOwner, {
            uiController.displayProgressBar(it)
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, { stateMessage ->
            stateMessage?.let { message ->
                when (message.response.message) {
                    MESSAGE_DETAILS_QUERY_SUCCESSFUL -> {
                        viewModel.clearStateMessage()
                        loadTrailers()
                    }
                    RETRIEVING_TV_SHOWS_EPISODES_SUCCESSFUL -> {
                        viewModel.clearStateMessage()
                        viewModel.setSeasonContainerState(SeasonContainerState.EPISODES_VIEW)
                    }
                    else -> {

                        uiController.onResponseReceived(
                            response = message.response,
                            stateMessageCallback = object : StateMessageCallback {
                                override fun removeMessageFromStack() {
                                    viewModel.clearStateMessage()
                                }
                            }
                        )
                    }
                }
            }
        })
    }

    private fun setMediaProperties(media: Media) {

        when (media.mediaType) {
            MediaType.MOVIE -> {
                collapsing_toolbar.tag_line.text = (media as Movie).tagLine
            }
            MediaType.PERSON -> {
                collapsing_toolbar.back_image.visible()
            }
            else -> {
            }
        }

        if (media.posterPath != null) {
            GlideApp.with(requireActivity())
                .load(NetworkConstants.SMALL_IMAGE_URL_PREFIX + media.posterPath)
                .into(collapsing_toolbar.image)
        }
        collapsing_toolbar.name.text = media.title

        if (layout_container.isInvisible) {

            when (media.mediaType) {
                MediaType.MOVIE, MediaType.TV_SHOW -> {
                    collapsing_toolbar.vote_count.text = media.voteCount.toString()
                    collapsing_toolbar.vote_average.text =
                        getString(R.string.vote_average_format, (media.voteAverage * 10).toInt())
                }
                else -> {
                    collapsing_toolbar.vote_average.gone()
                    collapsing_toolbar.vote_count.gone()
                    collapsing_toolbar.tmdb_logo.gone()
                    collapsing_toolbar.people_image.gone()
                }
            }
            layout_container.visible()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearViewFields()
    }

    private fun loadTrailers() {
        viewModel.loadTrailers(viewModel.getMediaType(), args.mediaId)
        resetUI()
    }

    private fun loadDetails() {
        viewModel.loadDetails(viewModel.getMediaType(), args.mediaId)
        resetUI()
    }

    private fun loadImages() {
        viewModel.loadImages(viewModel.getMediaType(), args.mediaId)
        resetUI()
    }

    private fun setupUI() {

        image.setOnClickListener {
            viewModel.getMedia()?.let { media ->
                findNavController().navigate(
                    ViewMediaFragmentDirections.actionViewMediaFragmentToPosterImagesActivity(
                        viewModel.getMediaType(),
                        media.id
                    )
                )
            }
        }


        var isShow = true
        var scrollRange = -1
        app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0) {
                title.text = viewModel.getMedia()?.title
                isShow = true
            } else if (isShow) {
                title.text = " "
                isShow = false
            }
        })

        arrow_back.setOnClickListener {
            findNavController().popBackStack()
        }

        action_send.setOnClickListener {
            viewModel.getMedia()?.let {
                sendMediaAsMessage(it)
            }
        }
    }

    private fun resetUI() {
        focusable_view.requestFocus()
    }
}