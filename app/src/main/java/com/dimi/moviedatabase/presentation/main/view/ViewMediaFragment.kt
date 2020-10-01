package com.dimi.moviedatabase.presentation.main.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.request.RequestOptions
import com.dimi.moviedatabase.R
import com.dimi.moviedatabase.business.domain.model.Image
import com.dimi.moviedatabase.business.domain.model.Media
import com.dimi.moviedatabase.business.domain.state.MediaType
import com.dimi.moviedatabase.business.domain.state.StateMessageCallback
import com.dimi.moviedatabase.business.interactors.SharedUseCasesKeys.MESSAGE_DETAILS_QUERY_SUCCESSFUL
import com.dimi.moviedatabase.business.interactors.SharedUseCasesKeys.USE_CASE_TRAILERS
import com.dimi.moviedatabase.business.interactors.tv_show.TvShowEpisodesUseCase.Companion.RETRIEVING_TV_SHOWS_EPISODES_SUCCESSFUL
import com.dimi.moviedatabase.databinding.FragmentViewMediaBinding
import com.dimi.moviedatabase.presentation.GlideApp
import com.dimi.moviedatabase.presentation.common.BaseDBDialogFragment
import com.dimi.moviedatabase.presentation.common.OnDataBindingClickListener
import com.dimi.moviedatabase.presentation.common.visible
import com.dimi.moviedatabase.presentation.main.view.adapter.BackDropSlider
import com.dimi.moviedatabase.presentation.main.view.adapter.ViewMediaPagerAdapter
import com.dimi.moviedatabase.presentation.main.view.fragments.*
import com.dimi.moviedatabase.presentation.main.view.state.SeasonContainerState
import com.dimi.moviedatabase.presentation.main.view.viewmodel.*
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@ExperimentalCoroutinesApi
@FlowPreview
@AndroidEntryPoint
class ViewMediaFragment :
    BaseDBDialogFragment<FragmentViewMediaBinding>(R.layout.fragment_view_media),
    OnDataBindingClickListener {

    val viewModel: ViewMediaViewModel by viewModels()

    private lateinit var imageSliderAdapter: BackDropSlider

    private val args: ViewMediaFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.listener = this

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
        viewModel.getMedia()?.let {
            resetUI()

            if (viewModel.getTrailers().isNullOrEmpty())
                loadTrailers()
        } ?: loadDetails()
    }

    private fun setupViewPagerAndTabs() {

        binding.swipeFragmentsContainer.adapter = ViewMediaPagerAdapter(
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
        binding.tabLayout.setupWithViewPager(binding.swipeFragmentsContainer)
    }

    private fun initBackdropsSlider(list: List<Image>) {
        if (binding.collapsingToolbarIncluded.backdrop.adapter == null) {
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
            binding.collapsingToolbarIncluded.backdrop.adapter = imageSliderAdapter
            TabLayoutMediator(
                binding.collapsingToolbarIncluded.backdropTabs,
                binding.collapsingToolbarIncluded.backdrop
            ) { _, _ -> }.attach()
        }
    }

    private fun subscribeObservers() {

        viewModel.viewState.observe(viewLifecycleOwner, { viewState ->
            if (viewState != null) {

                viewState.backdrops?.let { list ->
                    if (list.isNotEmpty())
                        initBackdropsSlider(list)

                }
                viewState.media?.let { media ->
                    binding.media = media
                    binding.layoutContainer.visible()
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
                    USE_CASE_TRAILERS -> {
                        viewModel.clearStateMessage()
                        loadImages()
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

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearViewFields()
    }

    private fun loadTrailers() {

        if (viewModel.getMediaType() == MediaType.PERSON) loadImages()
        else viewModel.loadTrailers(viewModel.getMediaType(), args.mediaId)
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

        var isShow = true
        var scrollRange = -1
        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0) {
                binding.title.text = viewModel.getMedia()?.title
                isShow = true
            } else if (isShow) {
                binding.title.text = " "
                isShow = false
            }
        })

    }

    private fun resetUI() {
        binding.focusableView.requestFocus()
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.actionSend.id -> {
                viewModel.getMedia()?.let {
                    sendMediaAsMessage(it)
                }
            }
            binding.arrowBack.id -> {
                findNavController().popBackStack()
            }
            binding.collapsingToolbarIncluded.image.id -> {
                if (viewModel.isThereAnyValidPoster())
                    viewModel.getMedia()?.let { media ->
                        findNavController().navigate(
                            ViewMediaFragmentDirections.actionViewMediaFragmentToPosterImagesActivity(
                                viewModel.getMediaType(),
                                media.id
                            )
                        )
                    }
            }
        }
    }
}