package com.dimi.moviedatabase.presentation.images.fragments

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.dimi.moviedatabase.R
import com.dimi.moviedatabase.business.domain.state.StateMessageCallback
import com.dimi.moviedatabase.presentation.GlideApp
import com.dimi.moviedatabase.presentation.common.gone
import com.dimi.moviedatabase.presentation.common.visible
import com.dimi.moviedatabase.presentation.images.*
import com.dimi.moviedatabase.util.Constants.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
import com.dimi.moviedatabase.util.SHARE_INTENT_REQUEST_CODE
import com.dimi.moviedatabase.util.SHARE_INTENT_TITLE
import kotlinx.android.synthetic.main.fragment_image_full_screen.*
import kotlinx.android.synthetic.main.fragment_image_full_screen.progress_bar
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect

@FlowPreview
@ExperimentalCoroutinesApi
class ImageFullScreenFragment : BaseImagesFragment(R.layout.fragment_image_full_screen) {

    private lateinit var currentPositionJob: Job
    var currentPosition = MutableStateFlow(0)

    val viewModel: FullScreenImagesViewModel by viewModels()

    private lateinit var recyclerAdapter: ImagesAdapter

    private val args: ImageFullScreenFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        viewModel.setOrientation(args.orientation)

        initRecyclerView()

        subscribeObservers()

        setupUI()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    viewModel.downloadImage(recyclerAdapter.getItem(currentPosition.value).filePath)
                } else
                //kreiraj error da mora da prihvati ako oce da skine
                    return
            }
            else -> {
            }
        }
    }

    private fun setupFlowCollectors(listSize: Int) {
        currentPositionJob = Job()
        CoroutineScope(Main + currentPositionJob).launch {
            currentPosition.collect {
                if (isAdapterInitialized()) {
                    images_count.text = resources.getString(R.string.number_slash_number, it+1, listSize)
                }
            }
        }
    }

    private fun subscribeObservers() {

        viewModel.viewState.observe(viewLifecycleOwner, { viewState ->
            if (viewState != null) {
                viewState.backdrops?.let { images ->
                    if (images.isNotEmpty() && viewModel.getOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                        recyclerAdapter.setList(images).let {
                            setupFlowCollectors(it)
                        }
                    }
                }
                viewState.posters?.let { images ->
                    if (images.isNotEmpty() && viewModel.getOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                        recyclerAdapter.setList(images).let {
                            currentPosition.value = 0
                            setupFlowCollectors(it)
                        }
                    }
                }

                viewState.sendingImageFileUri?.let { uri ->
                    progress_bar.gone()
                    prepareSharingIntent(uri)
                }
            }
        })

        viewModel.shouldDisplayProgressBar.observe(viewLifecycleOwner, { isDisplayed ->
            if (isDisplayed)
                progress_bar.visible()
            else
                progress_bar.gone()
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, { stateMessage ->
            stateMessage?.let { message ->
                uiController.onResponseReceived(
                    response = message.response,
                    stateMessageCallback = object : StateMessageCallback {
                        override fun removeMessageFromStack() {
                            viewModel.clearStateMessage()
                        }
                    }
                )
            }
        })

        recyclerAdapter.fullScreenMode.observe(viewLifecycleOwner, { fullScreenMode ->
            if (fullScreenMode)
                fullscreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_fullscreen_exit
                    )
                )
            else
                fullscreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_fullscreen
                    )
                )
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if(::currentPositionJob.isInitialized )
            currentPositionJob.cancel()
        recycler_view.adapter = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SHARE_INTENT_REQUEST_CODE -> {
                viewModel.deleteSavedImageForSharing()
            }
        }
    }

    private fun prepareSharingIntent(image: Uri) {

        context?.let {
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, image)
                type = "image/*"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            if (intent.resolveActivity(it.packageManager) != null)
                startActivityForResult(
                    Intent.createChooser(intent, SHARE_INTENT_TITLE),
                    SHARE_INTENT_REQUEST_CODE
                )

            viewModel.setSendingImageFileUri(null)
        }
    }

    private fun isAdapterInitialized() = ::recyclerAdapter.isInitialized

    private fun initRecyclerView() {

        PagerSnapHelper().attachToRecyclerView(recycler_view)

        recycler_view.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            recyclerAdapter = ImagesAdapter(
                GlideApp
                    .with(this)
                    .setDefaultRequestOptions(
                        RequestOptions()
                            .placeholder(R.drawable.ic_image_place_holder)
                            .error(R.drawable.ic_broken_image)
                    )
            )
            adapter = recyclerAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if ((recycler_view.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() != RecyclerView.NO_POSITION)
                        currentPosition.value =
                            (recycler_view.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadImages(args.mediaType, args.mediaId)
    }

    private fun setupUI() {
        arrow_back.setOnClickListener {
            activity?.onBackPressed()
        }
        fullscreen.setOnClickListener {
            recyclerAdapter.changeFullScreenMode()
        }
        download.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                if (permissions.isStoragePermissionGranted())
                    viewModel.downloadImage(recyclerAdapter.getItem(currentPosition.value).filePath)
            } else {
                viewModel.downloadImage(recyclerAdapter.getItem(currentPosition.value).filePath)
            }
        }
        share.setOnClickListener {
            progress_bar.visible()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                if (permissions.isStoragePermissionGranted())
                    viewModel.sendImage(recyclerAdapter.getItem(currentPosition.value).filePath)
            } else {
                viewModel.sendImage(recyclerAdapter.getItem(currentPosition.value).filePath)
            }
        }
    }
}